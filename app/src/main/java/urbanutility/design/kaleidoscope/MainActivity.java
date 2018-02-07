package urbanutility.design.kaleidoscope;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jerye on 1/4/2018.
 * 1 call with single list response -> call for each item of list
 * https://github.com/ReactiveX/RxJava/issues/1939
 * https://stackoverflow.com/questions/28035090/rxjava-fetch-every-item-on-the-list SOLUTION
 * <p>
 * CONVERSION CHART
 * http://www.vogella.com/tutorials/RxJava/article.html
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pager_title_strip)
    PagerTitleStrip pagerTitleStrip;

    String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        KaleidoFragmentStatePagerAdapter fragmentStatePagerAdapter = new KaleidoFragmentStatePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(fragmentStatePagerAdapter);


    }


    public class KaleidoFragmentStatePagerAdapter extends FragmentPagerAdapter {

        KaleidoFragmentStatePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
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
            switch (position){
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
}
