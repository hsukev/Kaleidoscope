package urbanutility.design.kaleidoscope.util;

import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;

/**
 * Created by jerye on 1/9/2018.
 */

public class TestBinanceOrder {

    public static BinanceOrder createTestBinanceOrder() {
        BinanceOrder binanceOrder = new BinanceOrder();
        binanceOrder.setSymbol("NEOBTC");
        binanceOrder.setOrderId(123122112);
        binanceOrder.setClientOrderId("5245456");
        binanceOrder.setPrice("0.000023252");
        binanceOrder.setOrigQty("143.42424");
        binanceOrder.setExecutedQty("141.22");
        binanceOrder.setStatus("FILLED");
        binanceOrder.setTimeInForce("1314111");
        binanceOrder.setType("LIMIT");
        binanceOrder.setSide("0");
        binanceOrder.setStopPrice("213");
        binanceOrder.setIcebergQty("0");
        binanceOrder.setTime(144446656233L);
        return binanceOrder;
    }
}
