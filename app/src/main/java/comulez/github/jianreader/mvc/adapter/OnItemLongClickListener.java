package comulez.github.jianreader.mvc.adapter;

import android.view.View;

/**
 * Created by Ulez on 2017/2/24.
 * Email：lcy1532110757@gmail.com
 */

public interface OnItemLongClickListener<K> {
    void onItemLongClick(View view, int pos, K k);
}
