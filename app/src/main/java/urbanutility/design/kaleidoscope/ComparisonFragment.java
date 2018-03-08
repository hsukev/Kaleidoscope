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
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
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

    private Map<String, Double> balanceMap;
    private KaleidoService kaleidoService;
    private SharedPreferences sharedPreferences;
    private static Set<String> newSet = new HashSet<>();


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
//        balanceMap = loadBalance();
//        loadBalance();
        setUpViewModel();
        observeLiveData();

//        populateView();

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
                kaleidoViewModel.setTripletBalances(kaleidoBalances);
            }
        };

        Observer<List<LiveMarketType>> liveMarketObserver = new Observer<List<LiveMarketType>>() {
            @Override
            public void onChanged(@Nullable List<LiveMarketType> liveMarketTypes) {
                kaleidoViewModel.setPairMarkets(liveMarketTypes);
            }
        };

        Observer<Map<String, Double>> distributedMapObserver = new Observer<Map<String, Double>>() {
            @Override
            public void onChanged(@Nullable Map<String, Double> stringDoubleMap) {
                loadDistributionPieChart(stringDoubleMap);
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
        mediatorLiveData.addSource(kaleidoViewModel.getBalanceMapInBtc(), distributedMapObserver);

        mediatorLiveData.observe(ComparisonFragment.this, dummyObserver);
    }

//    private void populateView() {
//        Set<String> exchangeSet = sharedPreferences.getStringSet("exchange", newSet);
//        if (exchangeSet.size() == 0) {
//        } else {
//            requestLiveMarket();
//        }
//    }

//    private void requestLiveMarket() {
//        kaleidoService.requestLiveMarkets().subscribe(new DisposableSingleObserver<List<LiveMarketType>>() {
//            @Override
//            public void onSuccess(List<LiveMarketType> liveMarketTypes) {
//                Log.d(LOG, "livemarketsize" + liveMarketTypes.size());
//                loadDistributionPieChart(calculateExchangeDistribution(liveMarketTypes));
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//        });
//    }

//    // assign livemarket type to hash map of coin ** exchange specific
//    private Map<String, Double> calculateExchangeDistribution(List<LiveMarketType> liveMarketTypes) {
//        Map<String, Double> exchangeSumsMap = new HashMap<>();
//        for (LiveMarketType liveMarketType : liveMarketTypes) {
////            Log.d(LOG, liveMarketType.symbol);
//
//            if (liveMarketType.symbol.contains("BTC")) {
//                String liveMarketId = KaleidoFunctions.createLiveMarketId(liveMarketType);
//                if (balanceMap.containsKey(liveMarketId)) {
//                    double exchangeSpecificBalance = balanceMap.get(liveMarketId) * liveMarketType.price;
//                    if (exchangeSumsMap.containsKey(liveMarketType.exchange)) {
//                        exchangeSpecificBalance += exchangeSumsMap.get(liveMarketType.exchange);
//                        Log.d(LOG, liveMarketType.price + "");
//
//                    }
//                    exchangeSumsMap.put(liveMarketType.exchange, exchangeSpecificBalance);
//                }
//            }
//
//        }
//        return exchangeSumsMap;
//    }


//    // get raw map from db
//    private Map<String, Double> loadBalance() {
//        KaleidoBalance[] balancesSortedByExchange = KaleidoDatabase.getAppDatabase(getContext()).kaleidoDao().getAllBalancesStatic();
//        Map<String, Double> map = new HashMap<>();
//        for (KaleidoBalance kaleidoBalance : balancesSortedByExchange) {
//            map.put(kaleidoBalance.getId(), kaleidoBalance.balanceType.amount);
//        }
//        return map;
//    }

    // create pie chart from exchangeSumMap
    private void loadDistributionPieChart(Map<String, Double> exchangeSumMap) {
        List<PieEntry> entries = new ArrayList<>();
//        exchangeSumMap.put("cryptopia", 2.312);

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
        for (String key : balanceMap.keySet()) {
            if (key.contains(exchange)) {
                exchangeSum += balanceMap.get(key);
            }
        }

        for (String key : balanceMap.keySet()) {
            if (key.contains(exchange)) {
                String coin = KaleidoFunctions.decodeBalanceId(key)[1];
                double percentage = balanceMap.get(key) / exchangeSum * 100;
                pieEntries.add(new PieEntry((float) percentage, coin));
            }
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
