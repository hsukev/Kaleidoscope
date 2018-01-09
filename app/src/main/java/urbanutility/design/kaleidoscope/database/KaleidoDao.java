package urbanutility.design.kaleidoscope.database;


import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import urbanutility.design.kaleidoscope.model.BinanceOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@android.arch.persistence.room.Dao
public interface KaleidoDao {

    @Insert
    public void insertBinanceOrder(BinanceOrder... binanceOrders);

    @Query("SELECT * FROM binance_orders")
    public BinanceOrder[] fetchOrderHistory();
}
