package urbanutility.design.kaleidoscope.exchange.gdax.client;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.KaleidoActivity;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.gdax.model.GdaxTicker;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/16/2018.
 */

public class GdaxDataRequestor implements DataRequestor {
    GdaxService gdaxService;


    public GdaxDataRequestor(KaleidoActivity kaleidoActivity) {
        this.gdaxService = kaleidoActivity.getGdaxService();
    }

    @Override
    public Single<List<LiveMarketType>> requestLiveMarkets() {
        return gdaxService.getGdaxLiveMarket().subscribeOn(Schedulers.io()).map(gdaxLiveMarketFunction());
    }

    @Override
    public Single<List<KaleidoBalance>> requestBalances() {
        return null;
    }

    @Override
    public Single<List<KaleidoOrder>> requestOrders() {
        return null;
    }

    @Override
    public Single<List<KaleidoDeposits>> requestDeposits() {
        return null;
    }

    private Function<GdaxTicker, List<LiveMarketType>> gdaxLiveMarketFunction() {
        return new Function<GdaxTicker, List<LiveMarketType>>() {
            @Override
            public List<LiveMarketType> apply(GdaxTicker gdaxTicker) throws Exception {
                List<LiveMarketType> liveMarketTypes = new ArrayList<>();
                LiveMarketType gdaxLive = new LiveMarketType();
                gdaxLive.exchange = "gdax";
                gdaxLive.symbol = "BTCUSD";
                gdaxLive.price = Double.parseDouble(gdaxTicker.getPrice());
                liveMarketTypes.add(gdaxLive);
                return liveMarketTypes;
            }
        };
    }
}
