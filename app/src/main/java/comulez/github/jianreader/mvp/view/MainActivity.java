package comulez.github.jianreader.mvp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvp.presenter.MainPresenter2;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private MainPresenter2 presenter;
    private TextView mShowTxt;
    private TextView mUn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mShowTxt = (TextView) findViewById(R.id.tv_dec);
        mUn = (TextView) findViewById(R.id.tv_un);
        mUn.setOnClickListener(this);
        loadDatas();
    }

    public void loadDatas() {
        presenter = new MainPresenter2();
        presenter.addTaskListener(this);
        presenter.getData();
    }

    @Override
    public void onShowData(String str) {
        mShowTxt.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_un:
                if (presenter != null)
                    presenter.unSubscribe();
                break;
        }
    }
}
