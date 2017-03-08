package comulez.github.jianreader.mvc.activity;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Ulez on 2017/2/23.
 * Email：lcy1532110757@gmail.com
 */

public class ReadWebViewClient extends WebViewClient {
    private String TAGkkk = "ReadWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.e(TAGkkk, "url=" + request.getUrl());
        Log.e(TAGkkk, "RequestHeaders=" + request.getRequestHeaders());
        Log.e(TAGkkk, "Method=" + request.getMethod());
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e(TAGkkk, "url=" + url);
        return super.shouldOverrideUrlLoading(view, url);
    }
}
