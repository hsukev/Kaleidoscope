package urbanutility.design.kaleidoscope.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceDataRequestor;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxDataRequestor;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/16/2018.
 */

public class ExchangePort {
    // Maps of all supported request RxJava singles
    private Map<String, Single<List<KaleidoLiveMarket>>> liveMarketMap = new HashMap<>();
    private Map<String, Single<List<KaleidoOrder>>> ordersMap = new HashMap<>();
    private Map<String, Single<List<KaleidoBalance>>> balancesMap = new HashMap<>();
    private Map<String, Single<List<KaleidoDeposits>>> depositsMap = new HashMap<>();

    // Add more supportExchanges as necessary
    public ExchangePort(KaleidoClients kaleidoClients){
        supportExchange("binance", new BinanceDataRequestor(kaleidoClients));
        supportExchange("gdax", new GdaxDataRequestor(kaleidoClients));
        //cryptopia
        //kucoin
        //bittrex
    }

    //
    private void supportExchange(String exchangeName, DataRequestor dataRequestor){
        liveMarketMap.put(exchangeName, dataRequestor.requestLiveMarkets());
        ordersMap.put(exchangeName, dataRequestor.requestOrders());
        balancesMap.put(exchangeName, dataRequestor.requestBalances());
        depositsMap.put(exchangeName, dataRequestor.requestDeposits());
    }

    // Exposed getter methods
    public Map<String, Single<List<KaleidoLiveMarket>>> getLiveMarketMap() {
        return liveMarketMap;
    }

    public Map<String, Single<List<KaleidoOrder>>> getOrdersMap() {
        return ordersMap;
    }

    public Map<String, Single<List<KaleidoBalance>>> getBalancesMap() {
        return balancesMap;
    }

    public Map<String, Single<List<KaleidoDeposits>>> getDepositsMap() {
        return depositsMap;
    }
}
