package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.datatypes.PositionType;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
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
    @BindView(R.id.current_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.current_recycler)
    RecyclerView recycler;
    @BindView(R.id.btc_total)
    TextView btcTotalView;
    @BindView(R.id.percent_growth_total)
    TextView percentGrowthView;

    float[] totalHistory = {1000.00000f, 1254.1231f, 1134.8547f, 1549.987432f, 5475.102f, 5252.545f, 6666.666f, 6666.6604f, 6666.6123f, 12321.10f, 8080.51444f};

    private KaleidoActivity kaleidoActivity;
    private KaleidoViewModel kaleidoViewModel;
    private BinanceService binanceService;
    private GdaxService gdaxService;
    private CurrentAdapter adapter;
    private KaleidoService kaleidoService;

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
        setUpViewModelAndObserver();
        kaleidoService = ((KaleidoActivity)getActivity()).getKaleidoService();
        ButterKnife.bind(this, view);
        setUpUI();
        setUpLineChart();

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
                Log.d(TAG, baseCurrencies.get(1).getBaseCurrencyType().positions.size() + "");

                double baseTotal = 0.0d;
                double btcTotal = 0.0d;
                double percentTotal = 0.0d;

                for (PositionType position : baseCurrencies.get(0).getBaseCurrencyType().positions) {
                    baseTotal += position.cost;
                    percentTotal += position.cost * position.changePercent;
                }

                for (PositionType position : baseCurrencies.get(1).getBaseCurrencyType().positions) {
                    btcTotal += position.cost;
                }

                if (baseTotal == 0.0d) baseTotal = 1.0d;
                btcTotalView.setText(btcTotal + " -- " + baseTotal);
                percentGrowthView.setText(percentTotal / baseTotal + "");

                if (baseCurrencies.get(1).getBaseCurrencyType().positions.size() > 0)
                    adapter.refresh(baseCurrencies);


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
        mediator.addSource(kaleidoViewModel.getBaseCurrency(), tripletObserver);

        mediator.observe(CurrentPriceFragment.this, mediatorObserver);


//        tickerLiveData.observe(CurrentPriceFragment.this, tickerObserver);

        return view;
    }

    private void setUpUI() {
        adapter = new CurrentAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLiveMarket();
            }
        });
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


    private void setUpViewModelAndObserver() {
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

    }

    private void requestLiveMarket() {
        kaleidoService.requestLiveMarkets().observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSingleObserver<List<LiveMarketType>>() {
            @Override
            public void onSuccess(List<LiveMarketType> liveMarketTypes) {
                kaleidoViewModel.setTripletMarkets(liveMarketTypes);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}




