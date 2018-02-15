package urbanutility.design.kaleidoscope.client;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.KaleidoActivity;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.exchange.gdax.model.GdaxTicker;

/**
 * Created by jerye on 2/13/2018.
 */

public class KaleidoService {

    BinanceService binanceService;
    GdaxService gdaxService;
    Map<String, Single<List<LiveMarketType>>> sourceMap = new HashMap<>();

    public KaleidoService(KaleidoActivity kaleidoActivity){
        binanceService = kaleidoActivity.getBinanceService();
        gdaxService = kaleidoActivity.getGdaxService();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(kaleidoActivity);
//        sharedPreferences.getStringSet("");
    }

    public Single<List<LiveMarketType>> getLiveData(){
        return Single.zip(getSingleSourceArray(), new Function<Object[], List<LiveMarketType>>() {
            @Override
            public List<LiveMarketType> apply(Object[] objects) throws Exception {
                List<LiveMarketType> list = new ArrayList<>();
                for(Object object: objects){
                    list.addAll((List<LiveMarketType>) object);
                }
                return list;
            }
        });
    }

    private List<Single<List<LiveMarketType>>> getSingleSourceArray(){
        List<Single<List<LiveMarketType>>> list = new ArrayList<>();
        list.add(getBinanceLiveMarket());
        list.add(getGdaxLiveMarket());
        return list;
    }

    private Single<List<LiveMarketType>> getGdaxLiveMarket(){
        return gdaxService.getGdaxLiveMarket().subscribeOn(Schedulers.io()).map(gdaxLiveMarketFunction());
    }

    private Single<List<LiveMarketType>> getBinanceLiveMarket(){
        return binanceService.getPriceTickers().subscribeOn(Schedulers.io())
                .map(binanceLiveMarketMapper());
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
