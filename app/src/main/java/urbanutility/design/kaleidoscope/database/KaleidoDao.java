package urbanutility.design.kaleidoscope.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public abstract class KaleidoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertOrder(KaleidoOrder... kaleidoOrders);

    @Insert
    public abstract void insertBalance(KaleidoBalance... kaleidoBalances);

    @Query("SELECT * FROM kaleido_orders")
    public abstract LiveData<List<KaleidoOrder>> getAllOrders();

    @Transaction
    public void queryAndUpdate(){
        
    }



    @Query("SELECT * FROM kaleido_balances")
    public abstract LiveData<List<KaleidoBalance>> getAllBalances();

}
