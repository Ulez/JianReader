package comulez.github.jianreader.mvc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.bean.Chapter;

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

    public void setOnItemClickListener(OnItemClickListener<Chapter> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Chapter> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public ChapterAdapter(Context context, ArrayList<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }


    @Override
    public ChapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ChapterHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterHolder holder, int position) {
        holder.textView.setText(chapterList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;

        public ChapterHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_book_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
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
