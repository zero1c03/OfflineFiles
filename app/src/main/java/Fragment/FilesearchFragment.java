package fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.weber.qsirch_offlinefiles.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import adapter.FileSearchAdapter;
import model.FileSearchModel;
import utils.SimpleUtils;

/**
 * Created by Weber on 2016/9/21.
 */

public class FilesearchFragment extends Fragment {

    public static String TAG = "FilesearchFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<FileSearchModel> mDatas;
    private FileSearchAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the intent, verify the action and get the query
        View rootView = inflater.inflate(R.layout.fragment_filesearch, container, false);
        // Recyclerview
        context = rootView.getContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new FileSearchAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
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
                Log.d(TAG, newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class SearchTask extends AsyncTask<String, Void, ArrayList<String>> {
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
        protected ArrayList<String> doInBackground(String... params) {
            String location = Environment.getExternalStorageDirectory().toString();
            Log.d(TAG, params.toString());
            return SimpleUtils.searchInDirectory(location, params[0]);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> files) {
            int len = files != null ? files.size() : 0;
            mDatas = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                updateData(files.get(i));
            }
            mAdapter.addContent(mDatas);
            pr_dialog.dismiss();
        }
    }

    protected void updateData(String filepath) {
        FileSearchModel dataModel = new FileSearchModel();
        File file = new File(filepath);
        Date lastModate = new Date(file.lastModified());

        dataModel.setFileName(file);
        dataModel.setFileType(filepath.substring(filepath.lastIndexOf(".") + 1));
        dataModel.setFileSize((file.length()));
        dataModel.setFileDate(lastModate);
        dataModel.setFileTime(lastModate);

        mDatas.add(dataModel);

    }

}

