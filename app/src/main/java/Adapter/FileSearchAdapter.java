package adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weber.qsirch_offlinefiles.R;

import java.util.ArrayList;
import java.util.List;

import model.FileSearchModel;

/**
 * Created by Weber on 2016/9/21.
 */


public class FileSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<? extends  FileSearchModel> data;
    ArrayList<Integer> selectedPositions;
    Context mContext;

    public FileSearchAdapter(Context context, ArrayList<? extends FileSearchModel> objects) {
        data = objects;
        selectedPositions = new ArrayList<>();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offlinefile_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FileSearchModel mFileSearchModel = data.get(position);
        ListItemViewHolder mListItemViewHolder = (ListItemViewHolder) holder;
        mListItemViewHolder.ItemTitle.setText(mFileSearchModel.getFileName());
        mListItemViewHolder.ItemText.setText(mFileSearchModel.getFileDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView ItemTitle;
        public TextView ItemSize;
        public TextView ItemText;
        public ImageView listImage;
        public ImageView ItemImage;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ItemTitle = (TextView) itemView.findViewById(R.id.ItemTitle);
            ItemSize = (TextView) itemView.findViewById(R.id.ItemSize);
            ItemText = (TextView) itemView.findViewById(R.id.ItemText);
            listImage = (ImageView) itemView.findViewById(R.id.listImage);
            ItemImage = (ImageView) itemView.findViewById(R.id.ItemImage);
        }
    }
}


