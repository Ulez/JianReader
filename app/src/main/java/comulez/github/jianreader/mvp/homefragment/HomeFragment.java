package comulez.github.jianreader.mvp.homefragment;

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
import comulez.github.jianreader.mvc.activity.Api;
import comulez.github.jianreader.mvc.activity.BaseActivity;
import comulez.github.jianreader.mvc.activity.ChapterListActivity;
import comulez.github.jianreader.mvc.activity.Constant;
import comulez.github.jianreader.mvc.adapter.BaseAniAdapter;
import comulez.github.jianreader.mvc.adapter.BookAdapter;
import comulez.github.jianreader.mvc.adapter.OnItemClickListener;
import comulez.github.jianreader.mvc.bean.Book;
import comulez.github.jianreader.mvp.EmptyLayout;


public class HomeFragment extends Fragment implements HomeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rvBooks;
    private String mParam1;
    private String mParam2;
    private String TAG = "HomeFragment";
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Book> fenglei = new ArrayList<>();
    private BookAdapter bookAdapter;
    private BaseActivity activity;
    private HomePresenter presenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        bookAdapter = new BookAdapter(activity, books);
        rvBooks.setAdapter(bookAdapter);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        bookAdapter.setAniType(BaseAniAdapter.ANI_LEFT_IN, 500);
        bookAdapter.setOnItemClickListener(new OnItemClickListener<Book>() {
            @Override
            public void onItemClick(View view, int pos, Book book) {
                startChapterActivity(view, pos, book);
//                Toast.makeText(activity, book.getName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(activity, ChapterListActivity.class);
//                intent.putExtra(Constant.PART_URL, book.getUrl());
//                activity.startActivity(intent);
            }
        });

        presenter = new HomePresenter();
        presenter.addTaskListener(this);
        presenter.getData(Api.base_url);
    }

    private void startChapterActivity(View view, int pos, Book book) {
        Intent intent = new Intent(activity, ChapterListActivity.class);
        intent.putExtra(Constant.PART_URL, book.getUrl());
        if (book.getType() == Book.Type_Hot) {
            intent.putExtra(Constant.COVER_URL, book.getImage_url());
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view.findViewById(R.id.iv_cover), ChapterListActivity.TRANSIT_IMG);
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public void showList(ArrayList<Book> books) {
        bookAdapter.setData(books);
    }

    @Override
    public void onError(Throwable e, int Status) {
        switch (Status) {
            case Constant.status_loading:
                bookAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.NETWORK_LOADING);
//                Toast.makeText(activity, "status_loading", Toast.LENGTH_SHORT).show();
                break;
            case Constant.status_no_data:
                bookAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.NODATA);
//                Toast.makeText(activity, "status_no_data", Toast.LENGTH_SHORT).show();
                break;
            default://
                bookAdapter.setEmptyView(R.layout.em_layout, EmptyLayout.HIDE_LAYOUT);
//                Toast.makeText(activity, "default", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
