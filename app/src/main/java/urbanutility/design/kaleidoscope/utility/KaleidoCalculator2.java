package urbanutility.design.kaleidoscope.utility;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoPosition;

/**
 * Created by jerye on 3/7/2018.
 */

public class KaleidoCalculator2 {

    public static Iterable<KaleidoPosition> CalculatePositions(
            List<KaleidoOrder> orders,
            List<KaleidoDeposits> deposits,
            List<LiveMarketType> liveMarkets
    ) {

        Map<String, KaleidoPosition> positionMap = new HashMap<>();

        new Comparator<Object>() {
            @Override
            public int compare(Object o, Object t1) {
//                String date = o instanceof KaleidoOrder ? ((KaleidoOrder) o).getOrdertype().time : ((KaleidoDeposits) o);

                return 0;
            }
        };

        for (KaleidoOrder order : orders) {
            String key = order.getOrdertype().convertedSymbol;

            //
            KaleidoPosition position;
            if (positionMap.containsKey(key)) {
                position = positionMap.get(key);


            } else {

            }
        }


        return null;

    }

}
