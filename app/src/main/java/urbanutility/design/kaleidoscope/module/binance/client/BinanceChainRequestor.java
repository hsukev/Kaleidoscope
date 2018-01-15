package urbanutility.design.kaleidoscope.module.binance.client;

import com.google.gson.JsonElement;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.BuildConfig;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceAccountInfo;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceBalance;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.module.gdax.client.GdaxService;
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

    public BinanceChainRequestor(HistoryFragment historyFragment){
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


//                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
//                    @Override
//                    public void onNext(List<BinanceOrder> binanceOrders) {
//                        if (binanceOrders.size() > 0) {
//                            insertOrderTable(binanceOrders);
//                            updateBalanceTable(binanceOrders);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        //calculate average and insert to db
//                    }
//                });
    }

    private void getRawOrders(){
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
                .filter(new Predicate<List<BinanceOrder>>() {
                    @Override
                    public boolean test(List<BinanceOrder> binanceOrders) throws Exception {
                        return binanceOrders.size()>0;
                    }
                })
                .flatMapIterable(new Function<List<BinanceOrder>, Iterable<BinanceOrder>>() {
                    @Override
                    public Iterable<BinanceOrder> apply(List<BinanceOrder> binanceOrders) throws Exception {
                        return binanceOrders;
                    }
                })
                .flatMap(new Function<BinanceOrder, ObservableSource<JsonElement>>() {
                    @Override
                    public ObservableSource<JsonElement> apply(BinanceOrder binanceOrder) throws Exception {
                        kaleidoViewModel.insertOrder();
                        return gdaxService.getHistoricBtc2Usd();
                    }
                })
                .subscribe();

    }

    private void getRawBalance(){
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
                .map(new Function<BinanceBalance, KaleidoBalance>() {
                    @Override
                    public KaleidoBalance apply(BinanceBalance binanceBalance) throws Exception {
                        KaleidoBalance kaleidoBalance = null;
                        return kaleidoBalance;
                    }
                })
                .subscribe(new DisposableObserver<KaleidoBalance>() {
                    @Override
                    public void onNext(KaleidoBalance kaleidoBalance) {
                        insertBalanceTable(kaleidoBalance);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void insertOrderTable(List<BinanceOrder> binanceOrders){
        for (BinanceOrder order : binanceOrders) {
            KaleidoOrder kaleidoOrder = new KaleidoOrder(order);
            kaleidoViewModel.insertOrder(kaleidoOrder);
        }
    }
    private void insertBalanceTable(KaleidoBalance kaleidoBalances){
        kaleidoViewModel.insertBalance(kaleidoBalances);
    }



}
