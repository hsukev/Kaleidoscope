package urbanutility.design.kaleidoscope.exchange.binance.client;

import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import urbanutility.design.kaleidoscope.DataRequestor;
import urbanutility.design.kaleidoscope.client.KaleidoClients;
import urbanutility.design.kaleidoscope.datatypes.BalanceType;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceAccountInfo;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceBalance;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceDeposit;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceDepositList;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinancePriceTicker;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceWithdrawList;
import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceWithdrawal;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.BaseAlts;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 2/16/2018.
 */

public class BinanceDataRequestor implements DataRequestor {
    private BinanceService binanceService;
    private GdaxService gdaxService;
    private int DELAY_BUFFER = 500;
    private int BINANCE_WEIGHT_PER_SECOND = 10;
    private int BINANCE_ALLORDERS_WEIGHT = 5;
    private int RECVWINDOWLAG = 100000;
    private int RECVWINDOW = 300000;

    final private int DELAY_MILLIS = BINANCE_ALLORDERS_WEIGHT/BINANCE_WEIGHT_PER_SECOND * 1000 + DELAY_BUFFER;

    public BinanceDataRequestor(KaleidoClients kaleidoClients) {
        this.binanceService = kaleidoClients.getBinanceService();
        this.gdaxService = kaleidoClients.getGdaxService();
    }

    @Override
    public Single<List<LiveMarketType>> requestLiveMarkets() {
        return binanceService.getPriceTickers().subscribeOn(Schedulers.io())
                .map(binanceLiveMarketMapper());
    }


    @Override
    public Single<List<KaleidoBalance>> requestBalances() {
        return binanceService.getAccountInfo(System.currentTimeMillis() - 10000, 300000)
                .subscribeOn(Schedulers.io())
                .map(accountToBalanceMapper())
                .flattenAsObservable(flattenBalances())
                .filter(filterFreedBalances())
                .map(createKaleidoBalance())
                .toList();
    }

    @Override
    public Single<List<KaleidoOrder>> requestOrders() {
        return binanceService.getPriceTickers()
                .subscribeOn(Schedulers.single())
                .flattenAsObservable(iterableTickerFunction())
                .concatMap(tickerDelayFunction())
                .flatMap(tickerToOrderFlatMap())
                .filter(orderSizeFilter())
                .flatMapIterable(flattenOrderList())
                .concatMap(orderDelayFunction())
                .filter(orderTypeFilter())
                .flatMap(conversionRatesFunction(),createKaleidoOrder())
                .toList();
    }

    @Override
    public Single<List<KaleidoDeposits>> requestDeposits() {
         Observable<KaleidoDeposits> deposits = binanceService.getBinanceDeposits(System.currentTimeMillis() - RECVWINDOWLAG, RECVWINDOW)
                 .subscribeOn(Schedulers.io())
                .map(new Function<BinanceDeposit, List<BinanceDepositList>>() {
                    @Override
                    public List<BinanceDepositList> apply(BinanceDeposit binanceDeposit) throws Exception {
                        return binanceDeposit.getBinanceDepositList();
                    }
                })
                .flattenAsObservable(new Function<List<BinanceDepositList>, Iterable<BinanceDepositList>>() {
                    @Override
                    public Iterable<BinanceDepositList> apply(List<BinanceDepositList> binanceDepositLists) throws Exception {
                        return binanceDepositLists;
                    }
                })
                .map(new Function<BinanceDepositList, KaleidoDeposits>() {
                    @Override
                    public KaleidoDeposits apply(BinanceDepositList binanceDepositList) throws Exception {
                        return new KaleidoDeposits(binanceDepositList.getTxId(),
                                "deposit",
                                "binance",
                                binanceDepositList.getAmount(),
                                binanceDepositList.getAsset(),
                                binanceDepositList.getAmount()*0.001
                        );
                    }
                });

         Observable<KaleidoDeposits> withdrawals = binanceService.getBinanceWithdrawals(System.currentTimeMillis() - RECVWINDOWLAG, RECVWINDOW)
                 .map(new Function<BinanceWithdrawal, List<BinanceWithdrawList>>() {
                     @Override
                     public List<BinanceWithdrawList> apply(BinanceWithdrawal binanceWithdrawal) throws Exception {
                         return binanceWithdrawal.getWithdrawList();
                     }
                 })
                 .flattenAsObservable(new Function<List<BinanceWithdrawList>, Iterable<BinanceWithdrawList>>() {
                     @Override
                     public Iterable<BinanceWithdrawList> apply(List<BinanceWithdrawList> binanceWithdrawLists) throws Exception {
                         return binanceWithdrawLists;
                     }
                 })
                 .map(new Function<BinanceWithdrawList, KaleidoDeposits>() {
                     @Override
                     public KaleidoDeposits apply(BinanceWithdrawList binanceWithdrawList) throws Exception {
                         return new KaleidoDeposits(
                                 binanceWithdrawList.getId(),
                                 "withdraw",
                                 "binance",
                                 binanceWithdrawList.getAmount(),
                                 binanceWithdrawList.getAsset(),
                                 binanceWithdrawList.getAmount()*0.001
                         );
                     }
                 });

         return Observable.concat(deposits, withdrawals).toList();
    }

