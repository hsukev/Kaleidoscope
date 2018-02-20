package urbanutility.design.kaleidoscope.client;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import urbanutility.design.kaleidoscope.KaleidoActivity;
import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 2/13/2018.
 */

public class KaleidoService implements PreferenceChangeListener{
    private SharedPreferences sharedPreferences;
    private ExchangePort exchangePort;
    private Set<String> exchangeSet = new HashSet<>();


    public KaleidoService(KaleidoActivity kaleidoActivity) {
        exchangeSet = kaleidoActivity.getPreferences(Context.MODE_PRIVATE).getStringSet("exchange",null );
        KaleidoClients kaleidoClients = new KaleidoClients();
        exchangePort = new ExchangePort(kaleidoClients);
    }

    public Single<List<LiveMarketType>> requestLiveMarkets() {
        return Single.zip(getLiveMarketSingles(), new Function<Object[], List<LiveMarketType>>() {
            @Override
            public List<LiveMarketType> apply(Object[] objects) throws Exception {
                List<LiveMarketType> list = new ArrayList<>();
                for (Object object : objects) {
                    list.addAll((List<LiveMarketType>) object);
                }
                return list;
            }
        });
    }

    public Single<List<KaleidoOrder>> requestOrders(){
        return Single.zip(getOrderSingles(), new Function<Object[], List<KaleidoOrder>>() {
            @Override
            public List<KaleidoOrder> apply(Object[] objects) throws Exception {
                List<KaleidoOrder> list = new ArrayList<>();
                for (Object object : objects) {
                    list.addAll((List<KaleidoOrder>) object);
                }
                return list;
            }
        });
    }

    public Single<List<KaleidoBalance>> requestBalances(){
        return null;
    }

    public Single<List<KaleidoDeposits>> requestDeposits(){
        return null;
    }

    // Build list of preferred exchanges from map of all support exchanges (filter by sharedpreferences)
    private List<Single<List<LiveMarketType>>> getLiveMarketSingles() {
        List<Single<List<LiveMarketType>>> list = new ArrayList<>();
        Map<String, Single<List<LiveMarketType>>> map = exchangePort.getLiveMarketMap();
        for(String exchange : exchangeSet){
            list.add(map.get(exchange));
        }
        return list;
    }

    private List<Single<List<KaleidoOrder>>> getOrderSingles() {
        List<Single<List<KaleidoOrder>>> list = new ArrayList<>();
        Map<String, Single<List<KaleidoOrder>>> map = exchangePort.getOrdersMap();
        for(String exchange : exchangeSet){
            list.add(map.get(exchange));
        }
        return list;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent preferenceChangeEvent) {
        if(preferenceChangeEvent.getKey().equals("exchange")){
//            exchangeSet = kaleidoActivity.getPreferences(Context.MODE_PRIVATE).getStringSet("exchange",null );
        }
    }
}