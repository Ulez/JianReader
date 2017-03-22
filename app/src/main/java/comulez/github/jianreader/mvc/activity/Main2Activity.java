package comulez.github.jianreader.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;

import comulez.github.jianreader.R;
import comulez.github.jianreader.mvp.homefragment.HomeFragment;

public class Main2Activity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private FragmentManager fManager;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private ParentBooksFragment parentBooksFragment;
    private ParentBooksFragment parentCatelogFragment;
    private Fragment mCurrentFragment;
    private Handler handler;
    private SearchView searchView;
    private String TAG = "Main2Activity";

    @Override
    public int getResId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeFragment = HomeFragment.newInstance("", "");

        fManager = getSupportFragmentManager();
        transaction = fManager.beginTransaction();
        transaction.replace(R.id.content_main, homeFragment).commitAllowingStateLoss();
        mCurrentFragment = homeFragment;
        getSupportActionBar().setTitle(getString(R.string.home));
        navigationView.setCheckedItem(R.id.shouye);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                canExit = false;
            }
        };
        SparseArray a;
        HashMap map=new HashMap();
        HashSet set=new HashSet();
    }

    private boolean canExit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (canExit)
                super.onBackPressed();
            else {
                Toast.makeText(appContext, "再按一次退出", Toast.LENGTH_SHORT).show();
                canExit = true;
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem menuItem = menu.findItem(R.id.sv_search);//
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);//加载searchview
        searchView.setOnQueryTextListener(this);//为搜索框设置监听事件
//        searchView.setSubmitButtonEnabled(true);//设置是否显示搜索按钮
        searchView.setQueryHint("搜书");//设置提示信息
        searchView.setIconifiedByDefault(true);//设置搜索默认为图标
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "onQueryTextSubmit,query=" + query);
        if (!TextUtils.isEmpty(query.trim())) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra(Constant.QUERY, query.trim());
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e(TAG, "onQueryTextChange,newText=" + newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sv_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        fManager = getSupportFragmentManager();
        transaction = fManager.beginTransaction();
        int id = item.getItemId();
        if (id == R.id.shouye) {
            getSupportActionBar().setTitle(getString(R.string.home));
            switchFragment(mCurrentFragment, homeFragment);
        } else if (id == R.id.rank) {
            getSupportActionBar().setTitle(getString(R.string.rank));
            if (parentBooksFragment == null)
                parentBooksFragment = ParentBooksFragment.newInstance("http://m.23us.com/top/allvisit_1.html", "");
            switchFragment(mCurrentFragment, parentBooksFragment);
        } else if (id == R.id.catelog) {
            getSupportActionBar().setTitle(getString(R.string.catelog));
            if (parentCatelogFragment == null)
                parentCatelogFragment = ParentBooksFragment.newInstance("http://m.23us.com/class/1_1.html", "");
            switchFragment(mCurrentFragment, parentCatelogFragment);
        } else if (id == R.id.book_store) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.browser) {
            startActivity(new Intent(this, ReadWebActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 用来代替replace；
     * 切换页面的重载，优化了fragment的切换
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (from == null || to == null)
            return;
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.content_main, to).commitAllowingStateLoss();
        } else {
            transaction.hide(from).show(to).commitAllowingStateLoss();
        }
        mCurrentFragment = to;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
