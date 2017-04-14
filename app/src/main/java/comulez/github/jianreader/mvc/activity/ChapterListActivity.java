package comulez.github.jianreader.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.ChapterAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.BookDetail;
import comulez.github.jianreader.mvc.bean.Chapter;
import comulez.github.jianreader.mvc.read.ReadActivity;
import comulez.github.jianreader.mvp.utils.CacheDao;
import comulez.github.jianreader.mvp.utils.Utils;

public class ChapterListActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    private String TAG = "ChapterListActivity";
    private ArrayList<Chapter> chapterList = new ArrayList<>();
    public static final int Type_Chapters = 1;
    public static final int Type_Dec = 2;
    private CollapsingToolbarLayoutState state;

    private Handler handler;
    RecyclerView recyclerView;
    private String originUrl;
    private String nextUrl;
    FloatingActionButton fb;
    LinearLayoutManager manager;
    private boolean top = true;
    private BookDetail detail;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivCover;
    private TextView tvName;
    private TextView tvAuthor;
    private WebView wvDec;
    private Runnable getChapterTask;
    private Runnable getDetailTask;
    private int taskCount = 0;
    private ExecutorService executor;
    public static final String TRANSIT_IMG = "image";
    private TextView tvType;
    private TextView tvStatus;
    private TextView tvLastChapter;
    private TextView tvLastupdate;
    private ChapterAdapter chapterAdapter;
    private ImageView ivAdd;
    private boolean added = false;
    private boolean gotDetail = false;
    private String bookUrl;

    @Override
    public int getResId() {
        return R.layout.activity_chapter_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        recyclerView = (RecyclerView) findViewById(R.id.rv_chapters);
        fb = (FloatingActionButton) findViewById(R.id.fb);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAuthor = (TextView) findViewById(R.id.tv_author);

        tvType = (TextView) findViewById(R.id.tv_type);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvLastChapter = (TextView) findViewById(R.id.tv_lastChapter);
        tvLastupdate = (TextView) findViewById(R.id.tv_lastupdate);

        wvDec = (WebView) findViewById(R.id.wv_dec);

        ViewCompat.setTransitionName(ivCover, TRANSIT_IMG);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(this);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                try {
                    setToobar(appBarLayout, verticalOffset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapterAdapter.reverseChapters();
//                if (top)
//                    recyclerView.scrollToPosition(chapterList.size() - 1);
//                else
//                    recyclerView.scrollToPosition(0);
                top = !top;
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Type_Chapters:
                        chapterAdapter = new ChapterAdapter(activity, chapterList, originUrl);
                        manager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(chapterAdapter);
                        chapterAdapter.setOnItemClickListener(new OnItemClickListener<Chapter>() {
                            @Override
                            public void onItemClick(View view, int pos, Chapter chapter) {
                                Log.e(TAG, chapter.getName() + ":" + originUrl + chapter.getUrl());
                                Intent intent = new Intent(activity, ReadActivity.class);
                                intent.putExtra(Constant.PART_URL, originUrl + chapter.getUrl());
                                intent.putExtra(Constant.BOOK_NAME, tvName.getText().toString());
                                activity.startActivity(intent);
                            }
                        });
                        break;
                    case Type_Dec:
                        setDetails();
                        break;
                }
            }
        };
        executor = Executors.newSingleThreadExecutor();
        originUrl = getIntent().getStringExtra(Constant.PART_URL);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.COVER_URL))) {
            Picasso.with(this)
                    .load(getIntent().getStringExtra(Constant.COVER_URL))
                    .error(R.drawable.defaultmin)
                    .fit()
                    .centerCrop()
                    .into(ivCover);
        } else {
            ivCover.setImageResource(R.drawable.progressloading);
        }
        executor.execute(getTask(originUrl));
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gotDetail) {
                    if (!added && CacheDao.getmInstance().addToBookSHELF(detail, "", bookUrl, "", "", "")) {
                        Utils.t(R.string.add);
                        ivAdd.setImageResource(R.drawable.had);
                    } else {
                        if (CacheDao.getmInstance().removeBook(bookUrl)) {
                            Utils.t(R.string.remove);
                            ivAdd.setImageResource(R.drawable.add2);
                        }
                    }
                    added = !added;
                }
            }
        });
    }

    private void setToobar(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (state != CollapsingToolbarLayoutState.EXPANDED) {
                state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                getSupportActionBar().setTitle(getString(R.string.back));
//                        collapsingToolbarLayout.setTitle("斗破苍穹");//设置title为EXPANDED
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                getSupportActionBar().setTitle(detail.getBookName());
//                        collapsingToolbarLayout.setTitle("斗破苍穹");
                state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
            }
        } else {
            if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                }
                state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chapterAdapter != null)
            chapterAdapter.notifyDataSetChanged();
    }

    private Runnable getTask(final String url) {
        if (!url.contains("book")) { //http://m.23us.com/html/58/58177/
            getChapterTask = new Runnable() {

                @Override
                public void run() {
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements elements = doc.select("ul.chapter");
                        Elements chapters = elements.select("li");
                        for (int i = 0; i < chapters.size(); i++) {
                            Element target = chapters.get(i);
                            chapterList.add(new Chapter(target.select("a").first().attr("href"), target.text()));
                        }
                        handler.sendEmptyMessage(Type_Chapters);

                        nextUrl = doc.select("div.cover").first().select("div.index_block").first().select("p").last().select("a").first().attr("href");

                        taskCount++;
                        if (taskCount == 1) {
                            executor.execute(getTask(nextUrl));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            return getChapterTask;
        } else { //http://m.23us.com/book/58177;
            bookUrl = url;
            getDetailTask = new Runnable() {

                @Override
                public void run() {
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements elements = doc.select("div.block");
                        Elements x1 = elements.select("div.block_img2");
                        String image_cover = x1.select("img").first().attr("src");
                        Elements xxxxx = elements.select("div.block_txt2");
                        String bookName = xxxxx.select("h1").first().text();
                        String author = xxxxx.select("p").get(2).text();
                        String type = xxxxx.select("p").get(3).text();
                        String status = xxxxx.select("p").get(4).text();
                        String up_date = xxxxx.select("p").get(5).text();
                        String latestChapter = xxxxx.select("p").get(6).text();
                        String latestUrl = xxxxx.select("p").get(6).select("a").first().attr("href");

                        Elements intro = doc.select("div.intro_info");
                        detail = new BookDetail(bookName, author, type, status, up_date, latestChapter, latestUrl, intro.toString(), image_cover);
                        handler.sendEmptyMessage(Type_Dec);

                        nextUrl = doc.select("div.more").select("a").first().attr("href");

                        taskCount++;
                        if (taskCount == 1) {
                            executor.execute(getTask(nextUrl));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            return getDetailTask;
        }
    }

    private void setDetails() {
        gotDetail = true;
        added = CacheDao.getmInstance().getAdded(bookUrl);
        if (added) {
            ivAdd.setImageResource(R.drawable.had);
        } else {
            ivAdd.setImageResource(R.drawable.add2);
        }
        tvName.setText(detail.getBookName());
        tvAuthor.setText(detail.getAuthor());
        tvLastChapter.setText(detail.getLatestChapter());
        tvStatus.setText(detail.getStatus());
        tvType.setText(detail.getType());
        tvLastupdate.setText(detail.getUp_date());
        detail.getLatestUrl();

        wvDec.loadDataWithBaseURL("about:blank", "<font color='white'>" + detail.getIntro(), "text/html", "utf-8", null);
        wvDec.getSettings().setTextZoom(40);
        wvDec.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (TextUtils.isEmpty(getIntent().getStringExtra(Constant.COVER_URL))) {
            Picasso.with(this)
                    .load(detail.getImage_cover())
                    .error(R.drawable.defaultmin)
                    .fit()
                    .centerCrop()
                    .into(ivCover);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String msg = "";
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                msg += "Click setting";
                break;
        }
        if (!msg.equals("")) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
