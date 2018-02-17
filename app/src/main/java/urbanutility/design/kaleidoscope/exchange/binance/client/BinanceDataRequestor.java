package urbanutility.design.kaleidoscope.exchange.binance.client;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.KaleidoActivity;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/16/2018.
 */

public class BinanceDataRequestor implements DataRequestor {
    BinanceService binanceService;
    GdaxService gdaxService;

    public BinanceDataRequestor(KaleidoActivity kaleidoActivity) {
        this.binanceService = kaleidoActivity.getBinanceService();
        this.gdaxService = kaleidoActivity.getGdaxService();
    }

    @Override
    public Single<List<LiveMarketType>> requestLiveMarkets() {
        return binanceService.getPriceTickers().subscribeOn(Schedulers.io())
                .map(binanceLiveMarketMapper());
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

    private Single<List<LiveMarketType>> getBinanceLiveMarket() {
        return null;
    }


    private Function<List<BinancePriceTicker>, List<LiveMarketType>> binanceLiveMarketMapper() {
        return new Function<List<BinancePriceTicker>, List<LiveMarketType>>() {
            @Override
            public List<LiveMarketType> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                List<LiveMarketType> liveMarketTypes = new ArrayList<>();
                for (BinancePriceTicker ticker : binancePriceTickers) {
                    LiveMarketType liveMarketType = new LiveMarketType();
                    liveMarketType.exchange = "binance";
                    liveMarketType.symbol = ticker.getSymbol();
                    liveMarketType.price = Double.parseDouble(ticker.getPrice());
                    liveMarketTypes.add(liveMarketType);
                }
                return liveMarketTypes;
            }
        };

    }
}
