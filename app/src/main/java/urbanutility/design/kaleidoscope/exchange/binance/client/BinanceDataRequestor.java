package urbanutility.design.kaleidoscope.exchange.binance.client;

import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.client.KaleidoClients;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.BaseAlts;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 2/16/2018.
 */

public class BinanceDataRequestor implements DataRequestor {
    BinanceService binanceService;
    GdaxService gdaxService;

    public BinanceDataRequestor(KaleidoClients kaleidoClients) {
        this.binanceService = kaleidoClients.getBinanceService();
        this.gdaxService = kaleidoClients.getGdaxService();
    }

    @Override
    public Single<List<LiveMarketType>> requestLiveMarkets() {
        return binanceService.getPriceTickers().subscribeOn(Schedulers.io())
                .map(binanceLiveMarketMapper());
    }

    @Override
    public Single<List<KaleidoBalance>> requestBalances() {
        return null;
    }

    @Override
    public Single<List<KaleidoOrder>> requestOrders() {
        return binanceService.getPriceTickers()
                .subscribeOn(Schedulers.single())
                .flattenAsObservable(iterableTickerFunction())
                .concatMap(tickerDelayFunction())
                .flatMap(tickerToOrderFlatMap())
                .filter(orderSizeFilter())
                .flatMapIterable(flattenOrderList())
                .concatMap(orderDelayFunction())
                .filter(orderTypeFilter())
                .flatMap(conversionRatesFunction(),createKaleidoOrder())
                .toList();
    }

    @Override
    public Single<List<KaleidoDeposits>> requestDeposits() {
        return null;
    }

