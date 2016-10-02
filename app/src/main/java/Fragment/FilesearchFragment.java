package fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weber.qsirch_offlinefiles.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FileSearchAdapter;
import model.FileSearchModel;

/**
 * Created by Weber on 2016/9/21.
 */

public class FilesearchFragment extends Fragment {

    public static String TAG = "FilesearchFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<FileSearchModel> mDatas;
    private FileSearchAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    protected void initData() {
        FileSearchModel dataModel = new FileSearchModel();
        mDatas = new ArrayList<>();
        dataModel.setFileName("File");
        dataModel.setFileDate("2016/10/02");
        dataModel.setFileSize("50mb");
        dataModel.setFileType("jpg");
        dataModel.setFileTime("12:29");

        mDatas.add(dataModel);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the intent, verify the action and get the query
        View rootView = inflater.inflate(R.layout.fragment_filesearch, container, false);

        Context context = rootView.getContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter = new FileSearchAdapter(context, mDatas));
        //To define new OptionMenu for fragment.
        setHasOptionsMenu(true);

        int position = getArguments().getInt("position");
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, query);
//            doMySearch(query);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        Log.d(TAG, "" + searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu, inflater);
    }
}
