package fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.weber.qsirch_offlinefiles.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import activity.MainActivity;
import adapter.FileSearchAdapter;
import model.FileSearchModel;
import preview.IconPreview;
import utils.QnapAppFolder;
import utils.SimpleUtils;
import com.qnap.medialibrary.video.VideoActivity;

/**
 * Created by Weber on 2016/9/21.
 */

public class FilesearchFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "FilesearchFragment";
    private QnapAppFolder qnapAppFolder;
    private Context context;
    private FilesearchFragment filesearchFragment = this;
    private RecyclerView mRecyclerView;

    private FileSearchAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<FileSearchModel> mDatas;

    private ImageView mSortButton;
    private TextView mSortType;

    public FilesearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the intent, verify the action and get the query
        View rootView = inflater.inflate(R.layout.fragment_filesearch, container, false);

        // Recyclerview
        context = rootView.getContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));

        mLinearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new FileSearchAdapter(context, filesearchFragment);
        mRecyclerView.setAdapter(mAdapter);

        mSortButton = (ImageView) rootView.findViewById(R.id.sortbutton);
        mSortButton.setOnClickListener(this);

        mSortType = (TextView) rootView.findViewById(R.id.sortType);

        new IconPreview(context);
        // To define new OptionMenu for fragment.
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTask mTask = new SearchTask(context);
                mTask.execute(query);
                // close search view, IME
                searchItem.collapseActionView();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View view) {
        final PopupMenu popupMenu = new PopupMenu(context, mSortButton);
        switch (view.getId()) {
            case R.id.sortbutton:
                popupMenu.getMenuInflater().inflate(R.menu.menu_officeline_sort, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.DownloadTimeDescending:
                                mAdapter.sortContent(0);
                                mSortType.setText(R.string.download_time_descending);
                                break;
                            case R.id.DownloadTimeAscending:
                                mAdapter.sortContent(1);
                                mSortType.setText(R.string.download_time_ascending);
                                break;
                            case R.id.FileNameDescending:
                                mAdapter.sortContent(2);
                                mSortType.setText(R.string.file_name_descending);
                                break;
                            case R.id.FileNameAscending:
                                mAdapter.sortContent(3);
                                mSortType.setText(R.string.file_name_ascending);
                                break;
                            case R.id.ModifiedDateDescending:
                                mAdapter.sortContent(4);
                                mSortType.setText(R.string.modified_date_descending);
                                break;
                            case R.id.ModifiedDateAscending:
                                mAdapter.sortContent(5);
                                mSortType.setText(R.string.modified_date_ascending);
                                break;
                            case R.id.FileSizeDescending:
                                mAdapter.sortContent(6);
                                mSortType.setText(R.string.file_size_descending);
                                break;
                            case R.id.FileSizeAscending:
                                mAdapter.sortContent(7);
                                mSortType.setText(R.string.file_size_ascending);
                                break;
                        }
                        popupMenu.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }
    }

    // Add search data.
    protected void AddData(String filepath, String openfiletype, String belongwhitchapp) {
        FileSearchModel dataModel = new FileSearchModel();
        File file = new File(filepath);
        Date lastModate = new Date(file.lastModified());

        dataModel.setFileName(file);
        dataModel.setFileType(filepath.substring(filepath.lastIndexOf(".") + 1));
        dataModel.setFileSize((file.length()));
        dataModel.setFileModifiedDate(lastModate);
        dataModel.setFileCreationTime(lastModate);
        dataModel.setFileApp(openfiletype);

        mDatas.add(dataModel);
    }

    // Search AsyncTask.
    private class SearchTask extends AsyncTask<String, Void, ArrayList<String[]>> {
        private ProgressDialog pr_dialog;
        private final Context context;

        private SearchTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            mAdapter.clearContent();
            pr_dialog = ProgressDialog.show(context, null,
                    getString(R.string.search));
            pr_dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            ArrayList<String[]> QnapApp = CheckQnapApp();
            ArrayList<String> SearchArray = new ArrayList<>();
            ArrayList<String[]> SearchResult = new ArrayList<>();

            for (int app = 0; app < QnapApp.size(); app++) {
                String location = Environment.getExternalStorageDirectory().toString() + "/" + QnapApp.get(app)[0];
                SearchArray = SimpleUtils.searchInDirectory(location, params[0]);

                for (int searchresult = 0; searchresult < SearchArray.size(); searchresult++) {
                    String[] file = {QnapApp.get(app)[0], QnapApp.get(app)[1], SearchArray.get(searchresult)};
                    SearchResult.add(file);
                }
            }
            return SearchResult;
        }

        @Override
        protected void onPostExecute(final ArrayList<String[]> files) {
            int len = files != null ? files.size() : 0;
            mDatas = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                AddData(files.get(i)[2], files.get(i)[1], files.get(i)[0]);
            }
            mAdapter.addContent(mDatas);
            mAdapter.sortContent(0);
            mSortType.setText(R.string.download_time_descending);
            pr_dialog.dismiss();
        }
    }

    // RecyclerView Divider Item Decoration.
    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    // Open right Drawer
    public void OpenDrawer(final Context context, final FileSearchModel fileSearchModel) {
        ((MainActivity) getActivity()).OpenDrawer(context, fileSearchModel);
    }

    // Left drawer Delete button, Activity(DeleteButton) -> FilesearchFragment(deleteFile) -> FileSearchAdapter(deleteContent)
    public void deleteFile(File file) {
        mAdapter.deleteContent(file);
    }

    // Scan Qnap app
    public ArrayList<String[]> CheckQnapApp() {
        ArrayList<String[]> QnapApp = new ArrayList<>();
        String[] QNAPFileName = {"Qfile", "Qmusic", "Qvideo", "Qphoto"};
        String[] QNAPPackageName = {"com.qnap.qfile", "com.qnap.qmusic", "com.qnap.qvideo", "com.qnap.qphoto", "com.qnap.qmarket"};
        PackageManager packageManager = getActivity().getPackageManager();


        for (int app = 0; app < QNAPPackageName.length; app++) {
            try {
                boolean bExists = false;
                final List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
                for (int i = 0; i < packs.size(); ++i) {
                    final String packageName = packs.get(i).packageName;
                    if (packageName != null && packageName.startsWith(QNAPPackageName[app])) {
                        bExists = true;
                        break;
                    }
                }
                if (!bExists) {
                    // not exists
                } else {
                    String[] QnapAppSet = {QNAPFileName[app], QNAPPackageName[app]};
                    QnapApp.add(QnapAppSet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return QnapApp;
    }

}

