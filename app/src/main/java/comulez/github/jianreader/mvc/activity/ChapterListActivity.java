package comulez.github.jianreader.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.adapter.ChapterAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.Chapter;
import comulez.github.jianreader.mvc.read.ReadActivity;

public class ChapterListActivity extends BaseActivity {

    private String TAG = "ChapterListActivity";
    private ArrayList<Chapter> chapterList = new ArrayList<>();

    private Handler handler;
    RecyclerView recyclerView;
    private String full_url;
    FloatingActionButton fb;
    LinearLayoutManager manager;
    private boolean top = true;

    @Override
    public int getResId() {
        return R.layout.activity_chapter_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.rv_chapters);
        fb = (FloatingActionButton) findViewById(R.id.fb);
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
        if (!full_url.contains("book")){ //http://m.23us.com/html/58/58177/
            Document doc = Jsoup.connect(full_url).get();
            Elements elements = doc.select(s1);
            Elements chapters = elements.select(s2);
            for (int i = 0; i < chapters.size(); i++) {
                Element target = chapters.get(i);
                chapterList.add(new Chapter(target.select("a").first().attr("href"), target.text()));
            }
            handler.sendEmptyMessage(1);
            Elements xx = doc.select("div.index_block");
        }else { //http://m.23us.com/book/58177;

        }
    }
}
