package urbanutility.design.kaleidoscope.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoPosition;
import urbanutility.design.kaleidoscope.model.OrderOrDeposit;

/**
 * Created by jerye on 3/7/2018.
 */

public class KaleidoCalculator2 {

    public static Iterable<KaleidoPosition> CalculatePositions(
            List<KaleidoDeposits> deposits,
            List<KaleidoOrder> orders,
            List<KaleidoLiveMarket> liveMarkets
    ) {

        List<OrderOrDeposit> orderOrDepositsList = new ArrayList<>();
        Map<String, KaleidoPosition> positionMap = new HashMap<>();

        // add all orders and deposits into mixed type list
        for (KaleidoOrder order : orders) {
            orderOrDepositsList.add(new OrderOrDeposit(order));
        }
        for (KaleidoDeposits deposit : deposits) {
            orderOrDepositsList.add(new OrderOrDeposit(deposit));
        }


        // comparator that sorts mix list of orders and deposits
        Comparator<OrderOrDeposit> comparator = new Comparator<OrderOrDeposit>() {
            @Override
            public int compare(OrderOrDeposit t1, OrderOrDeposit t2) {
                if (t1.isOrder() && t2.isOrder()) {
                    return t1.getKaleidoOrder().getTime().compareTo(t2.getKaleidoOrder().getTime());
                } else if (!t1.isOrder() && !t2.isOrder()) {
                    return t1.getKaleidoDeposits().getTime().compareTo(t2.getKaleidoDeposits().getTime());
                } else if (t1.isOrder() && !t2.isOrder()) {
                    return t1.getKaleidoOrder().getTime().compareTo(t2.getKaleidoDeposits().getTime());
                } else {
                    return t1.getKaleidoDeposits().getTime().compareTo(t2.getKaleidoOrder().getTime());
                }
            }
        };

        // sort using comparator
        Collections.sort(orderOrDepositsList, comparator);

        // calculate static map (no live market info yet)
        for (OrderOrDeposit orderOrDeposit : orderOrDepositsList) {
            KaleidoPosition position;
            String coin;

            // order
            if (orderOrDeposit.isOrder()) {
                KaleidoOrder order = orderOrDeposit.getKaleidoOrder();
                String symbolPair = order.getConvertedSymbol();
                coin = symbolPair.substring(0,symbolPair.length() - 3);

                // add orders to map
                if (positionMap.containsKey(coin)) {
                    position = positionMap.get(coin);

                    // recalculate position
                    position = recalculateStaticPosition(position, order);

                } else {
                    position = new KaleidoPosition(
                            coin,
                            0.0d,
                            order.getPrice(),
                            0.0d,
                            0.0d,
                            0.0d,
                            order.getAmount(),
                            0.0d
                    );
                }

                // deposit
            } else {
                double absoluteAmount = orderOrDeposit.getKaleidoDeposits().getAmount();
                double amount = orderOrDeposit.getKaleidoDeposits().isDeposit() ? absoluteAmount : absoluteAmount * -1;
                coin = orderOrDeposit.getKaleidoDeposits().getSymbol();


                // add deposits to map
                if (positionMap.containsKey(coin)) {
                    position = positionMap.get(coin);
                    position.setAmount(position.getAmount() + amount);

                } else {
                    position = new KaleidoPosition(
                            coin,
                            0.0d,
                            0.0d,
                            0.0d,
                            0.0d,
                            0.0d,
                            amount,
                            0.0d
                    );
                }


            }

            positionMap.put(coin, position);
        }

        // live market calculations
        for(KaleidoLiveMarket liveMarket : liveMarkets){
            String coin = KaleidoFunctions.decodeMarketCoin(liveMarket);
            if(positionMap.containsKey(coin)){
                KaleidoPosition position = calculateLivePosition(positionMap.get(coin), liveMarket);
                positionMap.put(coin, position);
            }
        }

        return new ArrayList<>(positionMap.values());
    }

    private static KaleidoPosition recalculateStaticPosition(KaleidoPosition position, KaleidoOrder order) {
        double orderAmount = order.getAmount();
        double oldAmount = position.getAmount();
        double newAmount;
        double newAvgPrice = position.getAvgUnitPrice();
        double realizedGain = position.getRealizedGain();

        if (order.isBuy()) {
            newAmount = oldAmount + orderAmount;
            newAvgPrice = (position.getAvgUnitPrice() * oldAmount + order.getPrice() * orderAmount) / newAmount;
            // realizedGain unchanged
        } else {
            newAmount = oldAmount - orderAmount;
            // avg price unchanged
            realizedGain *= (order.getPrice() - position.getAvgUnitPrice());
        }

        position.setAmount(newAmount);
        position.setAvgUnitPrice(newAvgPrice);
        position.setRealizedGain(realizedGain);

        return position;
    }
    private static KaleidoPosition calculateLivePosition(KaleidoPosition position, KaleidoLiveMarket liveMarket){
        //100 150 = +50% (150-100)/(100)
        //100 50 = -50% (50-100)/100
        double percentChange =  (liveMarket.getPrice() - position.getAvgUnitPrice())/ position.getAvgUnitPrice() * 100;

        // livemarket $1 - avg $0.5 * units
        double unrealizedGain = (liveMarket.getPrice() - position.getAvgUnitPrice()) * position.getAmount();

        position.setPercentChange(percentChange);
        position.setUnrealizedGain(unrealizedGain);
        position.setCurrentUnitPrice(liveMarket.getPrice());

        return position;
    }

}
