package urbanutility.design.kaleidoscope;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 1/11/2018.
 */

public class ComparisonFragment extends Fragment {
    @BindView(R.id.pie_chart)
    PieChart pieChart;

    private KaleidoViewModel kaleidoViewModel;

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
        setUpViewModelAndObserver();
        loadPieChart();
        return view;
    }


    private void setUpViewModelAndObserver() {
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

    }

    private void loadPieChart() {
        KaleidoBalance[] balancesSortedByExchange = KaleidoDatabase.getAppDatabase(getContext()).kaleidoDao().getAllBalancesStatic();
        List<PieEntry> entries = new ArrayList<>();

        double exchangeSum = 0.0d;
        double totalSum = 0.0d;
        String prevExchange = "";
        if (balancesSortedByExchange.length > 0) {
            prevExchange = balancesSortedByExchange[0].getBalanceType().exchange;
        }

        for (KaleidoBalance balance : balancesSortedByExchange) {
            exchangeSum += balance.balanceType.amount;
            if (!prevExchange.equals(balance.getBalanceType().symbol)){
                totalSum += exchangeSum;
                entries.add(new PieEntry((float) exchangeSum, prevExchange));
                exchangeSum = 0.0d;
            }
        }
        totalSum += exchangeSum;
        entries.add(new PieEntry((float) exchangeSum, prevExchange));
        exchangeSum = 0.0d;

        PieDataSet pieDataSet = new PieDataSet(entries, "Breakdown");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

//        float count = 1;
//        List<Entry> entries = new ArrayList<>();
//        for (float total : totalHistory) {
//            entries.add(new Entry(count, total));
//            count++;
//        }
//        LineDataSet lineDataSet = new LineDataSet(entries, "Total");
//        LineData lineData = new LineData(lineDataSet);
//        lineChart.setData(lineData);
//        lineChart.invalidate();
    }

}
