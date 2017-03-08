package comulez.github.jianreader.mvc.read;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import comulez.github.jianreader.mvc.activity.Api;
import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.activity.ReadView;
import comulez.github.jianreader.mvc.activity.BaseActivity;

public class ReadActivity extends BaseActivity implements View.OnClickListener, MyScrollView.OnMove {

    private ReadView webView;
    private TextView tv_name;
    TextView textViewnext;
    TextView textViewpre;
    TextView textViewnext1;
    TextView textViewpre1;
    private TextView tv_night;

    MyScrollView scrollView;
    private Handler handler;
    private String content;
    private String name;
    private String pre_url;
    private String next_url;
    private String TAG = "ReadActivity";
    String url;
    private boolean isNight = false;
    private boolean clickAble = true;
    private String cloreeee = "black";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.md_black);
        url = getIntent().getStringExtra(Constant.PART_URL);

        webView = (ReadView) findViewById(R.id.wb_read);
        tv_name = (TextView) findViewById(R.id.tv_name);
        scrollView = (MyScrollView) findViewById(R.id.activity_read);
        textViewnext = (TextView) findViewById(R.id.tv_next);
        textViewpre = (TextView) findViewById(R.id.tv_pre);
        textViewpre1 = (TextView) findViewById(R.id.tv_pre1);
        textViewnext1 = (TextView) findViewById(R.id.tv_next1);
        tv_night = (TextView) findViewById(R.id.tv_night);

        textViewnext.setOnClickListener(this);
        textViewpre.setOnClickListener(this);
        textViewnext1.setOnClickListener(this);
        textViewpre1.setOnClickListener(this);
        tv_night.setOnClickListener(this);

        scrollView.setOnMove(this);
        webView.setBackgroundColor(getResources().getColor(R.color.read_bg));
        scrollView.setBackgroundColor(getResources().getColor(R.color.read_bg));
        GetChapterContent();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                webView.loadDataWithBaseURL("about:blank", "<font color='" + cloreeee + "'>" + content, "text/html", "utf-8", null);
                tv_name.setText(name);
                scrollView.scrollTo(0, 0);
                clickAble = true;
            }
        };
    }

    private void setStatusColor(int color) {
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(color));
    }

    private void GetChapterContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getData(String url) throws IOException {
        Log.e(TAG, url);
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("div.txt");
        Elements title = doc.select("div.content");
        Element prEle = title.select("li.mulu").first();
        Elements nextEle = title.select("li.next");
        pre_url = Api.base_url + prEle.select("a").first().attr("href");
        next_url = Api.base_url + nextEle.select("a").first().attr("href");
        name = title.select("h1").first().text();
        content = elements.toString();
        handler.sendEmptyMessage(1);
    }

    @Override
    public int getResId() {
        return R.layout.activity_read;
    }

    @Override
    public void onClick(View v) {
        if (!clickAble)
            return;
        switch (v.getId()) {
            case R.id.tv_pre:
            case R.id.tv_pre1:
                goPre();
                break;
            case R.id.tv_next:
            case R.id.tv_next1:
                goNext();
                break;
            case R.id.tv_night:
                changeTheme();
                break;
        }

    }

    private void changeTheme() {
        isNight = !isNight;
        if (isNight) {
            setStatusColor(R.color.md_black);
            webView.loadDataWithBaseURL("about:blank", "<font color='gray'>" + content, "text/html", "utf-8", null);
            webView.getSettings();
            webView.setBackgroundColor(Color.BLACK);
            scrollView.setBackgroundColor(Color.BLACK);

            tv_name.setTextColor(Color.GRAY);
            textViewnext.setTextColor(Color.GRAY);
            textViewpre.setTextColor(Color.GRAY);
            textViewnext1.setTextColor(Color.GRAY);
            textViewpre1.setTextColor(Color.GRAY);
            tv_night.setTextColor(Color.GRAY);
            tv_night.setText("night");
            cloreeee = "gray";
        } else {
            setStatusColor(R.color.md_black);
            webView.loadDataWithBaseURL("about:blank", "<font color='black'>" + content, "text/html", "utf-8", null);
            webView.getSettings();
            webView.setBackgroundColor(getResources().getColor(R.color.read_bg));
            scrollView.setBackgroundColor(getResources().getColor(R.color.read_bg));

            tv_name.setTextColor(getResources().getColor(R.color.md_black));
            textViewnext.setTextColor(getResources().getColor(R.color.md_black));
            textViewpre.setTextColor(getResources().getColor(R.color.md_black));
            textViewnext1.setTextColor(getResources().getColor(R.color.md_black));
            textViewpre1.setTextColor(getResources().getColor(R.color.md_black));
            tv_night.setTextColor(getResources().getColor(R.color.md_black));
            tv_night.setText("day");
            cloreeee = "black";
        }
    }

    @Override
    public void rightMove() {
        goPre();
    }

    @Override
    public void leftMove() {
        goNext();
    }

    private void goNext() {
        clickAble = false;
        if (next_url.contains(".html")) {
            url = next_url;
            GetChapterContent();
        } else {
            finish();
        }
        Toast.makeText(activity, "下一章", Toast.LENGTH_SHORT).show();
    }

    private void goPre() {
        clickAble = false;
        if (pre_url.contains(".html")) {
            url = pre_url;
            GetChapterContent();
        } else {
            finish();
        }
        Toast.makeText(activity, "上一章", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
