package urbanutility.design.kaleidoscope;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jerye on 1/11/2018.
 */

public class ComparisonFragment extends Fragment{
    @BindView(R.id.pie_chart)
    PieChart pieChart;

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
        View view = inflater.inflate(R.layout.exchange_comparison_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
