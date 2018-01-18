package urbanutility.design.kaleidoscope.exchange.cryptopia.client;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistory;
import urbanutility.design.kaleidoscope.exchange.cryptopia.model.CryptopiaTradeHistoryResponse;
import urbanutility.design.kaleidoscope.model.KaleidoBalance;
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
                .map(new Function<CryptopiaTradeHistory, KaleidoBalance>() {
                    @Override
                    public KaleidoBalance apply(CryptopiaTradeHistory binanceBalance) throws Exception {
                        BalanceType balanceType = new BalanceType(binanceBalance.getAsset(), "binance", Double.parseDouble(binanceBalance.getFree()));
                        return new KaleidoBalance(String.valueOf(System.currentTimeMillis()), balanceType);
                    }
                })
                .subscribe(new DisposableObserver<KaleidoBalance>() {
                    @Override
                    public void onNext(KaleidoBalance kaleidoBalance) {
                        insertBalanceTable(kaleidoBalance);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("balance", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
