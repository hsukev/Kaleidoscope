package urbanutility.design.kaleidoscope;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import urbanutility.design.kaleidoscope.barcode.BarcodeCaptureActivity;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.view.ExchangeListAdapter;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;
import urbanutility.design.kaleidoscope.view.OrdersAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements ExchangeListAdapter.ExchangeListClickHandler {
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.import_button)
    TextView importButton;
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
    @BindView(R.id.history_tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.history_viewPager)
    ViewPager viewPager;

    static class TabExchangesLayout {
        @BindView(R.id.sync_all_button)
        @Nullable
        TextView syncAllButton;
        @BindView(R.id.recyclerExchangeList)
        @Nullable
        RecyclerView recyclerExchangeList;
    }

    static class TabOrdersLayout {
        @BindView(R.id.recyclerOrderHistory)
        @Nullable
        RecyclerView recyclerOrders;
    }

    public KaleidoViewModel kaleidoViewModel;
    private OrdersAdapter ordersAdapter;
    private KaleidoService kaleidoService;
    private static final String TAG = HistoryFragment.class.getName();
    private SharedPreferences sharedPreferences;
    private ExchangeListAdapter exchangeListAdapter;

    private static final int RC_BARCODE_CAPTURE = 9001;
    public static final String KEY_SECURITY_LEVEL = "key_security";
    private static Set<String> newSet = new HashSet<>();

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
        View view = inflater.inflate(R.layout.history_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        kaleidoService = ((KaleidoActivity) getActivity()).getKaleidoService();
        setUpViewModelAndObserver();
        setUpUI();
        setUpTabs();
        return view;
    }

    private void setUpTabs() {
        viewPager.setAdapter(new HistoryPagerAdapter(getContext()));
        tabLayout.setupWithViewPager(viewPager);

//        TabExchangesLayout tabExchangesLayout = new TabExchangesLayout();
//        TabOrdersLayout tabOrdersLayout = new TabOrdersLayout();
//        ButterKnife.bind(tabOrdersLayout, ordersTab);
//        ButterKnife.bind(tabExchangesLayout, history_tab2);
//
//        tabOrdersLayout.recyclerOrders.setAdapter(ordersAdapter);
//        tabOrdersLayout.recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        tabExchangesLayout.syncAllButton.setOnClickListener(HistoryClickListeners.syncAllClickListener(kaleidoService, kaleidoViewModel));
//        tabExchangesLayout.recyclerExchangeList.setAdapter(exchangeListAdapter);
//        tabExchangesLayout.recyclerExchangeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void setUpUI() {
        ordersAdapter = new OrdersAdapter(getContext());
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(spinner.getSelectedItem().toString());

                Set<String> oldSet = sharedPreferences.getStringSet("exchange", newSet);
                oldSet.add(spinner.getSelectedItem().toString());
                oldSet.add("gdax");
                sharedPreferences.edit().putStringSet("exchange", oldSet).apply();
            }
        });
        exchangeListAdapter = new ExchangeListAdapter(this);
        Set<String> exchangeSet = sharedPreferences.getStringSet("exchange", newSet);
        if (exchangeSet.size() == 0) {
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
                .subscribe(HistoryClickListeners.disposableOrdersSingleObserver(kaleidoViewModel));
        kaleidoService.requestSingularBalances(exchangeName)
                .subscribe(HistoryClickListeners.disposableBalancesSingleObserver(kaleidoViewModel));
        kaleidoService.requestSingularDeposits(exchangeName)
                .subscribe(HistoryClickListeners.disposableDepositsSingleObserver(kaleidoViewModel));
    }

    @Override
    public void delete(String exchangeName) {
        Set<String> exchangeSet = sharedPreferences.getStringSet("exchange", new HashSet<String>());
        if (exchangeSet.contains(exchangeName)) exchangeSet.remove(exchangeName);
        sharedPreferences.edit().putStringSet("exchange", exchangeSet).apply();
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