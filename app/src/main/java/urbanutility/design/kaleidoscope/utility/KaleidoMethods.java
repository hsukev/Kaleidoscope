package urbanutility.design.kaleidoscope.utility;

import java.util.ArrayList;
import java.util.List;

import urbanutility.design.kaleidoscope.datatypes.BaseCurrencyType;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.datatypes.OrderType;

/**
 * Created by Jason on 1/14/2018.
 */

public class KaleidoMethods {

    protected static long Sum(List<OrderType> list, String type) {
        long sum = 0L;
        for (OrderType entry: list) {
            if (type.equals("Amount")) sum += entry.amount;
            if (type.equals("Fee")) sum += entry.txFee;
            if (type.equals("Total")) sum += entry.amount * entry.price;
        }
        return sum;
    }

    protected static List<OrderType> FilterOrderType(List<OrderType> list, String symbol) {
        List<OrderType> outputList = new ArrayList<>();
        for (OrderType entry: list) {
            if (entry.side.equalsIgnoreCase(symbol)) {
                outputList.add(entry);
            }
        }
        return outputList;
    }
    protected static BaseCurrencyType FilterBaseCurrency(List<BaseCurrencyType> list, String symbol) {
        BaseCurrencyType outputList = new BaseCurrencyType();
        for (BaseCurrencyType entry: list) {
            if (entry.type.equalsIgnoreCase(symbol)) {
                outputList = entry;
            }
        }
        return outputList;
    }
    protected static LiveMarketType FilterLiveMarket(List<LiveMarketType> list, String symbol) {
        LiveMarketType outputList = new LiveMarketType();
        for (LiveMarketType entry: list) {
            if (entry.symbol.equalsIgnoreCase(symbol)) {
                outputList = entry;
            }
        }
        return outputList;
    }

}
