package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.view.ExchangeListAdapter;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;
import urbanutility.design.kaleidoscope.view.OrdersAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements ExchangeListAdapter.ExchangeListClickHandler {
    @BindView(R.id.recyclerOrderHistory)
    RecyclerView recyclerOrders;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.import_button)
    TextView importButton;
    @BindView(R.id.sync_all_button)
    TextView syncAllButton;
    @BindView(R.id.recyclerExchangeList)
    RecyclerView recyclerExchangeList;

    public KaleidoViewModel kaleidoViewModel;
    private OrdersAdapter ordersAdapter;
    private KaleidoService kaleidoService;
    private String TAG = HistoryFragment.class.getName();
    private SharedPreferences sharedPreferences;
    private ExchangeListAdapter exchangeListAdapter;

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
        sharedPreferences = getActivity().getSharedPreferences("exchange", Context.MODE_PRIVATE);
        kaleidoService = ((KaleidoActivity) getActivity()).getKaleidoService();
        setUpViewModelAndObserver();
        setUpUI();
        return view;
    }

    private void setUpUI() {
        ordersAdapter = new OrdersAdapter(getContext());
        recyclerOrders.setAdapter(ordersAdapter);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(spinner.getSelectedItem().toString());
            }
        });
        syncAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kaleidoService.requestOrders().observeOn(AndroidSchedulers.mainThread()).subscribe(disposableOrdersSingleObserver());
            }
        });
        exchangeListAdapter = new ExchangeListAdapter(this);

        recyclerExchangeList.setAdapter(exchangeListAdapter);
        recyclerExchangeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Set<String> exchangeSet = getActivity().getPreferences(Context.MODE_PRIVATE).getStringSet("exchange", null);
        for (String exchange : exchangeSet) {
            exchangeListAdapter.addExchange(exchange);
        }
    }

    private void setUpViewModelAndObserver() {
        kaleidoViewModel = ViewModelProviders.of(this).get(KaleidoViewModel.class);

        //remain in fragment custom
        final Observer<List<KaleidoOrder>> kaleidoObserver = new Observer<List<KaleidoOrder>>() {
            @Override
            public void onChanged(@Nullable List<KaleidoOrder> kaleidoOrders) {
                ordersAdapter.addOrder(kaleidoOrders);
            }
        };

        kaleidoViewModel.getAllOrders().observe(HistoryFragment.this, kaleidoObserver);
    }

    @Override
    public void update(String exchangeName) {
        kaleidoService.requestSingularOrders(exchangeName)
                .subscribe(disposableOrdersSingleObserver());
        kaleidoService.requestSingularBalances(exchangeName)
                .subscribe(disposableBalancesSingleObserver());
        kaleidoService.requestSingularDeposits(exchangeName)
                .subscribe(disposableDepositsSingleObserver());
    }

    @Override
    public void delete(String exchangeName) {
        Set<String> exchangeSet = sharedPreferences.getStringSet("exchange", new HashSet<String>());
        if (exchangeSet.contains(exchangeName)) exchangeSet.remove(exchangeName);
        sharedPreferences.edit().putStringSet("exchange", exchangeSet).apply();
    }

    private DisposableSingleObserver<List<KaleidoOrder>> disposableOrdersSingleObserver() {
        return new DisposableSingleObserver<List<KaleidoOrder>>() {
            @Override
            public void onSuccess(List<KaleidoOrder> kaleidoOrders) {
                Log.d(TAG, "orderSize: " + kaleidoOrders);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "One of your linked exchanges is currently unavailable", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        };
    }
    private DisposableSingleObserver<List<KaleidoBalance>> disposableBalancesSingleObserver() {
        return new DisposableSingleObserver<List<KaleidoBalance>>() {
            @Override
            public void onSuccess(List<KaleidoBalance> kaleidoBalances) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
    private DisposableSingleObserver<List<KaleidoDeposits>> disposableDepositsSingleObserver() {
        return new DisposableSingleObserver<List<KaleidoDeposits>>() {
            @Override
            public void onSuccess(List<KaleidoDeposits> kaleidoDeposits) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}

