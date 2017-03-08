package comulez.github.jianreader.mvc.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;

/**
 * Created by Ulez on 2017/2/24.
 * Email：lcy1532110757@gmail.com
 */

public class MyAnimation extends SimpleItemAnimator {

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.e("lcy","fromX="+fromX+",fromY"+fromY+",toX="+toX+",toY="+toY);
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public void runPendingAnimations() {

    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
