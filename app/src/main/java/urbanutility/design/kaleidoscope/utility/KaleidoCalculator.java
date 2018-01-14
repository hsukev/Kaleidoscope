package urbanutility.design.kaleidoscope.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urbanutility.design.kaleidoscope.datatypes.BaseCurrencyType;
import urbanutility.design.kaleidoscope.datatypes.OrderType;
import urbanutility.design.kaleidoscope.datatypes.PositionType;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;

/**
 * Created by Jason on 1/13/2018.
 */

public class KaleidoCalculator {

    private ArrayList<OrderType> FilterBuySell(List<OrderType> list, String type) {
        ArrayList<OrderType> outputList = new ArrayList<OrderType>();
        for (OrderType entry: list) {
            if (type.equalsIgnoreCase(type)) {
                outputList.add(entry);
            }
        }
        return outputList;
    }
    private long Sum(List<OrderType> list, String type) {
        long sum = 0L;
        for (OrderType entry: list) {
            if (type == "Amount") sum += entry.amount;
            if (type == "Fee") sum += entry.txFee;
        }
        return sum;
    }

    public void CalculatePosition (
            ArrayList<KaleidoBalance> inputBalance,
            ArrayList<KaleidoOrder> inputOrder,
            BaseCurrencyType btcBase,
            BaseCurrencyType usdBase
    ) {
        ArrayList<KaleidoBalance> balanceList = inputBalance;
        ArrayList<OrderType> orderList;
        BaseCurrencyType BtcBase;

        /* Put orders into Hashmap by symbol */
        Map<String, ArrayList<OrderType>> orderDict = new HashMap<String, ArrayList<OrderType>>();
        for (KaleidoOrder entry: inputOrder) {
            OrderType orderType = entry.getOrdertype();
            if (orderDict.containsKey(orderType.symbol)) {
                orderDict.get(orderType.symbol).add(orderType);
            } else {
                orderDict.put(orderType.symbol, new ArrayList<OrderType>());
            }
        }

        for (String entry: orderDict.keySet()) {
            ArrayList<OrderType> sorted_list = SortListByTime(orderDict.get(entry));
            ArrayList<OrderType> buyList = FilterBuySell(sorted_list, "buy");
            ArrayList<OrderType> sellList = FilterBuySell(sorted_list, "sell");

            long totalBuyAmount = Sum(buyList, "Amount");
            long totalSellAmount = Sum(sellList, "Amount");
            long txFee = 0L;
            PositionType BtcPosition = new PositionType();
            PositionType UsdPosition = new PositionType();

            if (totalBuyAmount > totalSellAmount) {
                /* Calculate realized gain */
                this.calcRealizedGainLoss(buyList, sellList, BtcPosition, UsdPosition);
                /* Calculate unrealized gain */
                this.calcUnrealizedGainLoss(buyList);
            } else {
                txFee = Sum(buyList, "Fee") + Sum(sellList, "Fee");
            }
            btcBase.positions;
            usdBase.positions;
        }
    }

    private void calcRealizedGainLoss (
            ArrayList<OrderType> buyList,
            ArrayList<OrderType> sellList,
            PositionType BtcPosition,
            PositionType UsdPosition) {

    }

    private void calcUnrealizedGainLoss (ArrayList<OrderType> buyList) {

    }

    private ArrayList<OrderType> SortListByTime (ArrayList<OrderType> in) {
        ArrayList<OrderType> list = new ArrayList<OrderType>(in);
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
    }
}
