package urbanutility.design.kaleidoscope.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoPosition;
import urbanutility.design.kaleidoscope.model.OrderOrDeposit;

/**
 * Created by jerye on 3/7/2018.
 */

public class KaleidoCalculator2 {

    public static Iterable<KaleidoPosition> CalculatePositions(
            List<KaleidoOrder> orders,
            List<KaleidoDeposits> deposits,
            List<LiveMarketType> liveMarkets
    ) {

        List<OrderOrDeposit> orderOrDepositsList = new ArrayList<>();
        Map<String, KaleidoPosition> positionMap = new HashMap<>();

        // add all orders and deposits into mixed type list
        for(KaleidoOrder order: orders){
            orderOrDepositsList.add(new OrderOrDeposit(order));
        }
        for(KaleidoDeposits deposit: deposits){
            orderOrDepositsList.add(new OrderOrDeposit(deposit));
        }


        // comparator that sorts mix list of orders and deposits
        Comparator<OrderOrDeposit> comparator = new Comparator<OrderOrDeposit>() {
            @Override
            public int compare(OrderOrDeposit t1, OrderOrDeposit t2) {
                if(t1.isOrder() && t2.isOrder()){
                    return t1.getKaleidoOrder().getOrdertype().time.compareTo(t2.getKaleidoOrder().getOrdertype().time);
                }else if(!t1.isOrder() && !t2.isOrder()){
                    return t1.getKaleidoDeposits().getTime().compareTo(t2.getKaleidoDeposits().getTime());
                }else if(t1.isOrder() && !t2.isOrder()){
                    return t1.getKaleidoOrder().getOrdertype().time.compareTo(t2.getKaleidoDeposits().getTime());
                }else{
                    return t1.getKaleidoDeposits().getTime().compareTo(t2.getKaleidoOrder().getOrdertype().time);
                }
            }
        };

        // sort using comparator
        Collections.sort(orderOrDepositsList, comparator);

        

        return null;

    }

}
