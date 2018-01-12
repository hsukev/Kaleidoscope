package urbanutility.design.kaleidoscope.client;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import urbanutility.design.kaleidoscope.model.binance.BinanceOrder;
import urbanutility.design.kaleidoscope.model.binance.BinancePriceTicker;
import urbanutility.design.kaleidoscope.model.binance.BinanceServerTime;

/**
 * Created by jerye on 1/5/2018.
 */

public interface KaleidoService {

    @GET("/api/v1/ping")
    void ping();

    @GET("/api/v1/time")
    Single<BinanceServerTime> getServerTime();

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/api/v3/allOrders")
    Observable<List<BinanceOrder>> getAllOrders(@Query("symbol") String symbol, @Query("timestamp") long timestamp, @Query("recvWindow") long receiveWindow);

    @GET("/api/v1/ticker/allPrices")
    Single<List<BinancePriceTicker>> getPriceTickers();
}
