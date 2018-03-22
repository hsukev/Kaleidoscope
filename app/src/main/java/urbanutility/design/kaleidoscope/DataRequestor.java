package urbanutility.design.kaleidoscope;

import java.util.List;

import io.reactivex.Single;
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/16/2018.
 */

public interface DataRequestor {
    Single<List<KaleidoLiveMarket>> requestLiveMarkets();
    Single<List<KaleidoBalance>> requestBalances();
    Single<List<KaleidoOrder>> requestOrders();
    Single<List<KaleidoDeposits>> requestDeposits();
}
