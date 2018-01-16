package urbanutility.design.kaleidoscope.exchange.binance.client;

import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
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
    private Date d,dEnd;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
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
                .subscribeOn(Schedulers.io())
                .flattenAsObservable(new Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>>() {
                    @Override
                    public Iterable<BinancePriceTicker> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                        Log.d("order", binancePriceTickers.size()+"");
                        return binancePriceTickers;
                    }
                })
                .flatMap(new Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>>() {
                    @Override
                    public ObservableSource<List<BinanceOrder>> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                        return binanceService.getAllOrders(binancePriceTicker.getSymbol(), System.currentTimeMillis()-10000, 300000);
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
                .map(new Function<BinanceOrder, BinanceOrder>() {
                    @Override
                    public BinanceOrder apply(BinanceOrder binanceOrder) throws Exception {
                        Long orderTime = Long.parseLong(binanceOrder.getTime());
                        d = new Date(orderTime);
                        dEnd = new Date(orderTime+fifteenInMilli);
                        startTime = sdf.format(d);
                        endTime = sdf.format(dEnd);
                        return binanceOrder;
                    }
                })
                .zipWith(gdaxService.getHistoricBtc2Usd(startTime,endTime,60 ).subscribeOn(Schedulers.newThread()),
                        new BiFunction<BinanceOrder, JsonElement, Pair<BinanceOrder, Double>>() {
                            @Override
                            public Pair<BinanceOrder, Double> apply(BinanceOrder binanceOrder, JsonElement jsonElement) throws Exception {
                                Log.d(TAG, jsonElement.getAsJsonArray().get(0).toString());
                                return new Pair<>(binanceOrder, jsonElement.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble());
                            }
                        })
                .subscribe(new DisposableObserver<Pair<BinanceOrder, Double>>() {
                    @Override
                    public void onNext(Pair<BinanceOrder, Double> binanceOrderDoublePair) {
                        insertOrderTable(binanceOrderDoublePair);
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
        Single<BinanceAccountInfo> binanceBalanceSingle = binanceService.getAccountInfo(System.currentTimeMillis());
        binanceBalanceSingle
                .subscribeOn(Schedulers.newThread())
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
                        return !binanceBalance.getFree().equals("0.0");
                    }
                })
                .map(new Function<BinanceBalance, KaleidoBalance>() {
                    @Override
                    public KaleidoBalance apply(BinanceBalance binanceBalance) throws Exception {
                        BalanceType balanceType = new BalanceType(binanceBalance.getAsset(), "binance", Double.parseDouble(binanceBalance.getFree()));
                        return new KaleidoBalance(String.valueOf(System.currentTimeMillis()),balanceType);
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

    private void insertOrderTable(Pair<BinanceOrder, Double> orderPair) {
        KaleidoOrder kaleidoOrder = new KaleidoOrder(orderPair.first, orderPair.second);
        kaleidoViewModel.insertOrder(kaleidoOrder);
    }

    private void insertBalanceTable(KaleidoBalance kaleidoBalances) {
        kaleidoViewModel.insertBalance(kaleidoBalances);
    }


}
