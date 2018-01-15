package urbanutility.design.kaleidoscope.module.binance.client;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import urbanutility.design.kaleidoscope.module.binance.model.Binance24hTicker;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceAccountInfo;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.module.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.module.binance.model.BinanceServerTime;

/**
 * Created by jerye on 1/5/2018.
 */

public interface BinanceService {

    @GET("/api/v1/ping")
    void ping();

    @GET("/api/v1/time")
    Single<BinanceServerTime> getServerTime();

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/api/v3/allOrders")
    Observable<List<BinanceOrder>> getAllOrders(@Query("symbol") String symbol, @Query("timestamp") long timestamp, @Query("recvWindow") long receiveWindow);

    @GET("/api/v1/ticker/24h")
    Single<Binance24hTicker> get24hTicker(@Query("symbol") String symbol);

    @GET("/api/v1/ticker/allPrices")
    Single<List<BinancePriceTicker>> getPriceTickers();

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/api/v3/account") //balance
    Single<BinanceAccountInfo> getAccountInfo(@Query("timestamp") long timestamp);
}
