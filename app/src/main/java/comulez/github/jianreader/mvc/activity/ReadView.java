package comulez.github.jianreader.mvc.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Ulez on 2017/2/26.
 * Email：lcy1532110757@gmail.com
 */

public class ReadView extends WebView {

    public ReadView(Context context) {
        this(context, null);
    }

    public ReadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ReadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


}
