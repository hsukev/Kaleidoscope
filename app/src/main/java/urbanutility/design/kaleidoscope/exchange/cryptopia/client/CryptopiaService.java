package urbanutility.design.kaleidoscope.exchange.cryptopia.client;

import io.reactivex.Single;
import retrofit2.http.GET;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaMarkets;

/**
 * Created by jerye on 1/13/2018.
 */

public interface CryptopiaService {

    @GET("/GetMarkets")
    Single<CryptopiaMarkets> getMarket();



}
