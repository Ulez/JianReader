package comulez.github.jianreader.mvc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;

import comulez.github.jianreader.mvc.activity.ChildBooksFragment;

/**
 * Created by eado on 2017/3/6.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<ChildBooksFragment> listFragments;
    private ArrayList<String> titles;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<ChildBooksFragment> al, ArrayList<String> titles) {
        super(fm);
        listFragments = al;
        this.titles = titles;
    }

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    //这个注意要重写；
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (position <= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }
}
