package comulez.github.jianreader.mvc.read;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Ulez on 2017/2/26.
 * Email：lcy1532110757@gmail.com
 */

public class MyScrollView extends ScrollView {
    private OnMove onMove;
    private String TAG = "Lcy";

    public void setOnMove(OnMove onMove) {
        this.onMove = onMove;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    int downX;
    int downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();
                if (Math.abs(currentX - downX) > Math.abs(currentY - downY)) {
                    if (currentX - downX > 50 && onMove != null) {
                        onMove.rightMove();
                        return true;
                    } else if (currentX - downX < -50 && onMove != null) {
                        onMove.leftMove();
                        return true;
                    }
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
//        return intercepted;
        return super.onInterceptTouchEvent(event);
    }

    public interface OnMove {
        void rightMove();

        void leftMove();
    }
}
