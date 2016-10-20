package activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.weber.qsirch_offlinefiles.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import adapter.FileDetailsDrawerAdapter;
import adapter.drawerAdapter;
import fragment.FilesearchFragment;
import fragment.HomeFragment;
import model.FileSearchModel;

import static utils.SimpleUtils.formatCalculatedSize;
import static utils.SimpleUtils.openFile;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivity";

    private Context context;
    private HomeFragment mHomeFragment;         // Home page
    private FilesearchFragment mSearchFragment; // Offline search
    private DrawerLayout mDrawerLayout;         // 左側 DrawerLayout
    private ListView mDrawerList;               // 左邊選單List
    private ScrollView mFileDetailsScrollView;  // 右側選單List
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;          // 抽屜 title
    private CharSequence mTitle;                // Activity title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mFileDetailsScrollView = (ScrollView) findViewById(R.id.file_details_drawer_container);

        mTitle = mDrawerTitle = getTitle();

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

        // 鎖定 DrawerLayout 滑動方向
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
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

    // 左側選擇
    private void selectItem(int position) {
        Bundle args;
        FragmentManager fragmentManager;

        switch (position) {
            case 0:
                args = new Bundle();
                mHomeFragment = new HomeFragment();
                args.putInt("position", position);
                mHomeFragment.setArguments(args);
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        mHomeFragment).commit();
                break;
            case 1:
                args = new Bundle();
                mSearchFragment = new FilesearchFragment();
                args.putInt("position", position);
                mSearchFragment.setArguments(args);

                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        mSearchFragment).commit();
                break;
        }
        // 建立 Fragment

//        Bundle args = new Bundle();
//        args.putInt("position", position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame,
//                fragment).commit();

        // 設定是否要表現出項目被選定的樣式
        mDrawerList.setItemChecked(position, true);
        // 設定 Activity title
        setTitle(drawerAdapter.functionName[position]);
        // 關閉抽屜
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    // 右側
    public void OpenDrawer(final Context mContext, final FileSearchModel mFileSearchModel) {
        SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat TimeFormatter = new SimpleDateFormat("hh:mm:ss");

        TextView FileSize = (TextView) mFileDetailsScrollView.findViewById(R.id.file_size);
        TextView FileType = (TextView) mFileDetailsScrollView.findViewById(R.id.file_type);
        TextView FileDate = (TextView) mFileDetailsScrollView.findViewById(R.id.file_date);

        TextView FileName = (TextView) mFileDetailsScrollView.findViewById(R.id.file_name);
        TextView FilePath = (TextView) mFileDetailsScrollView.findViewById(R.id.file_path);

        TextView OpenButton = (TextView) mFileDetailsScrollView.findViewById(R.id.file_openButton);
        TextView OpenInButton = (TextView) mFileDetailsScrollView.findViewById(R.id.file_openinButton);
        TextView ShareLinkButton = (TextView) mFileDetailsScrollView.findViewById(R.id.file_sharelinkButton);
        TextView DetailButton = (TextView) mFileDetailsScrollView.findViewById(R.id.file_detailButton);
        TextView DeleteButton = (TextView) mFileDetailsScrollView.findViewById(R.id.file_deleteButton);

        FileSize.setText(formatCalculatedSize(mFileSearchModel.getFileSize()));
        FileType.setText(mFileSearchModel.getFileName().toString().substring(mFileSearchModel.getFileName().toString().lastIndexOf(".") + 1));
        FileDate.setText(DateFormatter.format(mFileSearchModel.getFileModifiedDate()) + TimeFormatter.format(mFileSearchModel.getFileModifiedDate()));

        FileName.setText(mFileSearchModel.getFileName().toString().substring(mFileSearchModel.getFileName().toString().lastIndexOf("/") + 1));
        FilePath.setText(mFileSearchModel.getFileName().toString());

        OpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        OpenInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(mContext, mFileSearchModel.getFileName());
            }
        });

        ShareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mFileSearchModel.getFileName());
                startActivity(Intent.createChooser(sharingIntent, mFileSearchModel.getFileName().toString().substring(mFileSearchModel.getFileName().toString().lastIndexOf("/") + 1)));
            }
        });

        DetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Delete");
                dialog.setMessage("Would you want to delete " + mFileSearchModel.getFileName().toString().substring(mFileSearchModel.getFileName().toString().lastIndexOf("/") + 1));
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSearchFragment.deleteFile(mFileSearchModel.getFileName());
                        mDrawerLayout.closeDrawer(GravityCompat.END);
                    }
                });
                dialog.show();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawerLayout.openDrawer(GravityCompat.END);

    }
}
