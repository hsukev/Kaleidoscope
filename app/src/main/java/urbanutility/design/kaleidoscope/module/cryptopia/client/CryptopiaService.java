package urbanutility.design.kaleidoscope.module.cryptopia.client;

import io.reactivex.Single;
import retrofit2.http.GET;
import urbanutility.design.kaleidoscope.module.cryptopia.model.CryptopiaMarkets;

/**
 * Created by jerye on 1/13/2018.
 */

public interface CryptopiaService {

    @GET("/GetMarkets")
    Single<CryptopiaMarkets> getMarket();



}
