package app.com.appsamples.frag.BluetoothPager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.appsamples.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/27/2017.
 */

public class Home extends Fragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    Unbinder unbinder;

    String[] items = {"Normal", "LE"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bluetooth_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        setPager();

        return view;
    }

    void setPager() {

        tabs.setupWithViewPager(pager);
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager());
        pager.setAdapter(myAdapter);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new BluetoothTest();
            } else {
                return new BluetoothLETest();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items[position];
        }

        @Override
        public int getCount() {
            return items.length;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
