package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import urbanutility.design.kaleidoscope.datatypes.OrderType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;

/**
 * Created by jerye on 1/11/2018.
 */

@Entity(tableName = "kaleido_orders")
public class KaleidoOrder {
    @PrimaryKey
    @NonNull
    public String primaryId;
    @Embedded
    private OrderType ordertype;

    public KaleidoOrder() {
    }

    //Overload constructor for every exchange
    public KaleidoOrder(BinanceOrder binanceOrder, Double btcUsdRate) {
        this.primaryId=binanceOrder.getClientOrderId();
        this.ordertype.id = binanceOrder.getClientOrderId();
        this.ordertype.exchange = "binance";
        this.ordertype.symbol = binanceOrder.getSymbol();
        this.ordertype.amount = Long.valueOf(binanceOrder.getOrigQty());
        this.ordertype.txFee = 0L;
        this.ordertype.price = Long.valueOf(binanceOrder.getPrice());
        this.ordertype.time = binanceOrder.getTime();
        this.ordertype.btcUsdRate = btcUsdRate;
    }
//    public KaleidoOrder(GdaxRate gdaxRate) {
//        this.ordertype.id = gdaxRate.getClientOrderId();
//        this.ordertype.btcUsdRate = gdaxRate.getRate();
//    }

    @NonNull
    public OrderType getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(@NonNull OrderType ordertype) {
        this.ordertype = ordertype;
    }
}
