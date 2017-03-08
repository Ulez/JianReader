package comulez.github.jianreader.mvc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.BaseAniAdapter;
import comulez.github.jianreader.mvc.adapter.BookAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvc.bean.RankBean;
import comulez.github.jianreader.mvc.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChildBooksFragment extends Fragment {

    private String url;
    private static String TAG = "ChildBooksFragment";
    private ArrayList<RankBean> arrayList = new ArrayList<>();//排行list
    private ArrayList<Book> bookList = new ArrayList<>();//书本list
    private ArrayList<Book> parts;
    public Handler handler;
    private RecyclerView rvBooks;

    private BookAdapter bookAdapter;
    private LinearLayoutManager layoutManager;

    // TODO: Customize parameter argument names
    private static final String ARG_BEAN = "column-count";
    // TODO: Customize parameters
    private RankBean rankBean;
    private OnListFragmentInteractionListener mListener;
    private Context activity;
    private boolean isLoadingData = false;
    Runnable runnable;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChildBooksFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChildBooksFragment newInstance(RankBean rankBean) {
        ChildBooksFragment fragment = new ChildBooksFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BEAN, rankBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {
            rankBean = getArguments().getParcelable(ARG_BEAN);
        }
        url = Api.base_url + rankBean.getUrl();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    isLoadingData = true;
                    getData();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isLoadingData = false;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rvBooks = (RecyclerView) inflater.inflate(R.layout.rvbook, container, false);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setList();
            }
        };
        getDataaaaa();
        return rvBooks;
    }

    private void getDataaaaa() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }

    private void setList() {
        if (bookAdapter == null) {
            layoutManager = new LinearLayoutManager(activity);
            rvBooks.setLayoutManager(layoutManager);
            bookAdapter = new BookAdapter(activity, bookList);
            rvBooks.setAdapter(bookAdapter);
//                rvBooks.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
            rvBooks.setItemAnimator(new DefaultItemAnimator());
            bookAdapter.setAniType(BaseAniAdapter.ANI_LEFT_IN, 500);
            bookAdapter.setOnItemClickListener(new OnItemClickListener<Book>() {
                @Override
                public void onItemClick(View view, int pos, Book book) {
                    Toast.makeText(activity, book.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, ChapterListActivity.class);
                    intent.putExtra(Constant.PART_URL, book.getUrl());
                    activity.startActivity(intent);
                    Log.e("lcy", "bookUrl=" + book.getUrl());
                }
            });
            rvBooks.addOnScrollListener(new OnRcvScrollListener() {
                @Override
                public void onBottom() {
                    // 到底部自动加载
                    if (!isLoadingData) {
                        getDataaaaa();
                        isLoadingData = true;
                    }
                }
            });
        }
        bookAdapter.addData(parts);
    }

    private void getData() throws Exception {
        if (TextUtils.isEmpty(url) || !url.contains("http")) {
            Log.e(TAG, "不加载");
            return;
        }
        parts = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
//        Log.e(TAG, doc.toString());
        Elements ranks = doc.select("div.content");
        Elements els = ranks.select("li.prev");
        for (int i = 0; i < els.size(); i++) {
            Element target = els.get(i);
            arrayList.add(new RankBean(target.select("a").first().attr("href"), target.text()));
        }
        Elements cover = doc.select("div.cover");
        Elements books = cover.select("p.line");
        for (int i = 0; i < books.size(); i++) {
            Element bookEle = books.get(i);
            Elements book = bookEle.select("a.blue");
            String bookurl = book.select("a").first().attr("href");
            String bookname22 = book.text();
            String author22 = bookEle.select("a").last().text();
            parts.add(new Book(Api.base_url + bookurl, bookEle.text()));
        }
        Element page = doc.select("div.page").first();
        if (page != null) {
            Elements xx = page.select("a");
            for (int i = 0; i < xx.size(); i++) {
                if ("下页".equals(xx.get(i).text())) {
                    url = xx.get(i).attr("href");
                    break;
                }
            }
        } else {
            url = null;
        }
//        Log.e(TAG, "nextUrl=" + url);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            Log.e(TAG, "" + context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
