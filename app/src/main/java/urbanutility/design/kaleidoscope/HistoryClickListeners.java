package urbanutility.design.kaleidoscope;

import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 3/24/2018.
 */

public class HistoryClickListeners {

    public static View.OnClickListener syncAllClickListener(final KaleidoService kaleidoService, final KaleidoViewModel kaleidoViewModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kaleidoService.requestOrders().observeOn(AndroidSchedulers.mainThread()).subscribe(disposableOrdersSingleObserver(kaleidoViewModel));
            }
        };
    }


    public static DisposableSingleObserver<List<KaleidoOrder>> disposableOrdersSingleObserver(final KaleidoViewModel kaleidoViewModel) {
        return new DisposableSingleObserver<List<KaleidoOrder>>() {
            @Override
            public void onSuccess(List<KaleidoOrder> kaleidoOrders) {
                kaleidoViewModel.insertOrder(kaleidoOrders);
            }

            @Override
            public void onError(Throwable e) {
//                Toast.makeText(getActivity(), "One of your linked exchanges is currently unavailable", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        };
    }

    public static DisposableSingleObserver<List<KaleidoBalance>> disposableBalancesSingleObserver(final KaleidoViewModel kaleidoViewModel) {
        return new DisposableSingleObserver<List<KaleidoBalance>>() {
            @Override
            public void onSuccess(List<KaleidoBalance> kaleidoBalances) {
                kaleidoViewModel.insertBalance(kaleidoBalances);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    public static DisposableSingleObserver<List<KaleidoDeposits>> disposableDepositsSingleObserver(final KaleidoViewModel kaleidoViewModel) {
        return new DisposableSingleObserver<List<KaleidoDeposits>>() {
            @Override
            public void onSuccess(List<KaleidoDeposits> kaleidoDeposits) {
                kaleidoViewModel.insertDeposit(kaleidoDeposits);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }
}
