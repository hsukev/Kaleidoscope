package urbanutility.design.kaleidoscope.exchange.binance.client;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import urbanutility.design.kaleidoscope.security.HmacSigner;

/**
 * Created by jerye on 1/4/2018.
 */

public class BinanceRequestInterceptor implements Interceptor {
    private String mApiKey;
    private String mSecretKey;
    private String LOG = "HttpInterceptor";


    public BinanceRequestInterceptor(String apiKey, String secretKey){
        mApiKey = apiKey;
        mSecretKey = secretKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if(originalRequest.headers().size()==0) {
            Log.d("HttpInterceptorNoHeader", originalRequest.toString());
            return chain.proceed(originalRequest)
                ;}
//        Log.d("HttpInterceptor", originalRequest.headers().toString() );
//        Log.d("HttpInterceptor", originalRequest.headers().name(0));
        Request.Builder newBuilder = originalRequest.newBuilder();

        boolean isApiKeyRequired = originalRequest.headers().name(0).equals(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY);
        boolean isSignedRequired = originalRequest.headers().name(0).equals(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED);

        newBuilder.removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED).removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY);

        if(isApiKeyRequired || isSignedRequired){
            newBuilder.addHeader(BinanceApiConstants.BINANCE_API_KEY_HEADER, mApiKey);
            if(isSignedRequired){
                String query = originalRequest.url().query();
                String signedKey = HmacSigner.signSHA256(query, mSecretKey);
//                Log.d(LOG, signedKey);
                HttpUrl signedURL = originalRequest.url().newBuilder().addQueryParameter("signature", signedKey).build();
                newBuilder.url(signedURL);
            }
        }

        Request request = newBuilder.build();
//        Log.d(LOG, request.headers().toString());
        Log.d("HttpInterceptorNew", request.toString());
        return chain.proceed(request);
    }
}
