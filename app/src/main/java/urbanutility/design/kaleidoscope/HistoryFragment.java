package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import urbanutility.design.kaleidoscope.barcode.BarcodeCaptureActivity;
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
    @BindView(R.id.public_key_box)
    EditText publicKeyBox;
    @BindView(R.id.private_key_box)
    EditText privateKeyBox;
    @BindView(R.id.public_key_camera)
    ImageButton public_key_camera;
    @BindView(R.id.private_key_camera)
    ImageButton private_key_camera;
    @BindView(R.id.include_history_help)
    LinearLayout includeHistoryHelp;
    @BindView(R.id.help_button)
    ImageButton helpButton;

    public KaleidoViewModel kaleidoViewModel;
    private OrdersAdapter ordersAdapter;
    private KaleidoService kaleidoService;
    private static final String TAG = HistoryFragment.class.getName();
    private SharedPreferences sharedPreferences;
    private ExchangeListAdapter exchangeListAdapter;

    private static final int RC_BARCODE_CAPTURE = 9001;
    public static final String KEY_SECURITY_LEVEL = "key_security";

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
        Set<String> exchangeSet = sharedPreferences.getStringSet("exchange", null);
        if (exchangeSet == null) {

        } else {
            for (String exchange : exchangeSet) {
                exchangeListAdapter.addExchange(exchange);
            }
        }
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    includeHistoryHelp.setVisibility(View.VISIBLE);
            }
        });
        TextView nextButton = includeHistoryHelp.findViewById(R.id.history_helper_next);
        final ViewSwitcher viewSwitcher = includeHistoryHelp.findViewById(R.id.view_switcher);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
            }
        });
        public_key_camera.setOnClickListener(new CameraOnClickListner(false));
        private_key_camera.setOnClickListener(new CameraOnClickListner(true));


    }

    public class CameraOnClickListner implements View.OnClickListener {
        boolean isPrivate;

        CameraOnClickListner(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        @Override
        public void onClick(View view) {
            // launch barcode activity.
            Intent intent = new Intent(getContext(), BarcodeCaptureActivity.class);
            intent.putExtra(KEY_SECURITY_LEVEL, isPrivate ? 1 : 0);
//            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
//            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
    }

    public class EditTextOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

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
                Log.d(TAG, "orderSize: " + kaleidoOrders.size());
                kaleidoViewModel.insertOrder(kaleidoOrders);
            }

            @Override
            public void onError(Throwable e) {
//                Toast.makeText(getActivity(), "One of your linked exchanges is currently unavailable", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        };
    }

    private DisposableSingleObserver<List<KaleidoBalance>> disposableBalancesSingleObserver() {
        return new DisposableSingleObserver<List<KaleidoBalance>>() {
            @Override
            public void onSuccess(List<KaleidoBalance> kaleidoBalances) {
                Log.d(TAG, "balanceSize: " + kaleidoBalances.size());
                kaleidoViewModel.insertBalance(kaleidoBalances);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    private DisposableSingleObserver<List<KaleidoDeposits>> disposableDepositsSingleObserver() {
        return new DisposableSingleObserver<List<KaleidoDeposits>>() {
            @Override
            public void onSuccess(List<KaleidoDeposits> kaleidoDeposits) {
                Log.d(TAG, "depositSize:" + kaleidoDeposits.size());
                kaleidoViewModel.insertDeposit(kaleidoDeposits);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    int securityLevel = data.getIntExtra(KEY_SECURITY_LEVEL, -1);
                    switch (securityLevel) {
                        case 0:
                            publicKeyBox.setText(barcode.displayValue);
                            break;
                        case 1:
                            privateKeyBox.setText(barcode.displayValue);
                            break;
                        default:
                            // save to clipboard
                            break;

                    }
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(getContext(), "No barcode captured, intent data is null", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(getContext(), CommonStatusCodes.getStatusCodeString(resultCode), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

