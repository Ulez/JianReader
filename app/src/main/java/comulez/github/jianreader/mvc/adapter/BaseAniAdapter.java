package comulez.github.jianreader.mvc.adapter;


import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import comulez.github.jianreader.mvc.bean.Book;

/**
 * Created by Ulez on 2017/2/25.
 * Email：lcy1532110757@gmail.com
 */

public abstract class BaseAniAdapter<T extends RecyclerView.ViewHolder> extends BaseAdapter<T> {
    public static final int ANI_RIGHT_IN = 0;
    public static final int ANI_LEFT_IN = 1;
    public static final int ANI_ALPHA_IN = 2;
    public static final int ANI_SCALE_IN = 3;
    public static final int ANI_BOTTOM_IN_ = 4;

    public boolean showEmpty = false;
    public boolean showAni = true;

    public int lastPos = 0;

    private OnItemClickListener<Book> onItemClickListener;

    private int aniType;
    private int duration;

    public void setAniType(int aniType, int duration) {
        this.aniType = aniType;
        this.duration = duration;
    }

    public boolean isShowAni() {
        return showAni;
    }

    public void setShowAni(boolean showAni) {
        this.showAni = showAni;
    }

    @Override
    public void onViewAttachedToWindow(T holder) {
        super.onViewAttachedToWindow(holder);

        Log.e("lcy", "lastPos=" + lastPos + ",,holder.getAdapterPosition()=" + holder.getAdapterPosition());

        if (showAni && !showEmpty && holder.getAdapterPosition() >= lastPos) {
            switch (aniType) {
                case ANI_RIGHT_IN:
                    ObjectAnimator.ofFloat(holder.itemView, "translationX", holder.itemView.getRootView().getWidth(), 0).setDuration(duration).start();
                    break;
                case ANI_LEFT_IN:
                    ObjectAnimator.ofFloat(holder.itemView, "translationX", -holder.itemView.getRootView().getWidth(), 0).setDuration(duration).start();
                    break;
                case ANI_ALPHA_IN:
                    ObjectAnimator.ofFloat(holder.itemView, "alpha", 0, 1).setDuration(500).start();
                    break;
                default:
                    break;
            }
        }
    }
}
