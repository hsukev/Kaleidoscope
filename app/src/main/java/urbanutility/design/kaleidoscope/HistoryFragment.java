package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
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
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;
import urbanutility.design.kaleidoscope.view.OrdersAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recycler;

    private KaleidoViewModel kaleidoViewModel;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    KaleidoService kaleidoService;
    OrdersAdapter adapter;
    String TAG = HistoryFragment.class.getName();


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.binance.com");

        BittrexRequestInterceptor bittrexRequestInterceptor = new BittrexRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(bittrexRequestInterceptor);
        retrofitBuilder.client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        kaleidoService = retrofit.create(KaleidoService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_page,container,false);
        ButterKnife.bind(this,view);
        setUpViewModelAndObserver();
        setUpUI();

        return view;
    }

    private void setUpUI() {
        adapter = new OrdersAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setUpViewModelAndObserver(){
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);
        final Observer<List<BinanceOrder>> kaleidoObserver = new Observer<List<BinanceOrder>>() {
            @Override
            public void onChanged(@Nullable List<BinanceOrder> binanceOrders) {
                adapter.addOrder(binanceOrders);
            }
        };
        kaleidoViewModel.getOrderHistory().observe(HistoryFragment.this,kaleidoObserver);
    }

    public void addExchange(View view) {
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
                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
                    @Override
                    public void onNext(List<BinanceOrder> binanceOrders) {
                        if (binanceOrders.size() > 0) {
                            for (BinanceOrder order : binanceOrders) {
                                kaleidoViewModel.insertOrderHistory(order);
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
