package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import urbanutility.design.kaleidoscope.datatypes.OrderType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistory;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 1/11/2018.
 */

@Entity(tableName = "kaleido_orders")
public class KaleidoOrder {
    @PrimaryKey
    @NonNull
    public String primaryId;

    @Embedded
    public OrderType ordertype;

    public KaleidoOrder() {
    }

    //Overload constructor for every exchange
    public KaleidoOrder(BinanceOrder binanceOrder, Double btcUsdRate) {
        ordertype = new OrderType();
        this.primaryId=binanceOrder.getClientOrderId();
        this.ordertype.id = String.valueOf(binanceOrder.getClientOrderId());
        this.ordertype.exchange = "binance";
        this.ordertype.symbol = binanceOrder.getSymbol();
        this.ordertype.amount = Double.parseDouble(binanceOrder.getExecutedQty());
        this.ordertype.side = binanceOrder.getSide();
        this.ordertype.txFee = 0.0d;
        this.ordertype.price = Double.parseDouble(binanceOrder.getPrice());
        this.ordertype.time = KaleidoFunctions.convertMilliISO8601(binanceOrder.getTime());
        this.ordertype.btcUsdRate = btcUsdRate;
    }
    public KaleidoOrder(CryptopiaTradeHistory binanceOrder, Double btcUsdRate) {
        ordertype = new OrderType();
        this.primaryId=binanceOrder.getClientOrderId();
        this.ordertype.id = String.valueOf(binanceOrder.getClientOrderId());
        this.ordertype.exchange = "binance";
        this.ordertype.symbol = binanceOrder.getSymbol();
        this.ordertype.amount = Double.parseDouble(binanceOrder.getExecutedQty());
        this.ordertype.side = binanceOrder.getSide();
        this.ordertype.txFee = 0.0d;
        this.ordertype.price = Double.parseDouble(binanceOrder.getPrice());
        this.ordertype.time = KaleidoFunctions.convertMilliISO8601(binanceOrder.getTime());
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
