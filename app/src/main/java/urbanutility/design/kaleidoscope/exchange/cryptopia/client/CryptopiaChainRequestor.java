package urbanutility.design.kaleidoscope.exchange.cryptopia.client;

import android.util.Pair;

import com.google.gson.JsonElement;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistory;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistoryResponse;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;
import urbanutility.design.kaleidoscope.view.KaleidoViewModel;

/**
 * Created by jerye on 1/13/2018.
 */

public class CryptopiaChainRequestor implements ChainRequestor{
    private String TAG = getClass().getName();
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private KaleidoViewModel kaleidoViewModel;
    private Retrofit.Builder retrofitBuilder;
    private CryptopiaService cryptopiaService;
    private GdaxService gdaxService;
    private String startTime_UTC;
    private String endTime_UTC;

    public CryptopiaChainRequestor(HistoryFragment historyFragment){
        this.retrofitBuilder = historyFragment.retrofitBuilder;
        this.kaleidoViewModel = historyFragment.kaleidoViewModel;

        cryptopiaService = retrofitBuilder.baseUrl("https://www.cryptopia.co.nz/api/")
                .build()
                .create(CryptopiaService.class);
    }

    @Override
    public void requestAndInsert() {
        getTradeHistory();
    }

    private void getTradeHistory() {

        final Single<CryptopiaTradeHistoryResponse> cryptopiaTradeHistorySingle = cryptopiaService.getTradeHistory();
        cryptopiaTradeHistorySingle
                .subscribeOn(Schedulers.io())
                .map(new Function<CryptopiaTradeHistoryResponse, List<CryptopiaTradeHistory>>() {
                    @Override
                    public List<CryptopiaTradeHistory> apply(CryptopiaTradeHistoryResponse response) throws Exception {
                        return response.getData();
                    }
                })
                .flattenAsObservable(new Function<List<CryptopiaTradeHistory>, Iterable<CryptopiaTradeHistory>>() {
                    @Override
                    public Iterable<CryptopiaTradeHistory> apply(List<CryptopiaTradeHistory> cryptopiaTradeHistory) throws Exception {
                        return cryptopiaTradeHistory;
                    }
                })
                .map(new Function<CryptopiaTradeHistory, CryptopiaTradeHistory>() {
                    @Override
                    public CryptopiaTradeHistory apply(CryptopiaTradeHistory cryptopiaTradeHistory) throws Exception {
                        String orderTime = cryptopiaTradeHistory.getTime();
                        startTime_UTC = KaleidoFunctions.addMilliISO8601(orderTime, -60);
                        endTime_UTC = KaleidoFunctions.addMilliISO8601(orderTime, -45);
                        return cryptopiaTradeHistory;
                    }
                })
                .flatMap(new Function<CryptopiaTradeHistory, ObservableSource<JsonElement>>() {
                    @Override
                    public ObservableSource<JsonElement> apply(CryptopiaTradeHistory cryptopiaTradeHistory) throws Exception {
                        return gdaxService.getHistoricBtc2Usd(startTime_UTC, endTime_UTC, 60);
                    }
                }, new BiFunction<CryptopiaTradeHistory, JsonElement, Pair<CryptopiaTradeHistory, Double>>() {
                    @Override
                    public Pair<CryptopiaTradeHistory, Double> apply(CryptopiaTradeHistory cryptopiaTradeHistory, JsonElement jsonElement) throws Exception {
                        Double btcUsdRate = jsonElement.getAsJsonArray().get(0).getAsJsonArray().get(4).getAsDouble();
                        return new Pair<>(cryptopiaTradeHistory, btcUsdRate);
                    }
                })
                .subscribe(new DisposableObserver<Pair<CryptopiaTradeHistory, Double>>() {
                    @Override
                    public void onNext(Pair<CryptopiaTradeHistory, Double> cryptopiaTradeHistoryDoublePair) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
