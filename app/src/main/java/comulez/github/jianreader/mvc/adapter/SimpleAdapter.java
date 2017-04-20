package comulez.github.jianreader.mvc.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comulez.github.jianreader.R;

/**
 * Created by Ulez on 2017/4/18.
 * Email：lcy1532110757@gmail.com
 */


public abstract class SimpleAdapter<T, K extends SimpleAdapter.BaseHolder> extends RecyclerView.Adapter<K> {
    public List<T> mDatas;
    private int layoutResId;
    protected Context mContext;

    public SimpleAdapter(List<T> mDatas, int layoutResId) {
        this.mDatas = mDatas;
        this.layoutResId = layoutResId;
    }

    public SimpleAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    public SimpleAdapter(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    protected abstract void convert(K holder, T item, int position);

    protected abstract int getLayoutId(int viewType);

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if (layoutResId != 0) {
            return (K) BaseHolder.get(parent, layoutResId);
        }
        return (K) BaseHolder.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    public void setNewData(List<T> newDatas) {
        this.mDatas = newDatas;
        notifyDataSetChanged();
    }


    public static class BaseHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mContentView;
        Context context;

        public BaseHolder(View itemView) {
            super(itemView);
            mContentView = itemView;
            mViews = new SparseArray<>();
            context = itemView.getContext();
        }

        public static BaseHolder get(ViewGroup parent, int layoutResId) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
            return new BaseHolder(view);
        }


        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mContentView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int id, String str) {
            TextView xxx = getView(id);
            if (xxx != null) {
                xxx.setText(str);
            }
        }

        public void setImage(int id, @DrawableRes int resId) {
            ImageView imageView = getView(id);
            imageView.setImageResource(resId);
        }


        public void setImageByPicasso(int id, String url) {
            ImageView imageView = getView(id);
            Picasso.with(context)
                    .load(url)
                    .error(R.drawable.defaultmin)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }

        public BaseHolder setOnClickListener(int viewId, View.OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
            return this;
        }
        public BaseHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
            View view = getView(viewId);
            view.setOnLongClickListener(listener);
            return this;
        }
    }
}
