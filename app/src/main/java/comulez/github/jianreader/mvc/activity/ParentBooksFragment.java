package comulez.github.jianreader.mvc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.MyFragmentPagerAdapter;
import comulez.github.jianreader.mvc.bean.RankBean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ParentBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentBooksFragment extends Fragment {

    private static String TAG = "RanksActivity";
    private ArrayList<RankBean> rankList = new ArrayList<>();//排行list
    public Handler handler;
    private ViewPager pager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter adapter;
    private ArrayList<ChildBooksFragment> mTabContents = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mUrl;
    private String mParam2;


    public ParentBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url    Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParentBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentBooksFragment newInstance(String url, String param2) {
        ParentBooksFragment fragment = new ParentBooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_rank, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                for (RankBean bean : rankList) {
                    mTabContents.add(ChildBooksFragment.newInstance(bean));
                    titles.add(bean.getRankName());
                    Log.e(TAG, bean.toString());
                }
                adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mTabContents, titles);
                pager.setAdapter(adapter);
                tabLayout.setupWithViewPager(pager);
                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                pager.setOffscreenPageLimit(rankList.size());
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
        return view;
    }

    private void getData() throws IOException {
        Document doc = Jsoup.connect(mUrl).get();
        Elements ranks = doc.select("div.content");
        Elements els = ranks.select("li.prev");
        for (int i = 0; i < els.size(); i++) {
            Element target = els.get(i);
            rankList.add(new RankBean(target.select("a").first().attr("href"), target.text()));
        }
        handler.sendEmptyMessage(1);
    }

//    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
//        private Context context;
//
//        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
//            super(fm);
//            this.context = context;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mTabContents.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mTabContents.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
