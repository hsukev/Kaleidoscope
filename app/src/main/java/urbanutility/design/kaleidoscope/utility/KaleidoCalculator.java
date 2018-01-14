package urbanutility.design.kaleidoscope.utility;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import urbanutility.design.kaleidoscope.datatypes.OrderType;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoRawBalance;
import urbanutility.design.kaleidoscope.datatypes.BalanceType;

/**
 * Created by Jason on 1/13/2018.
 */

public class KaleidoCalculator implements Comparator<OrderType> {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<BalanceType> CalculatePosition (
            ArrayList<KaleidoRawBalance> inputBalance,
            ArrayList<KaleidoOrder> inputOrder
    ) {
        ArrayList<KaleidoRawBalance> balanceList = inputBalance;
        ArrayList<OrderType> orderList;

        /* Put orders into Hashmap by symbol */
        Map<String, ArrayList<OrderType>> orderDict = new HashMap<String, ArrayList<OrderType>>();
        inputOrder.forEach(entry -> {
            OrderType orderType = entry.getOrdertype();
            if (orderDict.containsKey(orderType.symbol)) {
                orderDict.get(orderType.symbol).add(orderType);
            } else {
                orderDict.put(orderType.symbol, new ArrayList<OrderType>());
            }
        });

        orderDict.forEach((key, value) -> {
            ArrayList<OrderType> sorted_list = SortListByTime(value);
            ArrayList<OrderType> buyList = sorted_list
                    .stream()
                    .filter(entry -> entry.type.equalsIgnoreCase("buy"))
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<OrderType> sellList = sorted_list
                    .stream()
                    .filter(entry -> entry.type.equalsIgnoreCase("sell"))
                    .collect(Collectors.toCollection(ArrayList::new));
            long totalBuyAmount = buyList.stream().mapToLong(entry -> entry.amount).sum();
            long totalSellAmount = sellList.stream().mapToLong(entry -> entry.amount).sum();

            if (totalBuyAmount > totalSellAmount) {
                /* Calculate realized gain */
                /* Calculate unrealized gain */
            } else {

            }
        });
    }
    private CalculateRealizedGain () {

    }

    private CalculateUnrealizedGain () {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<OrderType> SortListByTime (ArrayList<OrderType> in) {
        ArrayList<OrderType> list = new ArrayList<OrderType>(in);
        list.sort((o1, o2) -> {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .parse(o1.time)
                        .compareTo(new SimpleDateFormat("hh:mm a")
                                .parse(o2.time));
            } catch (ParseException e) {
                return 0;
            }
        });
        return list;
    }
}
