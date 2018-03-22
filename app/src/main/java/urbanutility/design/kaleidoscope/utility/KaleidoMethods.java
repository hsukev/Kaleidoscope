//package urbanutility.design.kaleidoscope.utility;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import urbanutility.design.kaleidoscope.datatypes.BaseCurrencyType;
//import urbanutility.design.kaleidoscope.datatypes.OrderType;
//import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
//
///**
// * Created by Jason on 1/14/2018.
// */
//
//public class KaleidoMethods {
//
//    protected static double Sum(List<OrderType> list, String type) {
//        double sum = 0d;
//        for (OrderType entry: list) {
//            if (type.equals("Amount")) sum += entry.amount;
//            if (type.equals("Fee")) sum += entry.txFee;
//            if (type.equals("Total")) sum += entry.amount * entry.price;
//        }
//        return sum;
//    }
//
//    protected static List<OrderType> FilterOrderType(List<OrderType> list, String symbol) {
//        List<OrderType> outputList = new ArrayList<>();
//        for (OrderType entry: list) {
//            if (entry.side.equalsIgnoreCase(symbol)) {
//                outputList.add(entry);
//            }
//        }
//        return outputList;
//    }
//    protected static BaseCurrencyType FilterBaseCurrency(List<BaseCurrencyType> list, String symbol) {
//        BaseCurrencyType outputList = new BaseCurrencyType();
//        for (BaseCurrencyType entry: list) {
//            if (entry.type.equalsIgnoreCase(symbol)) {
//                outputList = entry;
//            }
//        }
//        return outputList;
//    }
//    protected static List<KaleidoLiveMarket> FilterLiveMarketBySymbol(List<KaleidoLiveMarket> list, String symbol) {
//        List<KaleidoLiveMarket> outputList = new ArrayList<>();
//        for (KaleidoLiveMarket entry: list) {
//            if (entry.symbol.equalsIgnoreCase(symbol)) {
//                outputList.add(entry);
//            }
//        }
//        return outputList;
//    }
//    protected static KaleidoLiveMarket FilterLiveMarketByExchange(List<KaleidoLiveMarket> list, String exchange) {
//        KaleidoLiveMarket outputList = new KaleidoLiveMarket();
//        for (KaleidoLiveMarket entry: list) {
//            if (entry.exchange.equals(exchange)) {
//                outputList = entry;
//            }
//        }
//        return outputList;
//    }
//    protected static KaleidoLiveMarket FilterLiveMarket(List<KaleidoLiveMarket> list, String exchange, String symbol) {
//        KaleidoLiveMarket outputList = new KaleidoLiveMarket();
//        for (KaleidoLiveMarket entry: list) {
//            if (entry.symbol.equalsIgnoreCase(symbol) && entry.exchange.equals(exchange)) {
//                outputList = entry;
//            }
//        }
//        return outputList;
//    }
//}
