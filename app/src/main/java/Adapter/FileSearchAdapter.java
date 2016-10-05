package adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weber.qsirch_offlinefiles.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.FileSearchModel;
import utils.SimpleUtils;

import static utils.SimpleUtils.formatCalculatedSize;
import static utils.SimpleUtils.openFile;

/**
 * Created by Weber on 2016/9/21.
 */


public class FileSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String TAG = "FileSearchAdapter";
    ArrayList<? extends FileSearchModel> data;
    ArrayList<Integer> selectedPositions;
    Context mContext;

    public FileSearchAdapter(Context context) {
        data = new ArrayList<>();
        selectedPositions = new ArrayList<>();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offlinefile_list, parent, false);
        final ListItemViewHolder vh = new ListItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(mContext, data.get(vh.getAdapterPosition()).getFileName());
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocus();
                Log.d(TAG, "TOUCHED");
                return false;
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat TimeFormatter = new SimpleDateFormat("hh:mm:ss");
        FileSearchModel mFileSearchModel = data.get(position);
        ListItemViewHolder mListItemViewHolder = (ListItemViewHolder) holder;
        mListItemViewHolder.ItemTitle.setText(mFileSearchModel.getFileName().toString().substring(mFileSearchModel.getFileName().toString().lastIndexOf("/") + 1));
        mListItemViewHolder.ItemSize.setText(formatCalculatedSize(mFileSearchModel.getFileSize()));
        mListItemViewHolder.ItemText.setText(DateFormatter.format(mFileSearchModel.getFileDate()));
        mListItemViewHolder.ItemTime.setText(TimeFormatter.format(mFileSearchModel.getFileTime()));
    }

    // If return 0, it will show nothing.
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addContent(ArrayList<? extends FileSearchModel> files) {
        if (!data.isEmpty())
            data.clear();
        data = files;
        notifyDataSetChanged();
    }

    public void clearContent() {
        data.clear();
        notifyDataSetChanged();
    }

    private void sortContent(String sort, int order) {
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView ItemTitle;
        public TextView ItemSize;
        public TextView ItemText;
        public TextView ItemTime;
        public ImageView listImage;
        public ImageView ItemImage;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ItemTitle = (TextView) itemView.findViewById(R.id.ItemTitle);
            ItemSize = (TextView) itemView.findViewById(R.id.ItemSize);
            ItemText = (TextView) itemView.findViewById(R.id.ItemText);
            ItemTime = (TextView) itemView.findViewById(R.id.ItemTime);
            listImage = (ImageView) itemView.findViewById(R.id.listImage);
            ItemImage = (ImageView) itemView.findViewById(R.id.ItemImage);
        }
    }

  public class CustomComparator implements Comparator<FileSearchModel> {
      @Override
      public int compare(FileSearchModel t1, FileSearchModel t2) {
          if(t1.getFileName())
          return 0;
      }
  }


}


