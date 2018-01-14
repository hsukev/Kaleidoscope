package urbanutility.design.kaleidoscope.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import urbanutility.design.kaleidoscope.model.KaleidoRawBalance;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public abstract class KaleidoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertOrder(KaleidoOrder... kaleidoOrders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBalance(KaleidoRawBalance... kaleidoBalances);

    @Query("SELECT * FROM kaleido_orders")
    public abstract LiveData<List<KaleidoOrder>> getAllOrders();

    @Query("SELECT * FROM KaleidoRawBalance")
    public abstract LiveData<List<KaleidoRawBalance>> getAllBalances();

    @Query("SELECT * FROM KaleidoRawBalance WHERE symbol == :symbol ")
    public abstract KaleidoRawBalance getBalance(String symbol);


}
