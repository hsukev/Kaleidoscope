package urbanutility.design.kaleidoscope;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import urbanutility.design.kaleidoscope.model.APIKeysObject;

import static org.junit.Assert.assertEquals;

/**
 * Created by jerye on 6/3/2018.
 */

@RunWith(AndroidJUnit4.class)
public class SharedPreferenceTest {
    private SharedPreferences sharedPreferences;

    @Before
    public void gsonSetInsertion(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Set<String> gsonSet = new HashSet<>();
        Gson gson = new Gson();
        String json = gson.toJson(new APIKeysObject("test", 1234L, "private", "public"));
        gsonSet.add(json);
        gsonSet.add("test2");
        sharedPreferences = appContext.getSharedPreferences("exchange",Context.MODE_PRIVATE);
        appContext.getSharedPreferences("exchange",Context.MODE_PRIVATE).edit().putStringSet("exchangeSet",gsonSet).apply();

    }

    @Test
    public void gsonSetCheck(){
        Set<String> set = sharedPreferences.getStringSet("exchangeSet", new HashSet<String>());
        for(String s: set){
            assertEquals("test", s);
        }
    }

    @After
    public void after() {
        sharedPreferences.edit().putString("exchangeSet", null).apply();
    }
}
