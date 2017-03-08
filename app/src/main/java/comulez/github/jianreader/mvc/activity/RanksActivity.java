package comulez.github.jianreader.mvc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.bean.RankBean;

public class RanksActivity extends BaseActivity {
    private static String TAG = "RanksActivity";
    private ArrayList<RankBean> rankList = new ArrayList<>();//排行list
    public Handler handler;
    private ViewPager pager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter adapter;
    private ArrayList<ChildBooksFragment> mTabContents = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                for (RankBean bean : rankList) {
                    mTabContents.add(ChildBooksFragment.newInstance(bean));
                    titles.add(bean.getRankName());
                    Log.e(TAG, bean.toString());
                }
                adapter = new MyFragmentPagerAdapter(activity.getSupportFragmentManager(), activity);
                pager.setAdapter(adapter);
                tabLayout.setupWithViewPager(pager);
                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getData() throws IOException {
        Document doc = Jsoup.connect(Api.all_visit_url).get();
        Elements ranks = doc.select("div.content");
        Elements els = ranks.select("li.prev");
        for (int i = 0; i < els.size(); i++) {
            Element target = els.get(i);
            rankList.add(new RankBean(target.select("a").first().attr("href"), target.text()));
        }
        handler.sendEmptyMessage(1);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mTabContents.get(position);
        }

        @Override
        public int getCount() {
            return mTabContents.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
