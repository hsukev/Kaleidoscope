package urbanutility.design.kaleidoscope.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public interface KaleidoDao {

    @Insert
    void insertOrder(KaleidoOrder... kaleidoOrders);

    @Insert
    void insertBalance(KaleidoBalance... kaleidoBalances);

    @Query("SELECT * FROM kaleido_orders")
    LiveData<List<KaleidoOrder>> getAllOrders();

    @Query("SELECT * FROM kaleido_balances")
    LiveData<List<KaleidoBalance>> getAllBalances();
}
