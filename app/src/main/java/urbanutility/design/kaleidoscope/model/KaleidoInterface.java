package urbanutility.design.kaleidoscope.model;

import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;

/**
 * Created by jerye on 2/12/2018.
 */

public interface KaleidoInterface{
    void buildRetrofitServices();
    GdaxService getGdaxService();
    BinanceService getBinanceService();
}
