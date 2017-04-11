package comulez.github.jianreader.mvc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.bean.Chapter;
import comulez.github.jianreader.mvp.utils.CacheManager;
import comulez.github.jianreader.mvp.utils.Utils;

/**
 * Created by Ulez on 2017/2/25.
 * Email：lcy1532110757@gmail.com
 */

public class ChapterAdapter extends BaseAniAdapter<ChapterAdapter.ChapterHolder> {
    Context context;
    ArrayList<Chapter> chapterList;
    private OnItemClickListener<Chapter> onItemClickListener;
    private OnItemLongClickListener<Chapter> onItemLongClickListener;
    private String TAG = "ChapterAdapter";
    private String originUrl;

    public void setOnItemClickListener(OnItemClickListener<Chapter> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Chapter> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public ChapterAdapter(Context context, ArrayList<Chapter> chapterList, String originUrl) {
        this.context = context;
        this.chapterList = chapterList;
        this.originUrl = originUrl;
    }


    @Override
    public ChapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ChapterHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterHolder holder, int position) {
        holder.textView.setText(chapterList.get(position).getName());
        Log.e(TAG, "url:" + originUrl + chapterList.get(position).getUrl());
        if (TextUtils.isEmpty(CacheManager.getCacheManager().getChapterContent(originUrl + chapterList.get(position).getUrl()))) {
            holder.imageView.setImageResource(R.drawable.nocache);
        } else {
            holder.imageView.setImageResource(R.drawable.hascache);
        }
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public void reverseChapters() {
        Collections.reverse(chapterList);
        this.notifyDataSetChanged();
    }

    public class ChapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public ImageView imageView;

        public ChapterHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_book_name);
            imageView = (ImageView) itemView.findViewById(R.id.iv_cache);
            imageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cache:
                    Utils.t("缓存xxsfsdfsf");
                    return;
            }
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition(), chapterList.get(getAdapterPosition()));
            Log.e(TAG, "onClick=" + chapterList.get(getAdapterPosition()).toString());
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null)
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition(), chapterList.get(getAdapterPosition()));
            Log.e(TAG, "onLongClick=" + getAdapterPosition());
            return true;
        }
    }
}
