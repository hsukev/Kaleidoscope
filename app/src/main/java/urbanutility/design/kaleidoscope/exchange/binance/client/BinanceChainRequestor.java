package urbanutility.design.kaleidoscope.exchange.binance.client;

import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.BuildConfig;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.datatypes.BalanceType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceAccountInfo;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceBalance;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.BaseAlts;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 1/12/2018.
 */

public class BinanceChainRequestor implements ChainRequestor {
    private String TAG = getClass().getName();
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private KaleidoViewModel kaleidoViewModel;
    private Retrofit.Builder retrofitBuilder;
    private BinanceService binanceService;
    private GdaxService gdaxService;
    private String startTime;
    private String endTime;
    private long fifteenInMilli = 900000;


    public BinanceChainRequestor(HistoryFragment historyFragment) {
        this.retrofitBuilder = historyFragment.retrofitBuilder;
        this.kaleidoViewModel = historyFragment.kaleidoViewModel;

        BinanceRequestInterceptor binanceRequestInterceptor = new BinanceRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(binanceRequestInterceptor);

        binanceService = retrofitBuilder.baseUrl("https://api.binance.com")
                .client(httpClient.build())
                .build()
                .create(BinanceService.class);
        gdaxService = retrofitBuilder.baseUrl("https://api.gdax.com/")
                .build()
                .create(GdaxService.class);

    }


    @Override
    public void requestAndInsert() {
        getRawOrders();
        getRawBalance();

    }

    private void getRawOrders() {

        final Single<List<BinancePriceTicker>> binancePriceTicker = binanceService.getPriceTickers();
        binancePriceTicker
                .subscribeOn(Schedulers.single())
                .flattenAsObservable(new Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>>() {
                    @Override
                    public Iterable<BinancePriceTicker> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                        Log.d("order", binancePriceTickers.size() + "");
                        return binancePriceTickers;
                    }
                })
                .concatMap(new Function<BinancePriceTicker, ObservableSource<BinancePriceTicker>>() {
                    @Override
                    public ObservableSource<BinancePriceTicker> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                        return Observable.just(binancePriceTicker).delay(1000, TimeUnit.MILLISECONDS);
                    }
                })
                .flatMap(new Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>>() {
                    @Override
                    public ObservableSource<List<BinanceOrder>> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                        return binanceService.getAllOrders(binancePriceTicker.getSymbol(), System.currentTimeMillis() - 10000, 300000);
                    }
                })
                .filter(new Predicate<List<BinanceOrder>>() {
                    @Override
                    public boolean test(List<BinanceOrder> binanceOrders) throws Exception {
                        return binanceOrders.size() > 0;
                    }
                })
                .flatMapIterable(new Function<List<BinanceOrder>, Iterable<BinanceOrder>>() {
                    @Override
                    public Iterable<BinanceOrder> apply(List<BinanceOrder> binanceOrders) throws Exception {
                        return binanceOrders;
                    }
                })
                .concatMap(new Function<BinanceOrder, ObservableSource<BinanceOrder>>() {
                    @Override
                    public ObservableSource<BinanceOrder> apply(BinanceOrder binanceOrder) throws Exception {
                        return Observable.just(binanceOrder).delay(1000, TimeUnit.MILLISECONDS);
                    }
                })
                .filter(new Predicate<BinanceOrder>() {
                    @Override
                    public boolean test(BinanceOrder binanceOrder) throws Exception {
                        String status = binanceOrder.getStatus();
                        return status.equals("FILLED") || status.equals("PARTIALLY_FILLED ");
                    }
                })
                .map(timeMapper())
                .flatMap(conversionRatesFunction(), createKaleidoOrder())
                .subscribe(new DisposableObserver<KaleidoOrder>() {
                    @Override
                    public void onNext(KaleidoOrder kaleidoOrder) {
                        kaleidoViewModel.insertOrder(kaleidoOrder);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getRawBalance() {
        Single<BinanceAccountInfo> binanceBalanceSingle = binanceService.getAccountInfo(System.currentTimeMillis() - 10000, 300000);
        binanceBalanceSingle
                .subscribeOn(Schedulers.io())
                .map(new Function<BinanceAccountInfo, List<BinanceBalance>>() {
                    @Override
                    public List<BinanceBalance> apply(BinanceAccountInfo binanceAccountInfo) throws Exception {
                        return binanceAccountInfo.getBalances();
                    }
                })
                .flattenAsObservable(new Function<List<BinanceBalance>, Iterable<BinanceBalance>>() {
                    @Override
                    public Iterable<BinanceBalance> apply(List<BinanceBalance> binanceBalances) throws Exception {
                        return binanceBalances;
                    }
                })
                .filter(new Predicate<BinanceBalance>() {
                    @Override
                    public boolean test(BinanceBalance binanceBalance) throws Exception {
                        return Double.parseDouble(binanceBalance.getFree()) > 0;
                    }
                })
                .map(new Function<BinanceBalance, KaleidoBalance>() {
                    @Override
                    public KaleidoBalance apply(BinanceBalance binanceBalance) throws Exception {
                        BalanceType balanceType = new BalanceType(binanceBalance.getAsset(), "binance", Double.parseDouble(binanceBalance.getFree()));
                        return new KaleidoBalance(balanceType);
                    }
                })
                .subscribe(new DisposableObserver<KaleidoBalance>() {
                    @Override
                    public void onNext(KaleidoBalance kaleidoBalance) {
                        insertBalanceTable(kaleidoBalance);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("balance", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void insertBalanceTable(KaleidoBalance kaleidoBalances) {
        kaleidoViewModel.insertBalance(kaleidoBalances);
    }

    /*
    * Used for updating global start-end time variables
    * Observes: A BinanceOrder
    * Emits: Same BinanceOrder
    * */
    private Function<BinanceOrder, BinanceOrder> timeMapper(){
        return new Function<BinanceOrder, BinanceOrder>() {
            @Override
            public BinanceOrder apply(BinanceOrder binanceOrder) throws Exception {
                Long orderTime = Long.parseLong(binanceOrder.getTime());
                startTime = KaleidoFunctions.convertMilliISO8601(orderTime);
                endTime = KaleidoFunctions.convertMilliISO8601(orderTime+fifteenInMilli);
                return binanceOrder;
            }
        };
    }


    /*
    * Observes: A BinanceOrderf
    * Emits:  1. Same BinanceOrder
    *         2. Pair<gdaxBtcUsdRate, binanceAltBtcRate> combination depends on symbol
    * */
    private Function<BinanceOrder, ObservableSource<Pair<Double, Double>>> conversionRatesFunction(){
        return new Function<BinanceOrder, ObservableSource<Pair<Double, Double>>>() {
            @Override
            public ObservableSource<Pair<Double, Double>> apply(BinanceOrder binanceOrder) throws Exception {
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
