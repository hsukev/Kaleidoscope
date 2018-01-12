package urbanutility.design.kaleidoscope.module.binance.client;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.BuildConfig;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceServerTime;
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

    public BinanceChainRequestor(HistoryFragment historyFragment){
        this.retrofitBuilder = historyFragment.retrofitBuilder;
        this.kaleidoViewModel = historyFragment.kaleidoViewModel;

        BinanceRequestInterceptor binanceRequestInterceptor = new BinanceRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(binanceRequestInterceptor);

        binanceService = retrofitBuilder.baseUrl("https://api.binance.com")
                .client(httpClient.build())
                .build()
                .create(BinanceService.class);
    }


    @Override
    public void requestAndInsert() {
        Single<BinanceServerTime> serverTimeSingle = binanceService.getServerTime();
        serverTimeSingle
                .subscribeOn(Schedulers.io())
                .zipWith(binanceService.getPriceTickers().subscribeOn(Schedulers.newThread()),
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
                        return binanceService.getAllOrders(binanceServerTimeBinancePriceTickerPair.second.getSymbol(), binanceServerTimeBinancePriceTickerPair.first.getServerTime(), 300000);
                    }
                })
                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
                    @Override
                    public void onNext(List<BinanceOrder> binanceOrders) {
                        if (binanceOrders.size() > 0) {
                            for (BinanceOrder order : binanceOrders) {
                                KaleidoOrder kaleidoOrder = new KaleidoOrder(order);
                                kaleidoViewModel.insertOrder(kaleidoOrder);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
