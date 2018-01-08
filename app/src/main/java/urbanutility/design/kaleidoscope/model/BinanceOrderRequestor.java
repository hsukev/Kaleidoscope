package urbanutility.design.kaleidoscope.model;

/**
 * Created by jerye on 1/7/2018.
 */

public class BinanceOrderRequestor {
    BinancePriceTicker binancePriceTicker;
    BinanceServerTime binanceServerTime;

    public BinanceOrderRequestor(BinancePriceTicker binancePriceTicker, BinanceServerTime binanceServerTime){
        this.binancePriceTicker = binancePriceTicker;
        this.binanceServerTime = binanceServerTime;
    }

    public void setBinancePriceTicker(BinancePriceTicker binancePriceTicker){
        this.binancePriceTicker = binancePriceTicker;
    }
    public BinancePriceTicker getBinancePriceTicker(){
        return this.binancePriceTicker;
    }

    public void setBinanceServerTime(BinanceServerTime binanceServerTime){
        this.binanceServerTime = binanceServerTime;
    }
    public BinanceServerTime getBinanceServerTime(){
        return this.binanceServerTime;
    }
}
