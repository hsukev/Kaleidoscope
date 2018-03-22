package urbanutility.design.kaleidoscope.exchange.gdax.client;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.client.KaleidoClients;
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.exchange.gdax.model.GdaxTicker;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/16/2018.
 */

public class GdaxDataRequestor implements DataRequestor {
    GdaxService gdaxService;


    public GdaxDataRequestor(KaleidoClients kaleidoClients) {
        this.gdaxService = kaleidoClients.getGdaxService();
    }

    @Override
    public Single<List<KaleidoLiveMarket>> requestLiveMarkets() {
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

    private Function<GdaxTicker, List<KaleidoLiveMarket>> gdaxLiveMarketFunction() {
        return new Function<GdaxTicker, List<KaleidoLiveMarket>>() {
            @Override
            public List<KaleidoLiveMarket> apply(GdaxTicker gdaxTicker) throws Exception {
                List<KaleidoLiveMarket> kaleidoLiveMarkets = new ArrayList<>();
                KaleidoLiveMarket gdaxLive = new KaleidoLiveMarket(
                        "BTCUSD",
                        "gdax",
                        Double.parseDouble(gdaxTicker.getPrice()));
                kaleidoLiveMarkets.add(gdaxLive);
                return kaleidoLiveMarkets;
            }
        };
    }
}
