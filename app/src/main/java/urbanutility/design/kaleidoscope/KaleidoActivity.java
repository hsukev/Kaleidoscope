package urbanutility.design.kaleidoscope;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import urbanutility.design.kaleidoscope.client.KaleidoService;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceRequestInterceptor;
import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceService;
import urbanutility.design.kaleidoscope.exchange.gdax.client.GdaxService;
import urbanutility.design.kaleidoscope.model.KaleidoInterface;
import urbanutility.design.kaleidoscope.security.CustomTrust;

/**
 * Created by jerye on 1/4/2018.
 * 1 call with single list response -> call for each item of list
 * https://github.com/ReactiveX/RxJava/issues/1939
 * https://stackoverflow.com/questions/28035090/rxjava-fetch-every-item-on-the-list SOLUTION
 * <p>
 * CONVERSION CHART
 * http://www.vogella.com/tutorials/RxJava/article.html
 */

public class KaleidoActivity extends AppCompatActivity implements KaleidoInterface{
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pager_title_strip)
    PagerTitleStrip pagerTitleStrip;

    String TAG = "MainActivity";
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder httpClient;
    private KaleidoService kaleidoService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        buildRetrofitServices();
        kaleidoService = new KaleidoService(this);

        KaleidoFragmentStatePagerAdapter fragmentStatePagerAdapter = new KaleidoFragmentStatePagerAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(fragmentStatePagerAdapter);



    }

    public class KaleidoFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        KaleidoFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CurrentPriceFragment.newInstance();
                case 1:
                    return ComparisonFragment.newInstance();
                case 2:
                    return HistoryFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current";
                case 1:
                    return "Analysis";
                case 2:
                    return "Import";
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void buildRetrofitServices() {
        CustomTrust customTrust = new CustomTrust();
        httpClient = new OkHttpClient.Builder()
                .sslSocketFactory(customTrust.getSslSocketFactory(), customTrust.getTrustManager());
        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Override
    public GdaxService getGdaxService(){
        return retrofitBuilder.baseUrl("https://api.gdax.com/")
                .client(httpClient.build())
                .build()
                .create(GdaxService.class);
    }

    @Override
    public BinanceService getBinanceService(){
        BinanceRequestInterceptor binanceRequestInterceptor = new BinanceRequestInterceptor(BuildConfig.BINANCE_API_KEY, BuildConfig.BINANCE_SECRET_KEY);
        httpClient.addInterceptor(binanceRequestInterceptor);
        return retrofitBuilder.baseUrl("https://api.binance.com")
                .client(httpClient.build())
                .build()
                .create(BinanceService.class);
    }

    public KaleidoService getKaleidoService(){
        return kaleidoService;
    }
}