    // requestLiveMarkets functions
    private Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>> iterableTickerFunction(){
        return new Function<List<BinancePriceTicker>, Iterable<BinancePriceTicker>>() {
            @Override
            public Iterable<BinancePriceTicker> apply(List<BinancePriceTicker> binancePriceTickers) throws Exception {
                Log.d("order", binancePriceTickers.size() + "");
                return binancePriceTickers;
            }
        };
    }

    // requestOrders functions
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
    private Function<BinancePriceTicker, ObservableSource<BinancePriceTicker>> tickerDelayFunction(){
        return new Function<BinancePriceTicker, ObservableSource<BinancePriceTicker>>() {
            @Override
            public ObservableSource<BinancePriceTicker> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                return Observable.just(binancePriceTicker).delay(DELAY_MILLIS, TimeUnit.MILLISECONDS);
            }
        };
    }
    private Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>> tickerToOrderFlatMap(){
        return new Function<BinancePriceTicker, ObservableSource<List<BinanceOrder>>>() {
            @Override
            public ObservableSource<List<BinanceOrder>> apply(BinancePriceTicker binancePriceTicker) throws Exception {
                return binanceService.getAllOrders(binancePriceTicker.getSymbol(), System.currentTimeMillis() - RECVWINDOWLAG, RECVWINDOW);
            }
        };
    }
    private Predicate<List<BinanceOrder>> orderSizeFilter(){
        return new Predicate<List<BinanceOrder>>() {
            @Override
            public boolean test(List<BinanceOrder> binanceOrders) throws Exception {
                return binanceOrders.size() > 0;
            }
        };
    }
    private Function<List<BinanceOrder>, Iterable<BinanceOrder>> flattenOrderList(){
        return new Function<List<BinanceOrder>, Iterable<BinanceOrder>>() {
            @Override
            public Iterable<BinanceOrder> apply(List<BinanceOrder> binanceOrders) throws Exception {
                return binanceOrders;
            }
        };
    }
    private Function<BinanceOrder, ObservableSource<BinanceOrder>> orderDelayFunction(){
        return new Function<BinanceOrder, ObservableSource<BinanceOrder>>() {
            @Override
            public ObservableSource<BinanceOrder> apply(BinanceOrder binanceOrder) throws Exception {
                return Observable.just(binanceOrder).delay(DELAY_MILLIS, TimeUnit.MILLISECONDS);
            }
        };
    }
    private Predicate<BinanceOrder> orderTypeFilter(){
        return new Predicate<BinanceOrder>() {
            @Override
            public boolean test(BinanceOrder binanceOrder) throws Exception {
                String status = binanceOrder.getStatus();
                return status.equals("FILLED") || status.equals("PARTIALLY_FILLED ");
            }
        };
    }
    private Function<BinanceOrder, ObservableSource<Pair<Double, Double>>> conversionRatesFunction(){
        return new Function<BinanceOrder, ObservableSource<Pair<Double, Double>>>() {
            @Override
            public ObservableSource<Pair<Double, Double>> apply(BinanceOrder binanceOrder) throws Exception {
                Long orderTime = Long.parseLong(binanceOrder.getTime());
                String startTime = KaleidoFunctions.convertMilliISO8601(orderTime);
                String endTime = KaleidoFunctions.convertMilliISO8601(orderTime+900000);
                String rawSymbol = binanceOrder.getSymbol();
                String guess = rawSymbol.substring(rawSymbol.length()-4);
                if(rawSymbol.equals("BTCUSDT")) {
                    return Observable.just(new Pair<>(1d,1d));
                }else if(rawSymbol.contains("USDT")){
                    String altBtcSymbol = rawSymbol.substring(0,rawSymbol.length()-4) + "BTC";
                    return binanceService.getHistoricPrice(altBtcSymbol, "1m", binanceOrder.getTime())
                            .map(mapCoinBtcInverseRate());
                }else if(rawSymbol.contains("BTC")){
                    return gdaxService.getHistoricBtc2Usd(startTime, endTime,60)
                            .map(mapGdaxBtcUsdRate());
                }else{
                    String altBtcSymbol = "";
                    for(String baseAlt : BaseAlts.binanceBaseAlts){
                        if(guess.contains(baseAlt)) altBtcSymbol = baseAlt + "BTC";
                    }
                    return gdaxService.getHistoricBtc2Usd(startTime,endTime,60)
                            .zipWith(binanceService.getHistoricPrice(altBtcSymbol, "1m", binanceOrder.getTime())
                                            .subscribeOn(Schedulers.io()),
                                    bifuncGdaxBinanceRates());
                }
            }
        };
    }
    private BiFunction<BinanceOrder, Pair<Double, Double>, KaleidoOrder> createKaleidoOrder(){
        return new BiFunction<BinanceOrder, Pair<Double, Double>, KaleidoOrder>() {
            @Override
            public KaleidoOrder apply(BinanceOrder binanceOrder, Pair<Double, Double> ratesPair) throws Exception {
                return new KaleidoOrder(binanceOrder, ratesPair.first, ratesPair.second);
            }
        };
    }
    private Function<JsonElement, Pair<Double, Double>> mapCoinBtcInverseRate(){
        return new Function<JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement binanceCandle) throws Exception {
                double inverseRate =binanceCandle.getAsJsonArray().get(0).getAsJsonArray().get(1).getAsDouble();
                return new Pair<>(1d, inverseRate);
            }
        };
    }
    private Function<JsonElement, Pair<Double, Double>> mapGdaxBtcUsdRate(){
        return new Function<JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement jsonElement) throws Exception {
                return new Pair<>(jsonElement.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble(), 1d);
            }
        };
    }
    private BiFunction<JsonElement, JsonElement, Pair<Double, Double>> bifuncGdaxBinanceRates(){
        return new BiFunction<JsonElement, JsonElement, Pair<Double, Double>>() {
            @Override
            public Pair<Double, Double> apply(JsonElement gdaxCandle, JsonElement binanceCandle) throws Exception {
                double gdaxBtcUsdRate = gdaxCandle.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble();
                double coinBtcRate = binanceCandle.getAsJsonArray().get(0).getAsJsonArray().get(1).getAsDouble();
                return new Pair<>(gdaxBtcUsdRate, coinBtcRate);
            }
        };
    }

    // requestBalance functions
    private Function<BinanceAccountInfo, List<BinanceBalance>> accountToBalanceMapper(){
        return new Function<BinanceAccountInfo, List<BinanceBalance>>() {
            @Override
            public List<BinanceBalance> apply(BinanceAccountInfo binanceAccountInfo) throws Exception {
                return binanceAccountInfo.getBalances();
            }
        };
    }
    private Function<List<BinanceBalance>, Iterable<BinanceBalance>> flattenBalances(){
        return new Function<List<BinanceBalance>, Iterable<BinanceBalance>>() {
            @Override
            public Iterable<BinanceBalance> apply(List<BinanceBalance> binanceBalances) throws Exception {
                return binanceBalances;
            }
        };
    }
    private Predicate<BinanceBalance> filterFreedBalances(){
        return new Predicate<BinanceBalance>() {
            @Override
            public boolean test(BinanceBalance binanceBalance) throws Exception {
                return Double.parseDouble(binanceBalance.getFree()) > 0;
            }
        };
    }
    private Function<BinanceBalance, KaleidoBalance> createKaleidoBalance(){
        return new Function<BinanceBalance, KaleidoBalance>() {
            @Override
            public KaleidoBalance apply(BinanceBalance binanceBalance) throws Exception {
                BalanceType balanceType = new BalanceType(binanceBalance.getAsset(), "binance", Double.parseDouble(binanceBalance.getFree()));
                return new KaleidoBalance(balanceType);
            }
        };
    }


}
