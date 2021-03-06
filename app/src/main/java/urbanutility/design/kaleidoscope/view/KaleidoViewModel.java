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
import urbanutility.design.kaleidoscope.model.KaleidoLiveMarket;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.model.KaleidoPosition;
import urbanutility.design.kaleidoscope.utility.Doublet;
import urbanutility.design.kaleidoscope.utility.KaleidoCalculator2;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.utility.Triplet;

/**
 * Created by jerye on 1/9/2018.
 */

public class KaleidoViewModel extends AndroidViewModel {
    private String TAG = getClass().getName();
    private MutableLiveData<Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>>> tripletMutableLiveData;
    private MutableLiveData<Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>>> doubletMutableLiveData;
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

    public LiveData<List<KaleidoBalance>> getFilteredBalances(String exchange){
        return kaleidoDatabase.kaleidoDao().getFilteredBalances(exchange);
    }

    public LiveData<List<KaleidoDeposits>> getAllDeposits() {
        return kaleidoDatabase.kaleidoDao().getAllDeposits();
    }

    // TODO: Wrap publisher with error handling class. Error is not propogated due to subscription bypass using LiveData
    public LiveData<List<KaleidoLiveMarket>> getAllLiveMarkets(KaleidoService kaleidoService) {
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

    private MutableLiveData<Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>>> getTripletMutableLiveData() {
        if (tripletMutableLiveData == null) {
            tripletMutableLiveData = new MutableLiveData<>();
            tripletMutableLiveData.setValue(
                    new Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>>(
                            new ArrayList<KaleidoDeposits>(),
                            new ArrayList<KaleidoOrder>(),
                            new ArrayList<KaleidoLiveMarket>())
            );
        }
        return tripletMutableLiveData;
    }

    private MutableLiveData<Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>>> getDoubletMutableLiveData() {
        if (doubletMutableLiveData == null) {
            doubletMutableLiveData = new MutableLiveData<>();
            doubletMutableLiveData.setValue(
                    new Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>>(
                            new ArrayList<KaleidoBalance>(),
                            new ArrayList<KaleidoLiveMarket>()
                    )
            );
        }
        return doubletMutableLiveData;
    }

    public void setTripletDeposits(List<KaleidoDeposits> deposits) {
        Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>> triplet = getTripletMutableLiveData().getValue();
        triplet.setFirst(deposits);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setTripletOrders(List<KaleidoOrder> orders) {
        Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>> triplet = getTripletMutableLiveData().getValue();
        triplet.setSecond(orders);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setTripletMarkets(List<KaleidoLiveMarket> markets) {
        Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>> triplet = getTripletMutableLiveData().getValue();
        triplet.setThird(markets);
        getTripletMutableLiveData().setValue(triplet);
    }

    public void setPairBalances(List<KaleidoBalance> balances) {
        Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>> pair = getDoubletMutableLiveData().getValue();
        pair.setFirst(balances);
        getDoubletMutableLiveData().setValue(pair);
    }

    public void setPairMarkets(List<KaleidoLiveMarket> markets) {
        Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>> pair = getDoubletMutableLiveData().getValue();
        pair.setSecond(markets);
        getDoubletMutableLiveData().setValue(pair);
    }

    //Transformation of balance, order, liveMarket data into List of Base Currencies for display (realized gain etc.)
    public LiveData<Iterable<KaleidoPosition>> getBaseCurrency() {
        return Transformations.map(getTripletMutableLiveData(), new Function<Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>>, Iterable<KaleidoPosition>>() {
            @Override
            public Iterable<KaleidoPosition> apply(Triplet<List<KaleidoDeposits>, List<KaleidoOrder>, List<KaleidoLiveMarket>> input) {
                return KaleidoCalculator2.CalculatePositions(input.getFirst(), input.getSecond(), input.getThird());
            }
        });
    }

    // Transformation of Balance and LiveMarket data into Map of Exchanges and each of their total asset
    public LiveData<Map<String, Double>> getBalanceMapInBtc(final boolean isSummed) {
        return Transformations.map(getDoubletMutableLiveData(), new Function<Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>>, Map<String, Double>>() {
            @Override
            public Map<String, Double> apply(Doublet<List<KaleidoBalance>, List<KaleidoLiveMarket>> input) {
                List<KaleidoBalance> balances = input.getFirst();
                List<KaleidoLiveMarket> liveMarkets = input.getSecond();

                Map<String, Double> balanceMap = new HashMap<>();
                Map<String, Double> exchangeBalanceMap = new HashMap<>();
                //dump balance in map
                for (KaleidoBalance balance : balances) {
                    balanceMap.put(balance.getId(), balance.balanceType.amount);
                    Log.d(TAG, balance.balanceType.exchange);
                }

                //convert all balances to BTC
                for (KaleidoLiveMarket liveMarket : liveMarkets) {
                    String symbolPair = liveMarket.getSymbol();

                    //filter out nonBTC pairs
                    if (symbolPair.contains("BTC")) {
                        String matchBalanceId = KaleidoFunctions.createLiveMarketId(liveMarket);
                        //convert only if map contains
                        if (balanceMap.containsKey(matchBalanceId)) {
                            double btcPrice = balanceMap.get(matchBalanceId) * liveMarket.getPrice();
                            String exchangeName = liveMarket.getExchange();
                            // return this if requested
                            balanceMap.put(matchBalanceId, btcPrice);
                            // sum up balance by exchange name
                            if (exchangeBalanceMap.containsKey(exchangeName)) {
                                btcPrice += exchangeBalanceMap.get(exchangeName);
                            }
                            exchangeBalanceMap.put(exchangeName, btcPrice);
                        }
                    }
                }

                if(!isSummed){
                    // map of balances
                    return balanceMap;
                }else{
                    // return map of exchanges with corresponding total asset
                    return exchangeBalanceMap;
                }
            }
        });
    }
}
