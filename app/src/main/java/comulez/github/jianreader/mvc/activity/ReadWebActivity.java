package comulez.github.jianreader.mvc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import comulez.github.jianreader.R;

public class ReadWebActivity extends AppCompatActivity {
    private WebView webView;
    private String TAGkkk="RanksActivity";
    private RelativeLayout relativeLayout;
    private String BaiDu="http://m.baidu.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_web);
        webView = (WebView) findViewById(R.id.wb);
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new ReadWebViewClient());
        webView.loadUrl(BaiDu);
    }
}
