package urbanutility.design.kaleidoscope.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/9/2018.
 */

public class KaleidoViewModel extends AndroidViewModel {

    private KaleidoDatabase kaleidoDatabase;

    public KaleidoViewModel(Application application){
        super(application);

        kaleidoDatabase = kaleidoDatabase.getAppDatabase(this.getApplication());
    }

    //use async
    public LiveData<List<KaleidoOrder>> getAllOrders(){
        return kaleidoDatabase.kaleidoDao().getAllOrders();
    }


    public LiveData<List<KaleidoBalance>> getAllBalances(){
        return kaleidoDatabase.kaleidoDao().getAllBalances();
    }

    public void insertOrder(KaleidoOrder kaleidoOrder){
        kaleidoDatabase.kaleidoDao().insertOrder(kaleidoOrder);
    }

    public void insertBalance(KaleidoBalance kaleidoBalance){
        kaleidoDatabase.kaleidoDao().insertBalance(kaleidoBalance);
    }

}
