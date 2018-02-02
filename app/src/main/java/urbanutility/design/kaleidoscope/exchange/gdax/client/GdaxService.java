package urbanutility.design.kaleidoscope.exchange.gdax.client;

import com.google.gson.JsonElement;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import urbanutility.design.kaleidoscope.exchange.gdax.model.GdaxTicker;

/**
 * Created by jerye on 1/13/2018.
 */

public interface GdaxService {

    @GET("products/BTC-USD/candles")
    Observable<JsonElement> getHistoricBtc2Usd(@Query("start") String startTime,
                                               @Query("end") String endTime,
                                               @Query("granularity") int granularity);

    @GET("products/BTC-USD/ticker")
    Flowable<GdaxTicker> getGdaxLiveMarket();

}
