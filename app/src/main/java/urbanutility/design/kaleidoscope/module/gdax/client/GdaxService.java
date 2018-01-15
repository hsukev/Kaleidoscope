package urbanutility.design.kaleidoscope.module.gdax.client;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jerye on 1/13/2018.
 */

public interface GdaxService {

    @GET("products/BTC-USD/ticker")
    Observable<JsonElement> getHistoricBtc2Usd();

}
