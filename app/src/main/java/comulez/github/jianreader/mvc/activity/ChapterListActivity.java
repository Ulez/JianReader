package comulez.github.jianreader.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.ChapterAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.BookDetail;
import comulez.github.jianreader.mvc.bean.Chapter;
import comulez.github.jianreader.mvc.read.ReadActivity;

public class ChapterListActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    private String TAG = "ChapterListActivity";
    private ArrayList<Chapter> chapterList = new ArrayList<>();
    public static final int Type_Chapters = 1;
    public static final int Type_Dec = 2;
    private CollapsingToolbarLayoutState state;

    private Handler handler;
    RecyclerView recyclerView;
    private String full_url;
    FloatingActionButton fb;
    LinearLayoutManager manager;
    private boolean top = true;
    private BookDetail detail;
    private CollapsingToolbarLayout collapsingToolbarLayout;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.camera);
        collapsingToolbarLayout.setTitleEnabled(false);

        toolbar.setOnMenuItemClickListener(this);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbarLayout.setTitle("斗破苍穹");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbarLayout.setTitle("斗破苍穹");
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                        }
                        collapsingToolbarLayout.setTitle("斗破苍穹");
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (top)
                    recyclerView.scrollToPosition(chapterList.size() - 1);
                else
                    recyclerView.scrollToPosition(0);
                top = !top;
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Type_Chapters:
                        ChapterAdapter adapter = new ChapterAdapter(activity, chapterList);
//                adapter.setAniType(BaseAniAdapter.ANI_LEFT_IN, 150);
                        manager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(manager);
//                recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener<Chapter>() {
                            @Override
                            public void onItemClick(View view, int pos, Chapter chapter) {
                                Log.e(TAG, chapter.getName() + ":" + full_url + chapter.getUrl());
                                Intent intent = new Intent(activity, ReadActivity.class);
                                intent.putExtra(Constant.PART_URL, full_url + chapter.getUrl());
                                activity.startActivity(intent);
                            }
                        });
                        break;
                    case Type_Dec:

                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData("ul.chapter", "li");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getData(String s1, String s2) throws IOException {
        full_url = getIntent().getStringExtra(Constant.PART_URL);
        if (!full_url.contains("book")) { //http://m.23us.com/html/58/58177/
            Document doc = Jsoup.connect(full_url).get();
            Elements elements = doc.select(s1);
            Elements chapters = elements.select(s2);
            for (int i = 0; i < chapters.size(); i++) {
                Element target = chapters.get(i);
                chapterList.add(new Chapter(target.select("a").first().attr("href"), target.text()));
            }
            handler.sendEmptyMessage(Type_Chapters);
        } else { //http://m.23us.com/book/58177;
            Document doc = Jsoup.connect(full_url).get();
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
            handler.sendEmptyMessage(Type_Chapters);
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
