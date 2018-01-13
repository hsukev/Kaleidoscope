package urbanutility.design.kaleidoscope.module.binance.client;

import android.util.Log;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.BuildConfig;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinancePriceTicker;
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
        Single<List<BinancePriceTicker>> binancePriceTicker = binanceService.getPriceTickers();
        binancePriceTicker
                .subscribeOn(Schedulers.newThread())
                .flattenAsObservable(new Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>>() {
                    @Override
                    public Iterable<BinancePriceTicker> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                        return binancePriceTickers;
                    }
                })
                .flatMap(new Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>>() {
                    @Override
                    public ObservableSource<List<BinanceOrder>> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                        return binanceService.getAllOrders(binancePriceTicker.getSymbol(), System.currentTimeMillis(), 300000);
                    }
                })
                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
                    @Override
                    public void onNext(List<BinanceOrder> binanceOrders) {
                        if (binanceOrders.size() > 0) {
                            insertOrderTable(binanceOrders);
                            updateBalanceTable(binanceOrders);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        //calculate average and insert to db
                    }
                });
    }

    private void insertOrderTable(List<BinanceOrder> binanceOrders){
        for (BinanceOrder order : binanceOrders) {
            KaleidoOrder kaleidoOrder = new KaleidoOrder(order);
            kaleidoViewModel.insertOrder(kaleidoOrder);
        }
    }
    private void updateBalanceTable(List<BinanceOrder> binanceOrders){
        KaleidoBalance balance = kaleidoViewModel.getBalance(binanceOrders.get(0).getSymbol());

        for (BinanceOrder order : binanceOrders){
            //math: calculate average
            //update balance
        }

        kaleidoViewModel.insertBalance(balance);
    }



}
