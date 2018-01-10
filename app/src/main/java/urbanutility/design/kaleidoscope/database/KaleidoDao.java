package urbanutility.design.kaleidoscope.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import urbanutility.design.kaleidoscope.model.BinanceOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public interface KaleidoDao {

    @Insert
    public void insertBinanceOrder(BinanceOrder... binanceOrders);

    @Query("SELECT * FROM binance_orders")
    LiveData<List<BinanceOrder>> fetchOrderHistory();
}
