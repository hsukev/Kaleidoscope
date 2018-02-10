package urbanutility.design.kaleidoscope;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 1/11/2018.
 */

public class ComparisonFragment extends Fragment {
    @BindView(R.id.pie_chart)
    PieChart pieChart;

    String LOG = "ComparisonFragment";

    private KaleidoViewModel kaleidoViewModel;
    private Retrofit.Builder retrofitBuilder;
    private BinanceService binanceService;
    private GdaxService gdaxService;
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
        View view = inflater.inflate(R.layout.ecompare_page, container, false);
        ButterKnife.bind(this, view);
        balanceMap = loadBalance();
        setUpRetrofitBuilder();
        loadBalance();
        setUpViewModelAndObserver();

        return view;
    }


    private void setUpViewModelAndObserver() {
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

        Observer<List<LiveMarketType>> liveMarketObserver = new Observer<List<LiveMarketType>>() {
            @Override
            public void onChanged(@Nullable List<LiveMarketType> liveMarketTypes) {
                loadPieChart(calculateExchangeDistribution(liveMarketTypes));
            }
        };

        kaleidoViewModel.getLiveMarkets(binanceService, gdaxService).observe(ComparisonFragment.this, liveMarketObserver);

    }

    private Map<String, Double> calculateExchangeDistribution(List<LiveMarketType> liveMarketTypes) {
        Map<String, Double> exchangeSumsMap = new HashMap<>();
        for (LiveMarketType liveMarketType : liveMarketTypes) {
            Log.d(LOG, liveMarketType.symbol);

            if (liveMarketType.symbol.contains("BTC")) {
                String liveMarketId = KaleidoFunctions.createLiveMarketId(liveMarketType);
                if (balanceMap.containsKey(liveMarketId)) {
                    double exchangeSpecificBalance = balanceMap.get(liveMarketId)*liveMarketType.price;
                    if (exchangeSumsMap.containsKey(liveMarketType.exchange)) {
                        exchangeSpecificBalance += exchangeSumsMap.get(liveMarketType.exchange);
                        Log.d(LOG, liveMarketType.price+"");

                    }
                    exchangeSumsMap.put(liveMarketType.exchange, exchangeSpecificBalance);
                }
            }

        }
        return exchangeSumsMap;
    }

    private void setUpRetrofitBuilder() {
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        binanceService = retrofitBuilder.baseUrl("https://api.binance.com")
                .build()
                .create(BinanceService.class);
        gdaxService = retrofitBuilder.baseUrl("https://api.gdax.com")
                .build()
                .create(GdaxService.class);
    }

    private Map<String, Double> loadBalance() {
        KaleidoBalance[] balancesSortedByExchange = KaleidoDatabase.getAppDatabase(getContext()).kaleidoDao().getAllBalancesStatic();
        Map<String, Double> map = new HashMap<>();
        for (KaleidoBalance kaleidoBalance : balancesSortedByExchange) {
            map.put(kaleidoBalance.getId(), kaleidoBalance.balanceType.amount);
        }
        return map;
    }


    private void loadPieChart(Map<String, Double> exchangeSumMap) {
        List<PieEntry> entries = new ArrayList<>();
        exchangeSumMap.put("cryptopia", 2.312);

        double totalAsset = 0.0d;
        for(String key: exchangeSumMap.keySet()){
            totalAsset += exchangeSumMap.get(key);
        }

        for(String key: exchangeSumMap.keySet()){
            double percent = exchangeSumMap.get(key)/totalAsset*100;
            entries.add(new PieEntry((float) percent, key));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Breakdown");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setRotationEnabled(false);
        pieChart.invalidate();
    }

}
