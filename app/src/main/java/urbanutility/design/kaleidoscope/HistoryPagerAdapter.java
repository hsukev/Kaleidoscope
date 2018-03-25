package urbanutility.design.kaleidoscope;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jerye on 3/23/2018.
 */

public class HistoryPagerAdapter extends PagerAdapter {
    private Context context;

    public HistoryPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        switch (position) {
            case 0:
                View view0 = LayoutInflater.from(this.context).inflate(R.layout.history_orders_tab,container,false);
                container.addView(view0);
                return view0;
            case 1:
                View view1 = LayoutInflater.from(this.context).inflate(R.layout.history_exchanges_tab, container, false);
                container.addView(view1);
                return view1;
            default:
                return super.instantiateItem(container, position);
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "orders";
            case 1:
                return "exchanges";
            default:
                return super.getPageTitle(position);
        }

    }
}
