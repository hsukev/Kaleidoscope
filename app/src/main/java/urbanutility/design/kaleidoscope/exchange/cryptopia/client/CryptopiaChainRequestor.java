package urbanutility.design.kaleidoscope.exchange.cryptopia.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import urbanutility.design.kaleidoscope.ChainRequestor;
import urbanutility.design.kaleidoscope.HistoryFragment;
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

    }
}
