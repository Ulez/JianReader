package comulez.github.jianreader.mvc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.bean.ReadBook;
import comulez.github.jianreader.mvp.EmptyLayout;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public class BookShelfAdapter extends BaseAniAdapter<RecyclerView.ViewHolder> {
    public static final int Type_empty = 221;
    Context context;
    ArrayList<ReadBook> bookList;
    private OnItemClickListener<ReadBook> onItemClickListener;
    private OnEmptyClickListener onEmptyClickListener;

    public void setOnEmptyClickListener(OnEmptyClickListener onEmptyClickListener) {
        this.onEmptyClickListener = onEmptyClickListener;
    }

    private OnItemLongClickListener<ReadBook> onItemLongClickListener;
    private String TAG = "BookShelfAdapter";
    private int emId;
    private int emType;

    public void setOnItemClickListener(OnItemClickListener<ReadBook> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<ReadBook> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BookShelfAdapter(Context context, ArrayList<ReadBook> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookShelfAdapter.HotHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_book, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (bookList == null || bookList.size() <= 0)
            return;
        switch (getItemViewType(position)) {
            case Type_empty:
                break;
            default:
                ((BookShelfAdapter.HotHolder) holder).bookName.setText(bookList.get(position).getBookName());
                ((BookShelfAdapter.HotHolder) holder).author.setText(bookList.get(position).getAuthor());
                Picasso.with(context)
                        .load(bookList.get(position).getImage_cover())
                        .error(R.drawable.defaultmin)
                        .fit()
                        .centerCrop()
                        .into(((BookShelfAdapter.HotHolder) holder).cover);
                break;
        }
        lastPos = Math.max(lastPos, position);
    }

    @Override
    public int getItemCount() {
        if (showEmpty)
            return 1;
        return bookList.size();
    }

    public void addData(ArrayList<ReadBook> parts) {
        int start = bookList.size();
        bookList.addAll(parts);
        int end = bookList.size();
        notifyDataSetChanged();
    }

    public void clearData() {
        bookList.clear();
    }

    public void setData(ArrayList<ReadBook> books) {
        this.bookList = books;
        setShowAni(true);
        notifyItemRangeInserted(0, bookList.size());
    }

    public void setEmptyView(int layout_id, int statusType) {
        if (statusType == EmptyLayout.HIDE_LAYOUT) {
            this.emId = layout_id;
            this.emType = statusType;
            showEmpty = false;
            notifyDataSetChanged();
        } else {
            this.emId = layout_id;
            this.emType = statusType;
            showEmpty = true;
            bookList.clear();
            notifyDataSetChanged();
        }
    }

    public class EmptyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public EmptyHolder(View itemView) {
            super(itemView);
            if (itemView instanceof EmptyLayout) {
                ((EmptyLayout) itemView).setErrorType(emType);
                ((EmptyLayout) itemView).setOnLayoutClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (onEmptyClickListener != null) {
                onEmptyClickListener.onEmptyClick();
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (bookList != null && bookList.size() > 0 && lastPos == bookList.size() - 1)
            setShowAni(false);
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView bookName;
        public TextView author;

        public BookHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition(), bookList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null)
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition(), bookList.get(getAdapterPosition()));
//            Log.e(TAG, "onLongClick=" + getAdapterPosition());
            return true;
        }
    }


    public class HotHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView bookName;
        public TextView author;
        public TextView dec;
        public ImageView cover;

        public HotHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.tv_name);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            cover = (ImageView) itemView.findViewById(R.id.iv_cover);
            dec = (TextView) itemView.findViewById(R.id.wv_dec);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition(), bookList.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null)
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition(), bookList.get(getAdapterPosition()));
//            Log.e(TAG, "onLongClick=" + getAdapterPosition());
            return true;
        }
    }
}
