package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
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
    BinanceService binanceService;
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
        View view = inflater.inflate(R.layout.transactions_page,container,false);
        ButterKnife.bind(this,view);

        setUpViewModelAndObserver();
        setUpUI();
        setUpRetrofitBuilder();
        return view;
    }

    private void setUpRetrofitBuilder(){
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private void setUpUI() {
        adapter = new OrdersAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaleidoExchangePort exchangePort = new KaleidoExchangePort(HistoryFragment.this);
                exchangePort.addExchange(spinner.getSelectedItem().toString());
            }
        });
    }

    private void setUpViewModelAndObserver(){
        //move to main activity
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);
        //remain in fragment custom
        final Observer<List<KaleidoOrder>> kaleidoObserver = new Observer<List<KaleidoOrder>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoOrder> kaleidoOrders) {
                adapter.addOrder(kaleidoOrders);
            }
        };
        kaleidoViewModel.getAllOrders().observe(HistoryFragment.this,kaleidoObserver);
    }
}
