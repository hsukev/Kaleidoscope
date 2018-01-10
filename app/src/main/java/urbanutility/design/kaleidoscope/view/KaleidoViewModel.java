package urbanutility.design.kaleidoscope.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.model.BinanceOrder;

/**
 * Created by jerye on 1/9/2018.
 */

public class KaleidoViewModel extends AndroidViewModel {

    private LiveData<List<BinanceOrder>> mOrderHistory;
    private KaleidoDatabase kaleidoDatabase;

    public KaleidoViewModel(Application application){
        super(application);

        kaleidoDatabase = kaleidoDatabase.getAppDatabase(this.getApplication());
        mOrderHistory = kaleidoDatabase.kaleidoDao().fetchOrderHistory();

    }

    public LiveData<List<BinanceOrder>> getOrderHistory(){
        return mOrderHistory;
    }
}
