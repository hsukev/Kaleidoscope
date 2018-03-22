package urbanutility.design.kaleidoscope;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 1/11/2018.
 */

public class ComparisonFragment extends Fragment implements OnChartValueSelectedListener {
    @BindView(R.id.combined_pie_chart)
    PieChart combinedPieChart;
    @BindView(R.id.exchange_pie_chart)
    PieChart exchangePieChart;

    String LOG = "ComparisonFragment";

    private KaleidoViewModel kaleidoViewModel;
    private KaleidoService kaleidoService;
    private SharedPreferences sharedPreferences;
    private static Set<String> newSet = new HashSet<>();
    private Map<String, Double> balanceMap;


    public ComparisonFragment() {
        // Required empty public constructor
    }

    public static ComparisonFragment newInstance() {
        Bundle args = new Bundle();
        ComparisonFragment fragment = new ComparisonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange_compare_page, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("exchange", Context.MODE_PRIVATE);
        kaleidoService = ((KaleidoActivity) getActivity()).getKaleidoService();
        setUpViewModel();
        observeLiveData();
        return view;
    }

    private void setUpViewModel() {
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);
    }

    private void observeLiveData() {
        Observer<List<KaleidoBalance>> balanceObserver = new Observer<List<KaleidoBalance>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoBalance> kaleidoBalances) {
                kaleidoViewModel.setPairBalances(kaleidoBalances);
            }
        };

        Observer<List<KaleidoLiveMarket>> liveMarketObserver = new Observer<List<KaleidoLiveMarket>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoLiveMarket> liveMarketTypes) {
                kaleidoViewModel.setPairMarkets(liveMarketTypes);
            }
        };

        Observer<Map<String, Double>> distributedMapObserver = new Observer<Map<String, Double>>() {
            @Override
            public void onChanged(@Nullable Map<String, Double> distributedMap) {
                loadDistributionPieChart(distributedMap);
            }
        };
        Observer<Map<String, Double>> balanceMapObserver = new Observer<Map<String, Double>>() {
            @Override
            public void onChanged(@Nullable Map<String, Double> balanceMapObserver) {
                balanceMap = balanceMapObserver;
            }
        };

        Observer dummyObserver = new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {

            }
        };

        MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(kaleidoViewModel.getAllBalances(), balanceObserver);
        mediatorLiveData.addSource(kaleidoViewModel.getAllLiveMarkets(kaleidoService), liveMarketObserver);
        mediatorLiveData.addSource(kaleidoViewModel.getBalanceMapInBtc(true), distributedMapObserver);
        mediatorLiveData.addSource(kaleidoViewModel.getBalanceMapInBtc(false), balanceMapObserver);

        mediatorLiveData.observe(ComparisonFragment.this, dummyObserver);
    }

    // create pie chart from exchangeSumMap
    private void loadDistributionPieChart(Map<String, Double> exchangeSumMap) {
        Log.d("exchange map", "binance "+exchangeSumMap.get("binance"));
        List<PieEntry> entries = new ArrayList<>();
        exchangeSumMap.put("cryptopia", 2.312);

        double totalAsset = 0.0d;
        for (String key : exchangeSumMap.keySet()) {
            totalAsset += exchangeSumMap.get(key);
        }

        for (String key : exchangeSumMap.keySet()) {
            double percent = exchangeSumMap.get(key) / totalAsset * 100;
            entries.add(new PieEntry((float) percent, key));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Breakdown");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        combinedPieChart.setData(pieData);
        combinedPieChart.setRotationEnabled(false);
        combinedPieChart.setHoleRadius(65.0f);
        combinedPieChart.setTransparentCircleRadius(70.0f);
        combinedPieChart.setOnChartValueSelectedListener(this);
        combinedPieChart.invalidate();

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(LOG, "clicked");
        String exchange = ((PieEntry) e).getLabel();
        List<PieEntry> pieEntries = new ArrayList<>();
        double exchangeSum = 0.0d;

        if(balanceMap != null){
            for(String key: balanceMap.keySet()){
                Log.d(LOG, "balanceKey: " + key);
                if(KaleidoFunctions.decodeBalanceIdExchange(key).equals(exchange)){
                    pieEntries.add(new PieEntry(balanceMap.get(key).floatValue()));
                }
            }
        }else{
            Toast.makeText(getContext(), "Unable to load exchange data", Toast.LENGTH_SHORT).show();
        }

        loadExchangePieChart(pieEntries, exchange);
    }

    @Override
    public void onNothingSelected() {
        Log.d(LOG, "unclicked");
        exchangePieChart.animate().alpha(0.0f).setInterpolator(new FastOutSlowInInterpolator());

    }

    private void loadExchangePieChart(List<PieEntry> list, String exchange) {
        PieDataSet pieDataSet = new PieDataSet(list, exchange);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        exchangePieChart.setData(pieData);
        exchangePieChart.setRotationEnabled(false);
        exchangePieChart.setHoleRadius(0.0f);
        exchangePieChart.setTransparentCircleRadius(5.0f);
        exchangePieChart.invalidate();
        exchangePieChart.animate().alpha(1.0f)
                .setInterpolator(new FastOutSlowInInterpolator());
    }
}
