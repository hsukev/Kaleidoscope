package urbanutility.design.kaleidoscope;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.client.BittrexRequestInterceptor;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.BinanceOrder;
import urbanutility.design.kaleidoscope.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.model.BinanceServerTime;

/**
 * Created by jerye on 1/4/2018.
 * 1 call with single list response -> call for each item of list
 * https://github.com/ReactiveX/RxJava/issues/1939
 * https://stackoverflow.com/questions/28035090/rxjava-fetch-every-item-on-the-list SOLUTION
 * <p>
 * CONVERSION CHART
 * http://www.vogella.com/tutorials/RxJava/article.html
 */

public class MainActivity extends AppCompatActivity {
    public List<List<BinanceOrder>> transactionHistory = new ArrayList<>();
    String LOG = "MainActivity";
    String[] tradeSymbols = {"ETHBTC",
            "LTCBTC",
            "NEOBTC",
            "NEOBTC",
            "NEOBTC",
            "BCCBTC",
            "MCOBTC",
            "EVXBTC",
            "REQBTC",
            "REQBTC",
            "REQBTC",
            "REQBTC",
            "REQBTC",
            "REQBTC",
            "REQBTC",
            "REQETH",
            "REQETH",
            "REQETH",
            "TRXBTC",
            "TRXBTC",
            "POWRBTC",
            "POWRBTC",
            "XRPBTC",
            "XRPBTC",
            "XRPBTC",
            "XRPBTC",
            "STORJBTC",
            "STORJBTC",
            "STORJBTC",
            "VENBTC",
            "KMDBTC",
            "XMRBTC",
            "BATBTC",
            "QSPBTC",
            "QSPBTC",
            "QSPBTC",
            "QSPBTC",
            "QSPBTC",
            "QSPETH",
            "QSPETH",
            "QSPETH",
            "QSPETH",
            "XLMBTC",
            "XLMBTC",
            "WABIBTC",
            "WABIBTC",
            "WABIBTC",
            "WABIBTC",
            "WABIETH",
            "ELFBTC",
            "ELFBTC",
            "AIONBTC",
            "AIONBTC",
            "AIONBTC",
            "WINGSBTC",
            "APPCBTC",
            "APPCBTC"};
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.binance.com");

        BittrexRequestInterceptor bittrexRequestInterceptor = new BittrexRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(bittrexRequestInterceptor);
        retrofitBuilder.client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        final KaleidoService kaleidoService = retrofit.create(KaleidoService.class);

//        Observable.just(tradeSymbols)
//                .subscribeOn(Schedulers.io())
//                .flatMapIterable(new Function<String[], Iterable<String>>() {
//                    @Override
//                    public Iterable<String> apply(String[] strings) throws Exception {
//                        return Arrays.asList(strings);
//                    }
//                })
//                .flatMap(new Function<String, ObservableSource<List<BinanceOrder>>>() {
//                    @Override
//                    public ObservableSource<List<BinanceOrder>> apply(String s) throws Exception {
//                        return kaleidoService.getAllOrders(s, 1515394177677L, 300000);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
//                    @Override
//                    public void onNext(List<BinanceOrder> binanceOrders) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                })

        Single<BinanceServerTime> serverTimeSingle = kaleidoService.getServerTime();
        serverTimeSingle
                .subscribeOn(Schedulers.io())
                .zipWith(kaleidoService.getPriceTickers().subscribeOn(Schedulers.newThread()),
                        new BiFunction<BinanceServerTime, List<BinancePriceTicker>, Pair<BinanceServerTime, List<BinancePriceTicker>>>() {
                            @Override
                            public Pair<BinanceServerTime, List<BinancePriceTicker>> apply(BinanceServerTime binanceServerTime, List<BinancePriceTicker> binancePriceTickers) throws Exception {
                                return new Pair<>(binanceServerTime, binancePriceTickers);
                            }
                        })
                .map(new Function<Pair<BinanceServerTime, List<BinancePriceTicker>>, List<Pair<BinanceServerTime, BinancePriceTicker>>>() {
                    @Override
                    public List<Pair<BinanceServerTime, BinancePriceTicker>> apply(Pair<BinanceServerTime, List<BinancePriceTicker>> binanceServerTimeListPair) throws Exception {
                        List<Pair<BinanceServerTime, BinancePriceTicker>> listOfPairs = new ArrayList<>();
                        for (BinancePriceTicker binancePriceTicker : binanceServerTimeListPair.second) {
                            listOfPairs.add(new Pair<>(binanceServerTimeListPair.first, binancePriceTicker));
                        }
                        return listOfPairs;
                    }
                })
                .flattenAsObservable(new Function<List<Pair<BinanceServerTime, BinancePriceTicker>>, Iterable<Pair<BinanceServerTime, BinancePriceTicker>>>() {
                    @Override
                    public Iterable<Pair<BinanceServerTime, BinancePriceTicker>> apply(List<Pair<BinanceServerTime, BinancePriceTicker>> pairs) throws Exception {
                        return pairs;
                    }
                })

                .flatMap(new Function<Pair<BinanceServerTime, BinancePriceTicker>, ObservableSource<List<BinanceOrder>>>() {
                    @Override
                    public ObservableSource<List<BinanceOrder>> apply(Pair<BinanceServerTime, BinancePriceTicker> binanceServerTimeBinancePriceTickerPair) throws Exception {
                        return kaleidoService.getAllOrders(binanceServerTimeBinancePriceTickerPair.second.getSymbol(), binanceServerTimeBinancePriceTickerPair.first.getServerTime(), 300000);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
                    @Override
                    public void onNext(List<BinanceOrder> binanceOrders) {
                        if (binanceOrders.size() > 0) {
                            transactionHistory.add(binanceOrders);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        for (List<BinanceOrder> list : transactionHistory) {
                            for (BinanceOrder order : list) {
                                Log.d(LOG, order.getSymbol() + ": " + order.getExecutedQty());
                            }
                        }
                    }
                });


    }
}
