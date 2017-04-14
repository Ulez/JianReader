package comulez.github.jianreader.mvp.bookshelfFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvc.activity.BaseActivity;
import comulez.github.jianreader.mvc.activity.ChapterListActivity;
import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.adapter.BaseAniAdapter;
import comulez.github.jianreader.mvc.adapter.BookShelfAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.ReadBook;
import comulez.github.jianreader.mvp.EmptyLayout;


public class BookShelfFragment extends Fragment implements BookShelfView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rvBooks;
    private String mParam1;
    private String mParam2;
    private String TAG = "BookShelfFragment";
    private ArrayList<ReadBook> books = new ArrayList<>();
    private ArrayList<ReadBook> fenglei = new ArrayList<>();
    private BookShelfAdapter shelfAdapter;
    private BaseActivity activity;
    private ShelfPresenter presenter;

    public BookShelfFragment() {
        // Required empty public constructor
    }

    public static BookShelfFragment newInstance(String param1, String param2) {
        BookShelfFragment fragment = new BookShelfFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rvBooks = (RecyclerView) inflater.inflate(R.layout.rvbook, container, false);
        loadData();
        return rvBooks;
    }


    public void loadData() {
        rvBooks.setLayoutManager(new LinearLayoutManager(activity));
        shelfAdapter = new BookShelfAdapter(activity, books);
        rvBooks.setAdapter(shelfAdapter);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        shelfAdapter.setAniType(BaseAniAdapter.ANI_LEFT_IN, 500);
        shelfAdapter.setOnItemClickListener(new OnItemClickListener<ReadBook>() {
            @Override
            public void onItemClick(View view, int pos, ReadBook book) {
                startChapterActivity(view, pos, book);
            }
        });
        presenter = new ShelfPresenter();
        presenter.addTaskListener(this);
        presenter.getAllRead();
    }

    private void startChapterActivity(View view, int pos, ReadBook book) {
        Intent intent = new Intent(activity, ChapterListActivity.class);
        intent.putExtra(Constant.PART_URL, book.getBookUrl());
        intent.putExtra(Constant.COVER_URL, book.getImage_cover());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view.findViewById(R.id.iv_cover), ChapterListActivity.TRANSIT_IMG);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    @Override
    public void showList(ArrayList<ReadBook> books) {
        shelfAdapter.setData(books);
    }

    @Override
    public void onError(Throwable e, int Status) {
        switch (Status) {
            case Constant.status_loading:
                shelfAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.NETWORK_LOADING);
                break;
            case Constant.status_no_data:
                shelfAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.NODATA);
                break;
            default:
                shelfAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.HIDE_LAYOUT);
                break;
        }
    }
}
