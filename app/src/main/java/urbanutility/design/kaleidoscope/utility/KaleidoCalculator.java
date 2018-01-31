package urbanutility.design.kaleidoscope.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.datatypes.OrderType;
import urbanutility.design.kaleidoscope.datatypes.PositionType;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoBaseCurrency;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by Jason on 1/13/2018.
 */

public class KaleidoCalculator extends KaleidoMethods {
    public static List<KaleidoBaseCurrency> CalculatePosition(
            List<KaleidoBalance> inputBalance,
            List<KaleidoOrder> inputOrder,
            /* Expect BaseCurrencyType.type defined by caller to "BTC" or "USD"
            *  BaseCurrencyType.position should be blank from caller
            *  */
            List<KaleidoBaseCurrency> baseCurrency,
            /* Expect exchange: "Gdax", symbol: "BTCUSD" also be part of exchange Market data */
            List<LiveMarketType> marketData
    ) {
        List<KaleidoBalance> balanceList = inputBalance;
        List<OrderType> orderList;
        KaleidoBaseCurrency BtcBase = new KaleidoBaseCurrency("BTC");
        KaleidoBaseCurrency UsdBase = new KaleidoBaseCurrency("USD");

        /* Put orders into Hashmap by symbol */
        Map<String, ArrayList<OrderType>> orderDict = new HashMap<String, ArrayList<OrderType>>();
        for (KaleidoOrder entry : inputOrder) {
            OrderType orderType = entry.getOrdertype();
            if (orderDict.containsKey(orderType.symbol)) {
                orderDict.get(orderType.symbol).add(orderType);
            } else {
                orderDict.put(orderType.symbol, new ArrayList<OrderType>());
            }
        }

        for (String key : orderDict.keySet()) {
            List<OrderType> sorted_list = SortListByTime(orderDict.get(key));
            List<OrderType> buyList = FilterOrderType(sorted_list, "buy");
            List<OrderType> sellList = FilterOrderType(sorted_list, "sell");

            double totalBuyAmount = Sum(buyList, "Amount");
            double totalSellAmount = Sum(sellList, "Amount");
            double txFee = 0L;

            PositionType BtcPosition = new PositionType();
            PositionType UsdPosition = new PositionType();

            if (totalBuyAmount > totalSellAmount) {
                /* Calculate realized gain */
                calcRealizedGainLoss(buyList, sellList, BtcPosition, UsdPosition);
                /* Calculate unrealized gain */
                calcUnrealizedGainLoss(key, buyList, BtcPosition, UsdPosition, marketData);
            } else {
                double BtcUsdPrice = FilterLiveMarket(marketData, "Gdax", "BTC").price;
                txFee = Sum(buyList, "Fee") + Sum(sellList, "Fee");
                BtcPosition.realizedGain = Sum(sellList, "Total") - Sum(buyList, "Fee");
                UsdPosition.realizedGain = BtcPosition.realizedGain * BtcUsdPrice;
            }
            BtcPosition.changePercent = (BtcPosition.currentVal != 0) ?
                    ((BtcPosition.currentVal / BtcPosition.cost) - 1) * 100 : 0;
            UsdPosition.changePercent = (UsdPosition.currentVal != 0) ?
                    ((UsdPosition.currentVal / UsdPosition.cost) - 1) * 100 : 0;
            BtcPosition.symbol = key;
            UsdPosition.symbol = key;
            BtcBase.getBaseCurrencyType().positions.add(BtcPosition);
            UsdBase.getBaseCurrencyType().positions.add(UsdPosition);

        }
        if (baseCurrency.get(0).getBaseCurrencyType().type.equals("BTC")) {
            baseCurrency.set(0, BtcBase);
            baseCurrency.set(1, UsdBase);
        } else {
            baseCurrency.set(1, BtcBase);
            baseCurrency.set(0, UsdBase);
        }

        return baseCurrency;




    }

