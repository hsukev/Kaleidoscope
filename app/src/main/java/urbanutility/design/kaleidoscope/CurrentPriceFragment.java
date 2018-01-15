package urbanutility.design.kaleidoscope;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPriceFragment extends Fragment {
    @BindView(R.id.line_chart)
    LineChart lineChart;
    float[] totalHistory = {1000.00000f, 1254.1231f, 1134.8547f, 1549.987432f, 5475.102f, 5252.545f, 6666.666f, 6666.6604f, 6666.6123f, 12321.10f, 8080.51444f};


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_price_page, container, false);
        ButterKnife.bind(this,view);
        setUpLineChart();

        return view;
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


}
