package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoBaseCurrency;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.view.CurrentAdapter;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPriceFragment extends Fragment {
    private String TAG = "current";
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.current_recycler)
    RecyclerView recycler;
    float[] totalHistory = {1000.00000f, 1254.1231f, 1134.8547f, 1549.987432f, 5475.102f, 5252.545f, 6666.666f, 6666.6604f, 6666.6123f, 12321.10f, 8080.51444f};

    private KaleidoViewModel kaleidoViewModel;
    private Retrofit.Builder retrofitBuilder;
    private BinanceService binanceService;
    private CurrentAdapter adapter;

    public CurrentPriceFragment() {
        // Required empty public constructor
    }

    public static CurrentPriceFragment newInstance() {
        Bundle args = new Bundle();
        CurrentPriceFragment fragment = new CurrentPriceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_price_page, container, false);
        ButterKnife.bind(this, view);
        setUpUI();
        setUpLineChart();
        setUpRetrofitBuilder();
        setUpViewModelAndObserver();




        Observer<List<LiveMarketType>> marketObserver = new Observer<List<LiveMarketType>>() {
            @Override
            public void onChanged(@Nullable List<LiveMarketType> liveMarketTypes) {
                kaleidoViewModel.setTripletMarkets(liveMarketTypes);
            }
        };

        Observer<List<KaleidoOrder>> orderObserver = new Observer<List<KaleidoOrder>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoOrder> kaleidoOrders) {
                kaleidoViewModel.setTripletOrders(kaleidoOrders);
            }
        };

        final Observer<List<KaleidoBalance>> balanceObserver = new Observer<List<KaleidoBalance>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoBalance> kaleidoBalances) {
                kaleidoViewModel.setTripletBalances(kaleidoBalances);
            }
        };

        Observer<List<KaleidoBaseCurrency>> tripletObserver = new Observer<List<KaleidoBaseCurrency>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoBaseCurrency> baseCurrencies) {
                Log.d(TAG, baseCurrencies.get(1).getBaseCurrencyType().positions.size()+"");

                if(baseCurrencies.get(1).getBaseCurrencyType().positions.size()>0) adapter.refresh(baseCurrencies);
            }
        };

        Observer mediatorObserver = new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                Log.d(TAG, "mediator changed");
            }
        };


        MediatorLiveData mediator = new MediatorLiveData();
        mediator.addSource(kaleidoViewModel.getAllOrders(), orderObserver);
        mediator.addSource(kaleidoViewModel.getAllBalances(), balanceObserver);
        mediator.addSource(kaleidoViewModel.getLiveMarkets(binanceService), marketObserver);
        mediator.addSource(kaleidoViewModel.getBaseCurrency(), tripletObserver);

        mediator.observe(CurrentPriceFragment.this, mediatorObserver);



//        tickerLiveData.observe(CurrentPriceFragment.this, tickerObserver);

        return view;
    }

    private void setUpUI() {
        adapter = new CurrentAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void setUpLineChart() {
        float count = 1;
        List<Entry> entries = new ArrayList<>();
        for (float total : totalHistory) {
            entries.add(new Entry(count, total));
            count++;
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Total");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void setUpRetrofitBuilder() {
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        binanceService = retrofitBuilder.baseUrl("https://api.binance.com")
                .build()
                .create(BinanceService.class);
    }

    private void setUpViewModelAndObserver() {
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

    }


}