    private static void calcRealizedGainLoss(
            List<OrderType> buyList,
            List<OrderType> sellList,
            PositionType BtcPosition,
            PositionType UsdPosition
    ) {
        double BtcTxFee = 0L;
        double UsdTxFee = 0L;

        while (!sellList.isEmpty()) {
            OrderType oldestSell = sellList.get(sellList.size() - 1);
            while (!buyList.isEmpty() && oldestSell.amount > 0) {
                OrderType oldestBuy = buyList.get(buyList.size() - 1);
                double amountBalance = oldestSell.txFee - oldestBuy.txFee;
                if (amountBalance < 0) {
                    BtcPosition.realizedGain += oldestSell.amount * (oldestSell.price - oldestBuy.price);
                    UsdPosition.realizedGain += BtcPosition.realizedGain * oldestSell.btcUsdRate;
                    oldestSell.amount = 0;
                    oldestBuy.amount = Math.abs(amountBalance);
                } else if (amountBalance > 0) {
                    BtcPosition.realizedGain += oldestBuy.amount * (oldestSell.price - oldestBuy.price);
                    UsdPosition.realizedGain += BtcPosition.realizedGain * oldestBuy.btcUsdRate;
                    oldestSell.amount = Math.abs(amountBalance);
                    oldestBuy.amount = 0;
                } else {
                    BtcPosition.realizedGain += oldestBuy.amount * (oldestSell.price - oldestBuy.price);
                    UsdPosition.realizedGain += BtcPosition.realizedGain * oldestBuy.btcUsdRate;
                    oldestSell.amount = 0;
                    oldestBuy.amount = 0;
                    break;
                }
                if (oldestBuy.amount == 0) {
                    BtcTxFee += oldestBuy.txFee;
                    UsdTxFee += BtcTxFee * oldestBuy.btcUsdRate;
                    buyList.remove(buyList.size() - 1);
                }
            }
            if (oldestSell.amount == 0) {
                BtcTxFee += oldestSell.txFee;
                UsdTxFee += BtcTxFee * oldestSell.btcUsdRate;
                sellList.remove(sellList.size() - 1);
            }
        }
        BtcPosition.realizedGain -= BtcTxFee;
        UsdPosition.realizedGain -= UsdTxFee;
    }

    private static void calcUnrealizedGainLoss(
            String key,
            List<OrderType> buyList,
            PositionType BtcPosition,
            PositionType UsdPosition,
            List<LiveMarketType> marketData
    ) {
        double BtcTxFee = Sum(buyList, "Fee");
        double BtcTotalAmount = Sum(buyList, "Amount");
        List<LiveMarketType> coinMarketRateList = FilterLiveMarketBySymbol(marketData, key);
        LiveMarketType coinMarketRate = FilterLiveMarketByExchange(coinMarketRateList, buyList.get(buyList.size() - 1).exchange);
        LiveMarketType btcUsdMarketRate = FilterLiveMarket(marketData, "Gdax", "BTC");

        while (!buyList.isEmpty()) {
            OrderType oldestBuy = buyList.get(buyList.size() - 1);
            coinMarketRate = FilterLiveMarketByExchange(coinMarketRateList, oldestBuy.exchange);
            BtcPosition.unrealizedGain += oldestBuy.amount * (coinMarketRate.price - oldestBuy.price);
            UsdPosition.unrealizedGain += BtcPosition.unrealizedGain * oldestBuy.btcUsdRate;
            BtcPosition.cost += oldestBuy.amount * oldestBuy.price;
            UsdPosition.cost += BtcPosition.cost * btcUsdMarketRate.price;
            buyList.remove(buyList.size() - 1);
        }

        BtcPosition.currentVal = BtcTotalAmount * coinMarketRate.price;
        UsdPosition.currentVal = BtcPosition.currentVal * btcUsdMarketRate.price;
        BtcPosition.unrealizedGain -= BtcTxFee;
        UsdPosition.unrealizedGain -= BtcTxFee * btcUsdMarketRate.price;
        BtcPosition.costPerUnit = BtcPosition.cost / BtcTotalAmount;
        UsdPosition.costPerUnit = BtcPosition.costPerUnit * btcUsdMarketRate.price;
        BtcPosition.amount = BtcTotalAmount;
        UsdPosition.amount = BtcTotalAmount;
    }

    private static ArrayList<OrderType> SortListByTime(ArrayList<OrderType> in) {
        ArrayList<OrderType> list = new ArrayList<>(in);
        Collections.sort(list, new Comparator<OrderType>() {
            public int compare(OrderType o1, OrderType o2) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .parse(o1.time)
                            .compareTo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .parse(o2.time));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });
        return list;
    }
}
