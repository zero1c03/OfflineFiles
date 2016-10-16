package activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.weber.qsirch_offlinefiles.R;

import adapter.drawerAdapter;
import fragment.FilesearchFragment;
import fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static String TAG = "MainActivity";

    private Context context;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;               // 左邊選單List
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;          // 抽屜 title
    private CharSequence mTitle;                // Activity title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // 設定 mDrawerList's Adapter listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(context,
                R.layout.item_drawer, drawerAdapter.functionName));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // 將導航抽屜選單作用在ActionBar上
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove the title text from the Android ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 設定 ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                       /* 主要 Activity */
                mDrawerLayout,              /* DrawerLayout xml */
                R.string.drawer_open,        /* 開啓抽屜簡述 */
                R.string.drawer_close         /* 關閉抽屜簡述 */
        ) {
            public void onDrawerClosed(View view) {
                // 設定 action bar title
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                // 設定 action bar title
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };
        // ActionBarDrawerToggle 狀態與 DrawerLayout 同步
        mDrawerToggle.syncState();
        // DrawerLayout設定DrawerToggle監聽器
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // 預設顯示 position = 2 的資料內容
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 打開/關閉抽屜與打開方向
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    // 抽屜項目列表監聽器
    private class DrawerItemClickListener
            implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment = new HomeFragment();
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new FilesearchFragment();
                break;
        }
        // 建立 Fragment

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                fragment).commit();

        // 設定是否要表現出項目被選定的樣式
        mDrawerList.setItemChecked(position, true);
        // 設定 Activity title
        setTitle(drawerAdapter.functionName[position]);
        // 關閉抽屜
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        // User changed the text
        return false;
    }
}
