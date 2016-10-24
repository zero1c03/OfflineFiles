package adapter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weber.qsirch_offlinefiles.R;

import java.util.ArrayList;
import java.util.List;

import model.FileSearchModel;

/**
 * Created by Weber on 2016/10/14.
 */


public class FileDetailsDrawerAdapter extends BaseAdapter {

    List<FileSearchModel> FileDeteils = new ArrayList<FileSearchModel>();

    Context context;

    public FileDetailsDrawerAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return FileDeteils.size();
    }

    @Override
    public Object getItem(int i) {
        return FileDeteils.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.file_details_view, viewGroup, false);

        }
        return null;
    }
}
