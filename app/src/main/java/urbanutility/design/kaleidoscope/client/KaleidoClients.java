package urbanutility.design.kaleidoscope.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.BuildConfig;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceRequestInterceptor;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.security.CustomTrust;

/**
 * Created by jerye on 2/17/2018.
 */

public class KaleidoClients {
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder httpClient;

    public KaleidoClients(){
        buildRetrofitServices();
    }

    private void buildRetrofitServices() {
        CustomTrust customTrust = new CustomTrust();
        httpClient = new OkHttpClient.Builder()
                .sslSocketFactory(customTrust.getSslSocketFactory(), customTrust.getTrustManager());
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public GdaxService getGdaxService(){
        return retrofitBuilder.baseUrl("https://api.gdax.com/")
                .client(httpClient.build())
                .build()
                .create(GdaxService.class);
    }

    public BinanceService getBinanceService(){
        BinanceRequestInterceptor binanceRequestInterceptor = new BinanceRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(binanceRequestInterceptor);
        return retrofitBuilder.baseUrl("https://api.binance.com")
                .client(httpClient.build())
                .build()
                .create(BinanceService.class);
    }
}
