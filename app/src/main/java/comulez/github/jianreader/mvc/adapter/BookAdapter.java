package comulez.github.jianreader.mvc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvp.EmptyLayout;

/**
 * Created by Ulez on 2017/2/23.
 * Email：lcy1532110757@gmail.com
 */

public class BookAdapter extends BaseAniAdapter<RecyclerView.ViewHolder> {
    public static final int Type_empty = 221;
    Context context;
    ArrayList<Book> bookList;
    private OnItemClickListener<Book> onItemClickListener;
    private OnEmptyClickListener onEmptyClickListener;

    public void setOnEmptyClickListener(OnEmptyClickListener onEmptyClickListener) {
        this.onEmptyClickListener = onEmptyClickListener;
    }

    private OnItemLongClickListener<Book> onItemLongClickListener;
    private String TAG = "BookAdapter";
    private int emId;
    private int emType;

    public void setOnItemClickListener(OnItemClickListener<Book> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Book> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Type_empty:
                return new EmptyHolder(LayoutInflater.from(context).inflate(R.layout.em_layout, parent, false));
            case Book.Type_Hot:
                return new HotHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_book, parent, false));
            case Book.Type_sort:
                return new SortHolder(LayoutInflater.from(context).inflate(R.layout.item_sort, parent, false));
            default:
                return new BookHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showEmpty)
            return Type_empty;
        return bookList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Book.Type_Hot:
                ((HotHolder) holder).bookName.setText(bookList.get(position).getName());
                ((HotHolder) holder).author.setText(bookList.get(position).getAuthor());
                ((HotHolder) holder).dec.setText(bookList.get(position).getDec());
                Glide.with(context)
                        .load(bookList.get(position).getImage_url())
                        .placeholder(R.drawable.default_cover)
                        .into(((HotHolder) holder).cover);
                break;
            case Book.Type_sort:
                ((SortHolder) holder).sort.setText(bookList.get(position).getName().replace("更多...", ""));
                break;
            case Type_empty:
                break;
            default:
                ((BookHolder) holder).bookName.setText(bookList.get(position).getName());
                ((BookHolder) holder).author.setText(bookList.get(position).getAuthor());
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

    public void addData(ArrayList<Book> parts) {
        int start = bookList.size();
        bookList.addAll(parts);
        int end = bookList.size();
        notifyDataSetChanged();
    }

    public void clearData() {
        bookList.clear();
    }

    public void setData(ArrayList<Book> books) {
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
            dec = (TextView) itemView.findViewById(R.id.tv_dec);
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

    public class SortHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView sort;

        public SortHolder(View itemView) {
            super(itemView);
            sort = (TextView) itemView.findViewById(R.id.tv_sort);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
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
            return true;
        }
    }
}
