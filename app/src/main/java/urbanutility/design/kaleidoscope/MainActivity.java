package urbanutility.design.kaleidoscope;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.client.BittrexRequestInterceptor;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.model.BinanceOrder;
import urbanutility.design.kaleidoscope.model.BinanceServerTime;

public class MainActivity extends AppCompatActivity {

    String LOG = "MainActivity";

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.binance.com");

        BittrexRequestInterceptor bittrexRequestInterceptor = new BittrexRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(bittrexRequestInterceptor);
        retrofitBuilder.client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        final KaleidoService kaleidoService = retrofit.create(KaleidoService.class);

        Observable<BinanceServerTime> serverTimeCallback = kaleidoService.getServerTime();
        serverTimeCallback
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<BinanceServerTime, ObservableSource<List<BinanceOrder>>>() {
                    @Override
                    public ObservableSource<List<BinanceOrder>> apply(BinanceServerTime binanceServerTime) throws Exception {
                        return kaleidoService.getAllOrders("NEOBTC", binanceServerTime.getServerTime());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<BinanceOrder>>() {
                    @Override
                    public void onNext(List<BinanceOrder> binanceOrders) {
                        for(BinanceOrder order: binanceOrders){
                            Log.d(LOG, order.getOrigQty());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });








    }
}
