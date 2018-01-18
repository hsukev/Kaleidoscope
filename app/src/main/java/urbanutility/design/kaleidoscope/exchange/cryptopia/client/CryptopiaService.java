package urbanutility.design.kaleidoscope.exchange.cryptopia.client;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaBalance;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaMarkets;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistoryResponse;

/**
 * Created by jerye on 1/13/2018.
 */

public interface CryptopiaService {

    @GET("/GetMarkets")
    Single<CryptopiaMarkets> getMarket();

    @GET("/GetBalance")
    Single<CryptopiaBalance> getBalance();

    @GET("/GetTradeHistory")
    Single<CryptopiaTradeHistoryResponse> getTradeHistory();
}
