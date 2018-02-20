package urbanutility.design.kaleidoscope.view;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoBaseCurrency;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoCalculator;
import urbanutility.design.kaleidoscope.utility.Triplet;

/**
 * Created by jerye on 1/9/2018.
 */

public class KaleidoViewModel extends AndroidViewModel {
    private MutableLiveData<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>> tripletMutableLiveData;
    private MutableLiveData<Integer> live1, live2, live3;
    private KaleidoDatabase kaleidoDatabase;
    private Retrofit.Builder retrofitBuilder;

    public KaleidoViewModel(Application application){
        super(application);
        kaleidoDatabase = KaleidoDatabase.getAppDatabase(this.getApplication());
    }

    //use async
    public LiveData<List<KaleidoOrder>> getAllOrders(){
        Log.d("Viewpager", "getAllOrders");
        return kaleidoDatabase.kaleidoDao().getAllOrders();
    }

    public LiveData<List<KaleidoBalance>> getAllBalances(){
        Log.d("Viewpager", "getAllOrders");
        return kaleidoDatabase.kaleidoDao().getAllBalances();
    }

    public KaleidoBalance getBalance(String symbol){
        return kaleidoDatabase.kaleidoDao().getBalance(symbol);
    }

    public void insertOrder(List<KaleidoOrder> kaleidoOrder){
        kaleidoDatabase.kaleidoDao().insertOrder(kaleidoOrder);
    }

    public void insertBalance(KaleidoBalance... kaleidoBalance){
        kaleidoDatabase.kaleidoDao().insertBalance(kaleidoBalance);
    }

    public MutableLiveData<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>> getTripletMutableLiveData(){
        if (tripletMutableLiveData==null){
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

    public void setTripletBalances(List<KaleidoBalance> balances){
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> test = getTripletMutableLiveData().getValue();
        test.setFirst(balances);
        getTripletMutableLiveData().setValue(test);
    }

    public void setTripletOrders(List<KaleidoOrder> orders){
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> test = getTripletMutableLiveData().getValue();
        test.setSecond(orders);
        getTripletMutableLiveData().setValue(test);
    }

    public void setTripletMarkets(List<LiveMarketType> markets){
        Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> test = getTripletMutableLiveData().getValue();
        test.setThird(markets);
        getTripletMutableLiveData().setValue(test);
    }

    public LiveData<List<KaleidoBaseCurrency>> getBaseCurrency(){
        return Transformations.map(getTripletMutableLiveData(), new Function<Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>>, List<KaleidoBaseCurrency>>() {
            @Override
            public List<KaleidoBaseCurrency> apply(Triplet<List<KaleidoBalance>, List<KaleidoOrder>, List<LiveMarketType>> input) {
                Log.d("viewmodel", input.getFirst().size() + "," + input.getSecond().size()+","+input.getThird().size());
                List<KaleidoBaseCurrency> baseCurrencyTypes = new ArrayList<>();
                baseCurrencyTypes.add(new KaleidoBaseCurrency("USD"));
                baseCurrencyTypes.add(new KaleidoBaseCurrency("BTC"));
                return KaleidoCalculator.CalculatePosition(input.getFirst(), input.getSecond(), baseCurrencyTypes, input.getThird());
            }
        });
    }

}
