package urbanutility.design.kaleidoscope.view;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoBaseCurrency;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.Doublet;
import urbanutility.design.kaleidoscope.utility.KaleidoCalculator;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.utility.Triplet;

/**
 * Created by jerye on 1/9/2018.
 */

public class KaleidoViewModel extends AndroidViewModel {
    private MutableLiveData<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>> tripletMutableLiveData;
    private MutableLiveData<Doublet<List<KaleidoBalance>, List<LiveMarketType>>> doubletMutableLiveData;
    private KaleidoDatabase kaleidoDatabase;

    public KaleidoViewModel(Application application) {
        super(application);
        kaleidoDatabase = KaleidoDatabase.getAppDatabase(this.getApplication());
    }

    //use async
    public LiveData<List<KaleidoOrder>> getAllOrders() {
        Log.d("Viewpager", "getAllOrders");
        return kaleidoDatabase.kaleidoDao().getAllOrders();
    }

    public LiveData<List<KaleidoBalance>> getAllBalances() {
        Log.d("Viewpager", "getAllOrders");
        return kaleidoDatabase.kaleidoDao().getAllBalances();
    }


    public LiveData<List<KaleidoDeposits>> getAllDeposits() {
        return kaleidoDatabase.kaleidoDao().getAllDeposits();
    }

    public LiveData<List<LiveMarketType>> getAllLiveMarkets(KaleidoService kaleidoService) {
        return LiveDataReactiveStreams.fromPublisher(kaleidoService.requestLiveMarkets().toFlowable()
                .delay(5000, TimeUnit.MILLISECONDS).repeat());
    }

    public void insertOrder(List<KaleidoOrder> kaleidoOrders) {
        kaleidoDatabase.kaleidoDao().insertOrder(kaleidoOrders);
    }

    public void insertBalance(List<KaleidoBalance> kaleidoBalances) {
        kaleidoDatabase.kaleidoDao().insertBalance(kaleidoBalances);
    }

    public void insertDeposit(List<KaleidoDeposits> kaleidoDeposit) {
        kaleidoDatabase.kaleidoDao().insertDeposit(kaleidoDeposit);
    }

    private MutableLiveData<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>> getTripletMutableLiveData() {
        if (tripletMutableLiveData == null) {
            tripletMutableLiveData = new MutableLiveData<>();
            tripletMutableLiveData.setValue(
                    new Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>(
                            new ArrayList<KaleidoBalance>(),
                            new ArrayList<KaleidoOrder>(),
                            new ArrayList<LiveMarketType>())
            );
        }
        return tripletMutableLiveData;
    }

    private MutableLiveData<Doublet<List<KaleidoBalance>, List<LiveMarketType>>> getDoubletMutableLiveData() {
        if (doubletMutableLiveData == null) {
            doubletMutableLiveData = new MutableLiveData<>();
            doubletMutableLiveData.setValue(
                    new Doublet<List<KaleidoBalance>, List<LiveMarketType>>(
                            new ArrayList<KaleidoBalance>(),
                            new ArrayList<LiveMarketType>()
                    )
            );
        }
        return doubletMutableLiveData;
    }

    public void setTripletBalances(List<KaleidoBalance> balances) {
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> triplet = getTripletMutableLiveData().getValue();
        triplet.setFirst(balances);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setTripletOrders(List<KaleidoOrder> orders) {
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> triplet = getTripletMutableLiveData().getValue();
        triplet.setSecond(orders);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setTripletMarkets(List<LiveMarketType> markets) {
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> triplet = getTripletMutableLiveData().getValue();
        triplet.setThird(markets);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setPairBalances(List<KaleidoBalance> balances) {
        Doublet<List<KaleidoBalance>, List<LiveMarketType>> pair = getDoubletMutableLiveData().getValue();
        pair.setFirst(balances);
        getDoubletMutableLiveData().setValue(pair);
    }

    public void setPairMarkets(List<LiveMarketType> markets) {
        Doublet<List<KaleidoBalance>, List<LiveMarketType>> pair = getDoubletMutableLiveData().getValue();
        pair.setSecond(markets);
        getDoubletMutableLiveData().setValue(pair);
    }

    //Transformation of balance, order, liveMarket data into List of Base Currencies for display (realized gain etc.)
    public LiveData<List<KaleidoBaseCurrency>> getBaseCurrency() {
        return Transformations.map(getTripletMutableLiveData(), new Function<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>, List<KaleidoBaseCurrency>>() {
            @Override
            public List<KaleidoBaseCurrency> apply(Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> input) {
                Log.d("viewmodel", input.getFirst().size() + "," + input.getSecond().size() + "," + input.getThird().size());
                List<KaleidoBaseCurrency> baseCurrencyTypes = new ArrayList<>();
                baseCurrencyTypes.add(new KaleidoBaseCurrency("USD"));
                baseCurrencyTypes.add(new KaleidoBaseCurrency("BTC"));
                return KaleidoCalculator.CalculatePosition(input.getFirst(), input.getSecond(), baseCurrencyTypes, input.getThird());
            }
        });
    }

    // Transformation of Balance and LiveMarket data into Map of Exchanges and each of their total asset
    public LiveData<Map<String, Double>> getBalanceMapInBtc() {
        return Transformations.map(getDoubletMutableLiveData(), new Function<Doublet<List<KaleidoBalance>, List<LiveMarketType>>, Map<String, Double>>() {
            @Override
            public Map<String, Double> apply(Doublet<List<KaleidoBalance>, List<LiveMarketType>> input) {
                List<KaleidoBalance> balances = input.getFirst();
                List<LiveMarketType> liveMarkets = input.getSecond();

                Map<String, Double> balanceMap = new HashMap<>();
                Map<String, Double> exchangeBalanceMap = new HashMap<>();
                //dump balance in map
                for (KaleidoBalance balance : balances) {
                    balanceMap.put(balance.getId(), balance.balanceType.amount);
                }

                //convert all balances to BTC
                for (LiveMarketType liveMarket : liveMarkets) {
                    String symbolPair = liveMarket.symbol;

                    //filter out nonBTC pairs
                    if (symbolPair.contains("BTC")) {
                        String matchBalanceId = KaleidoFunctions.createLiveMarketId(liveMarket);

                        //convert only of map contains
                        if (balanceMap.containsKey(matchBalanceId)) {
                            double btcPrice = balanceMap.get(matchBalanceId) * liveMarket.price;
                            String exchangeName = liveMarket.exchange;

                            // sum up balance by exchange name
                            if (exchangeBalanceMap.containsKey(exchangeName)) {
                                btcPrice += exchangeBalanceMap.get(exchangeName);
                            }
                            exchangeBalanceMap.put(exchangeName, btcPrice);
                        }
                    }
                }

                // return map of exchanges with corresponding total asset
                return exchangeBalanceMap;
            }
        });
    }


}
