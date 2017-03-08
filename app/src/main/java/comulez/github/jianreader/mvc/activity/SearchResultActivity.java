package comulez.github.jianreader.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.BaseAniAdapter;
import comulez.github.jianreader.mvc.adapter.BookAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.Book;

public class SearchResultActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private Runnable runnable;
    private SearchView searchView;
    private RecyclerView rvBooks;
    private String TAG = "SearchResultActivity";
    private String bookQuery;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Book> part;
    private Handler handler;
    private BookAdapter bookAdapter;
    private LinearLayoutManager layoutManager;
    private ExecutorService executor;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        bookQuery = getIntent().getStringExtra(Constant.QUERY);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        setSupportActionBar(toolbar);
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executor.execute(runnable);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setList();
            }
        };
    }

    private void setList() {
        if (bookAdapter == null) {
            layoutManager = new LinearLayoutManager(activity);
            rvBooks.setLayoutManager(layoutManager);
            bookAdapter = new BookAdapter(activity, books);
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
        }
        bookAdapter.clearData();
        bookAdapter.addData(part);
    }

    private void getData() throws Exception {
        part = new ArrayList<>();
//        Document doc = Jsoup.connect("http://m.23us.com/modules/article/search.php?searchtype=articlename&searchkey=%B6%B7%C6%C6&submit=").post();
        url = "http://m.23us.com/modules/article/search.php?searchtype=articlename&searchkey=" + URLEncoder.encode(bookQuery, "GBK") + "&submit=";
        Document doc = Jsoup.parse(new URL(url).openStream(), "GBK", url);
//        Document doc = Jsoup.connect("http://m.23us.com/modules/article/search.php")
//                .data("searchtype", "articlename")
//                .data("searchkey", URLEncoder.encode("斗破", "UTF-16"))
//                .post();
        Log.e(TAG, "doc=" + doc.toString());
        Elements books = doc.select("div.cover");
        for (Element element : books) {
            for (Element element222 : element.select("p.line")) {
                Elements book = element222.select("a.blue");
                String bookUrl = book.select("a").first().attr("href");
                String bookName = book.text();
                String author = element222.select("a").last().text();
                part.add(new Book(Api.base_url + bookUrl, null, bookName, null, author));
            }
        }
        handler.sendEmptyMessage(1);
    }

    @Override
    public int getResId() {
        return R.layout.activity_search_result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem menuItem = menu.findItem(R.id.sv_search);//
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);//加载searchview
        searchView.setOnQueryTextListener(this);//为搜索框设置监听事件
//        searchView.setSubmitButtonEnabled(true);//设置是否显示搜索按钮
        searchView.setQueryHint("搜书");//设置提示信息
        searchView.setIconifiedByDefault(true);//设置搜索默认为图标
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "onQueryTextSubmit,query=" + query);
        bookQuery = query.trim();
        if (!TextUtils.isEmpty(query.trim())) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("query", query.trim());
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e(TAG, "onQueryTextChange,newText=" + newText);
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent");
        executor.execute(runnable);
    }
}
