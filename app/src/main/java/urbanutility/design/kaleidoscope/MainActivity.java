package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.stetho.Stetho;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.client.BittrexRequestInterceptor;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.model.BinanceOrder;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;
import urbanutility.design.kaleidoscope.view.OrdersAdapter;

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
    @BindView(R.id.recyclerView)
    RecyclerView recycler;
    OrdersAdapter adapter;

    String TAG = "MainActivity";

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private KaleidoViewModel kaleidoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        final KaleidoDatabase db = Room.databaseBuilder(this, KaleidoDatabase.class,"binance_orders").build();

        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

        adapter = new OrdersAdapter(this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


        final Observer<List<BinanceOrder>> kaleidoObserver = new Observer<List<BinanceOrder>>() {
            @Override
            public void onChanged(@Nullable List<BinanceOrder> binanceOrders) {
                adapter.addOrder(binanceOrders);
            }
        };

        kaleidoViewModel.getOrderHistory().observe(MainActivity.this,kaleidoObserver);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.binance.com");

        BittrexRequestInterceptor bittrexRequestInterceptor = new BittrexRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(bittrexRequestInterceptor);
        retrofitBuilder.client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        final KaleidoService kaleidoService = retrofit.create(KaleidoService.class);


//        Single<BinanceServerTime> serverTimeSingle = kaleidoService.getServerTime();
//        serverTimeSingle
//                .subscribeOn(Schedulers.io())
//                .zipWith(kaleidoService.getPriceTickers().subscribeOn(Schedulers.newThread()),
//                        new BiFunction<BinanceServerTime, List<BinancePriceTicker>, Pair<BinanceServerTime, List<BinancePriceTicker>>>() {
//                            @Override
//                            public Pair<BinanceServerTime, List<BinancePriceTicker>> apply(BinanceServerTime binanceServerTime, List<BinancePriceTicker> binancePriceTickers) throws Exception {
//                                return new Pair<>(binanceServerTime, binancePriceTickers);
//                            }
//                        })
//                .map(new Function<Pair<BinanceServerTime, List<BinancePriceTicker>>, List<Pair<BinanceServerTime, BinancePriceTicker>>>() {
//                    @Override
//                    public List<Pair<BinanceServerTime, BinancePriceTicker>> apply(Pair<BinanceServerTime, List<BinancePriceTicker>> binanceServerTimeListPair) throws Exception {
//                        List<Pair<BinanceServerTime, BinancePriceTicker>> listOfPairs = new ArrayList<>();
//                        for (BinancePriceTicker binancePriceTicker : binanceServerTimeListPair.second) {
//                            listOfPairs.add(new Pair<>(binanceServerTimeListPair.first, binancePriceTicker));
//                        }
//                        return listOfPairs;
//                    }
//                })
//                .flattenAsObservable(new Function<List<Pair<BinanceServerTime, BinancePriceTicker>>, Iterable<Pair<BinanceServerTime, BinancePriceTicker>>>() {
//                    @Override
//                    public Iterable<Pair<BinanceServerTime, BinancePriceTicker>> apply(List<Pair<BinanceServerTime, BinancePriceTicker>> pairs) throws Exception {
//                        return pairs;
//                    }
//                })
//
//                .flatMap(new Function<Pair<BinanceServerTime, BinancePriceTicker>, ObservableSource<List<BinanceOrder>>>() {
//                    @Override
//                    public ObservableSource<List<BinanceOrder>> apply(Pair<BinanceServerTime, BinancePriceTicker> binanceServerTimeBinancePriceTickerPair) throws Exception {
//                        return kaleidoService.getAllOrders(binanceServerTimeBinancePriceTickerPair.second.getSymbol(), binanceServerTimeBinancePriceTickerPair.first.getServerTime(), 300000);
//                    }
//                })
//                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
//                    @Override
//                    public void onNext(List<BinanceOrder> binanceOrders) {
//                        if (binanceOrders.size() > 0) {
//                            for(BinanceOrder order:binanceOrders){
//                                db.kaleidoDao().insertBinanceOrder(order);
//                            }
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
//                        for (List<BinanceOrder> list : transactionHistory) {
//                            for (BinanceOrder order : list) {
//                                Log.d(TAG, order.getSymbol() + ": " + order.getExecutedQty());
//                            }
//                        }
//                    }
//                });


    }

    private void setUpView(){

    }
}
