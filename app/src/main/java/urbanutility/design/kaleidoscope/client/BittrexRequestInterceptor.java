package urbanutility.design.kaleidoscope.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jerye on 1/4/2018.
 */

public class BittrexRequestInterceptor implements Interceptor {


    public BittrexRequestInterceptor(String apiKey){

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newRequest = request.newBuilder();
        newRequest.addHeader()

        return null;
    }
}
