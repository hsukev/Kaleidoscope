package urbanutility.design.kaleidoscope;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import urbanutility.design.kaleidoscope.model.KaleidoOrder;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, KaleidoActivity.class);
        startActivity(intent);
        finish();
    }
}
