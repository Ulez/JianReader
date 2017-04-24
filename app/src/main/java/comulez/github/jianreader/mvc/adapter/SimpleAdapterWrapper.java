package comulez.github.jianreader.mvc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import comulez.github.jianreader.mvp.utils.Utils;

/**
 * Created by Ulez on 2017/4/24.
 * Email：lcy1532110757@gmail.com
 */


public class SimpleAdapterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    enum ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    }

    private SimpleAdapter mAdapter;
    private View mHeaderView;
    private View mFooterView;

    public SimpleAdapterWrapper(SimpleAdapter simpleAdapter) {
        this.mAdapter = simpleAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            return new BaseHolder(mHeaderView);
        } else if (viewType == ITEM_TYPE.FOOTER.ordinal()) {
            return new BaseHolder(mFooterView);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            return;
        } else if (position == mAdapter.getItemCount() + 1) {
            return;
        } else {
            mAdapter.onBindViewHolder((BaseHolder) holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        Utils.e("size==" + (mAdapter.getItemCount() + 2));
        return mAdapter.getItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.HEADER.ordinal();
        } else if (position == mAdapter.getItemCount() + 1) {
            return ITEM_TYPE.FOOTER.ordinal();
        } else {
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    public void setHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    public void setFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    public void setNewData(List<T> newDatas) {
        mAdapter.setNewData(newDatas);
        notifyDataSetChanged();
    }
}
