package urbanutility.design.kaleidoscope;


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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;
import urbanutility.design.kaleidoscope.view.OrdersAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recycler;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.import_button)
    TextView importButton;

    public KaleidoViewModel kaleidoViewModel;
    public Retrofit.Builder retrofitBuilder;
    OrdersAdapter adapter;
    String TAG = HistoryFragment.class.getName();

    public HistoryFragment() {// Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_page, container, false);
        ButterKnife.bind(this, view);

        setUpViewModelAndObserver();
        setUpUI();
        setUpRetrofitBuilder();
        return view;
    }

    private void setUpRetrofitBuilder() {

    }

    private void setUpUI() {
        adapter = new OrdersAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("History", "import clicked");
                KaleidoExchangePort exchangePort = new KaleidoExchangePort(HistoryFragment.this);
                exchangePort.addExchange(spinner.getSelectedItem().toString());
            }
        });
    }

    private void setUpViewModelAndObserver() {
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

        //remain in fragment custom
        final Observer<List<KaleidoOrder>> kaleidoObserver = new Observer<List<KaleidoOrder>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoOrder> kaleidoOrders) {
                adapter.addOrder(kaleidoOrders);
            }
        };

        kaleidoViewModel.getAllOrders().observe(HistoryFragment.this, kaleidoObserver);
    }
}
