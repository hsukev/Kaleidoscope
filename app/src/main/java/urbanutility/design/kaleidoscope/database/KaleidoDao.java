package urbanutility.design.kaleidoscope.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public abstract class KaleidoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertOrder(List<KaleidoOrder> kaleidoOrders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBalance(List<KaleidoBalance> kaleidoBalances);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertDeposit(List<KaleidoDeposits> kaleidoDeposits);

    @Query("SELECT * FROM kaleido_orders ORDER BY time DESC")
    public abstract LiveData<List<KaleidoOrder>> getAllOrders();

    @Query("SELECT * FROM kaleido_balances")
    public abstract LiveData<List<KaleidoBalance>> getAllBalances();

    @Query("SELECT * FROM kaleido_balances WHERE exchange == :exchange")
    public abstract LiveData<List<KaleidoBalance>> getFilteredBalances(String exchange);

    @Query("SELECT * FROM kaleido_deposits")
    public abstract LiveData<List<KaleidoDeposits>> getAllDeposits();
    //by exchange name order
    @Query("SELECT * FROM kaleido_balances")
    public abstract KaleidoBalance[] getAllBalancesStatic();

    @Query("SELECT * FROM kaleido_balances WHERE symbol == :symbol ")
    public abstract KaleidoBalance getBalance(String symbol);

    @Query("SELECT * FROM kaleido_orders WHERE symbol == :symbol")
    public abstract KaleidoOrder getOrder(String symbol);
}
