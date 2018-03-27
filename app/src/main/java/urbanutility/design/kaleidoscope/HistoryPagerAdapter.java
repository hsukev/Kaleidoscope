package urbanutility.design.kaleidoscope;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jerye on 3/23/2018.
 */

public class HistoryPagerAdapter extends PagerAdapter {
    private Context context;

    public HistoryPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        switch (position) {
            case 0:
//                View view0 = LayoutInflater.from(this.context).inflate(R.layout.history_orders_tab,container,false);
//                view0.setTag("orders_tab");
//                container.addView(view0);
//                Log.d("History", "addTab1");

                View view0 = container.findViewById(R.id.history_tab_orders);
//                view0.setTag("orders_tag");
                return view0;
            case 1:
//                View view1 = LayoutInflater.from(this.context).inflate(R.layout.history_exchanges_tab, container, false);
//                view1.setTag("exchanges_tab");
//                container.addView(view1);
//                Log.d("History", "addTab2");

                return container.findViewById(R.id.history_tab_exchanges);
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
        switch (position) {
            case 0:
                return "orders";
            case 1:
                return "exchanges";
            default:
                return super.getPageTitle(position);
        }

    }
}
