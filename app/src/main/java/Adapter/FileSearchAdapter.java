package adapter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weber.qsirch_offlinefiles.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fragment.FilesearchFragment;
import model.FileSearchModel;
import preview.IconPreview;

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
    FilesearchFragment filesearchFragment;

    public FileSearchAdapter(Context context, FilesearchFragment filesearchFragment) {
        data = new ArrayList<>();
        selectedPositions = new ArrayList<>();
        mContext = context;
        this.filesearchFragment = filesearchFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offlinefile_list, parent, false);
        RelativeLayout clickableRelativeLayout = (RelativeLayout) view.findViewById(R.id.clickableRelativeLayout);
        LinearLayout clickableLinearLayout = (LinearLayout)view.findViewById(R.id.ButtonLayout);

        final ListItemViewHolder vh = new ListItemViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFile(mContext, data.get(vh.getAdapterPosition()).getFileName());
//            }
//        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocus();
                return false;
            }
        });

        clickableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(mContext, data.get(vh.getAdapterPosition()).getFileName());
            }
        });

        clickableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filesearchFragment.OpenDrawer();
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
        mListItemViewHolder.ItemText.setText(DateFormatter.format(mFileSearchModel.getFileModifiedDate()));
        mListItemViewHolder.ItemTime.setText(TimeFormatter.format(mFileSearchModel.getFileModifiedDate()));
        // get icon
        IconPreview.getFileIcon(mFileSearchModel.getFileName(), mListItemViewHolder.listImage);
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
        Collections.sort(data, new DownloadTimeComparator());
        notifyDataSetChanged();
    }

    public void clearContent() {
        data.clear();
        notifyDataSetChanged();
    }

    public void sortContent(int order) {
        switch (order) {
            case 0:
                Collections.sort(data, new DownloadTimeComparator());
                Collections.reverse(data);
                break;
            case 1:
                Collections.sort(data, new DownloadTimeComparator());
                break;
            case 2:
                Collections.sort(data, new FileNameComparator());
                Collections.reverse(data);
                break;
            case 3:
                Collections.sort(data, new FileNameComparator());
                break;
            case 4:
                Collections.sort(data, new ModifiedDateComparator());
                Collections.reverse(data);
                break;
            case 5:
                Collections.sort(data, new ModifiedDateComparator());
                break;
            case 6:
                Collections.sort(data, new FileSizeComparator());
                Collections.reverse(data);
                break;
            case 7:
                Collections.sort(data, new FileSizeComparator());
                break;
        }
        notifyDataSetChanged();
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

    public class DownloadTimeComparator implements Comparator<FileSearchModel> {
        @Override
        public int compare(FileSearchModel obj1, FileSearchModel obj2) {
            if (obj1.getFileCreationTime() == obj2.getFileCreationTime()) {
                return 0;
            }
            if (obj1.getFileCreationTime() == null) {
                return -1;
            }
            if (obj2.getFileCreationTime() == null) {
                return 1;
            }
            return obj1.getFileCreationTime().compareTo(obj2.getFileCreationTime());
        }
    }

    public class FileNameComparator implements Comparator<FileSearchModel> {
        @Override
        public int compare(FileSearchModel obj1, FileSearchModel obj2) {
            if (obj1.getFileName() == obj2.getFileName()) {
                return 0;
            }
            if (obj1.getFileName() == null) {
                return -1;
            }
            if (obj2.getFileName() == null) {
                return 1;
            }
            return obj1.getFileName().compareTo(obj2.getFileName());
        }
    }

    public class ModifiedDateComparator implements Comparator<FileSearchModel> {
        @Override
        public int compare(FileSearchModel obj1, FileSearchModel obj2) {
            if (obj1.getFileModifiedDate() == obj2.getFileModifiedDate()) {
                return 0;
            }
            if (obj1.getFileModifiedDate() == null) {
                return -1;
            }
            if (obj2.getFileModifiedDate() == null) {
                return 1;
            }
            return obj1.getFileModifiedDate().compareTo(obj2.getFileModifiedDate());
        }
    }

    public class FileSizeComparator implements Comparator<FileSearchModel> {
        @Override
        public int compare(FileSearchModel obj1, FileSearchModel obj2) {
            if (obj1.getFileSize() == obj2.getFileSize()) {
                return 0;
            }
            if (obj1.getFileSize() == null) {
                return -1;
            }
            if (obj2.getFileSize() == null) {
                return 1;
            }
            return obj1.getFileSize().compareTo(obj2.getFileSize());
        }
    }
}