    // requestLiveMarkets functions
    private Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>> iterableTickerFunction(){
        return new Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>>() {
            @Override
            public Iterable<BinancePriceTicker> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                Log.d("order", binancePriceTickers.size() + "");
                return binancePriceTickers;
            }
        };
    }

    // requestOrders functions
    private Function<List<BinancePriceTicker>, List<LiveMarketType>> binanceLiveMarketMapper() {
        return new Function<List<BinancePriceTicker>, List<LiveMarketType>>() {
            @Override
            public List<LiveMarketType> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                List<LiveMarketType> liveMarketTypes = new ArrayList<>();
                for (BinancePriceTicker ticker : binancePriceTickers) {
                    LiveMarketType liveMarketType = new LiveMarketType();
                    liveMarketType.exchange = "binance";
                    liveMarketType.symbol = ticker.getSymbol();
                    liveMarketType.price = Double.parseDouble(ticker.getPrice());
                    liveMarketTypes.add(liveMarketType);
                }
                return liveMarketTypes;
            }
        };

    }
    private Function<BinancePriceTicker, ObservableSource<BinancePriceTicker>> tickerDelayFunction(){
        return new Function<BinancePriceTicker, ObservableSource<BinancePriceTicker>>() {
            @Override
            public ObservableSource<BinancePriceTicker> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                return Observable.just(binancePriceTicker).delay(400, TimeUnit.MILLISECONDS);
            }
        };
    }
    private Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>> tickerToOrderFlatMap(){
        return new Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>>() {
            @Override
            public ObservableSource<List<BinanceOrder>> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                return binanceService.getAllOrders(binancePriceTicker.getSymbol(), System.currentTimeMillis() - 10000, 300000);
            }
        };
    }
    private Predicate<List<BinanceOrder>> orderSizeFilter(){
        return new Predicate<List<BinanceOrder>>() {
            @Override
            public boolean test(List<BinanceOrder> binanceOrders) throws Exception {
                return binanceOrders.size() > 0;
            }
        };
    }
    private Function<List<BinanceOrder>, Iterable<BinanceOrder>> flattenOrderList(){
        return new Function<List<BinanceOrder>, Iterable<BinanceOrder>>() {
            @Override
            public Iterable<BinanceOrder> apply(List<BinanceOrder> binanceOrders) throws Exception {
                return binanceOrders;
            }
        };
    }
    private Function<BinanceOrder, ObservableSource<BinanceOrder>> orderDelayFunction(){
        return new Function<BinanceOrder, ObservableSource<BinanceOrder>>() {
            @Override
            public ObservableSource<BinanceOrder> apply(BinanceOrder binanceOrder) throws Exception {
                return Observable.just(binanceOrder).delay(400, TimeUnit.MILLISECONDS);
            }
        };
    }
    private Predicate<BinanceOrder> orderTypeFilter(){
        return new Predicate<BinanceOrder>() {
            @Override
            public boolean test(BinanceOrder binanceOrder) throws Exception {
                String status = binanceOrder.getStatus();
                return status.equals("FILLED") || status.equals("PARTIALLY_FILLED ");
            }
        };
    }
    private Function<BinanceOrder, ObservableSource<Pair<Double, Double>>> conversionRatesFunction(){
        return new Function<BinanceOrder, ObservableSource<Pair<Double, Double>>>() {
            @Override
            public ObservableSource<Pair<Double, Double>> apply(BinanceOrder binanceOrder) throws Exception {
                Long orderTime = Long.parseLong(binanceOrder.getTime());
                String startTime = KaleidoFunctions.convertMilliISO8601(orderTime);
                String endTime = KaleidoFunctions.convertMilliISO8601(orderTime+900000);
                String rawSymbol = binanceOrder.getSymbol();
                String guess = rawSymbol.substring(rawSymbol.length()-4);
                if(rawSymbol.equals("BTCUSDT")) {
                    return Observable.just(new Pair<>(1d,1d));
                }else if(rawSymbol.contains("USDT")){
                    String altBtcSymbol = rawSymbol.substring(0,rawSymbol.length()-4) + "BTC";
                    return binanceService.getHistoricPrice(altBtcSymbol, 1, "1m", binanceOrder.getTime())
                            .map(mapCoinBtcInverseRate());
                }else if(rawSymbol.contains("BTC")){
                    return gdaxService.getHistoricBtc2Usd(startTime, endTime,60)
                            .map(mapGdaxBtcUsdRate());
                }else{
                    String altBtcSymbol = "";
                    for(String baseAlt : BaseAlts.binanceBaseAlts){
                        if(guess.contains(baseAlt)) altBtcSymbol = baseAlt + "BTC";
                    }
                    return gdaxService.getHistoricBtc2Usd(startTime,endTime,60)
                            .zipWith(binanceService.getHistoricPrice(altBtcSymbol, 1, "1m", binanceOrder.getTime())
                                            .subscribeOn(Schedulers.io()),
                                    bifuncGdaxBinanceRates());
                }
            }
        };
    }
    /*
* Observes: Emission from conversionRatesFunction()
* Emits: new KaleidoOrder
* */
    private BiFunction<BinanceOrder, Pair<Double, Double>, KaleidoOrder> createKaleidoOrder(){
        return new BiFunction<BinanceOrder, Pair<Double, Double>, KaleidoOrder>() {
            @Override
            public KaleidoOrder apply(BinanceOrder binanceOrder, Pair<Double, Double> ratesPair) throws Exception {
                return new KaleidoOrder(binanceOrder, ratesPair.first, ratesPair.second);
            }
        };
    }

    private Function<JsonElement, Pair<Double, Double>> mapCoinBtcInverseRate(){
        return new Function<JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement binanceCandle) throws Exception {
                double inverseRate =binanceCandle.getAsJsonArray().get(0).getAsJsonArray().get(1).getAsDouble();
                return new Pair<>(1d, inverseRate);
            }
        };
    }

    private Function<JsonElement, Pair<Double, Double>> mapGdaxBtcUsdRate(){
        return new Function<JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement jsonElement) throws Exception {
                return new Pair<>(jsonElement.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble(), 1d);
            }
        };
    }

    private BiFunction<JsonElement, JsonElement, Pair<Double, Double>> bifuncGdaxBinanceRates(){
        return new BiFunction<JsonElement, JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement gdaxCandle, JsonElement binanceCandle) throws Exception {
                double gdaxBtcUsdRate = gdaxCandle.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble();
                double coinBtcRate = binanceCandle.getAsJsonArray().get(0).getAsJsonArray().get(1).getAsDouble();
                return new Pair<>(gdaxBtcUsdRate, coinBtcRate);
            }
        };
    }
}