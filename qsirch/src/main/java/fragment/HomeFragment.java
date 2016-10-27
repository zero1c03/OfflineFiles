package fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.weber.qsirch_offlinefiles.R;
import com.qnap.medialibrary.video.VideoActivity;

import activity.MainActivity;

/**
 * Created by Weber on 2016/9/26.
 */

public class HomeFragment extends Fragment {
    Context context;
    Button PlayVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();
        PlayVideo = (Button) rootView.findViewById(R.id.play_video);
        setListener();
        return rootView;
    }

    private void setListener() {
        PlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoActivity.start(context, "/storage/sdcard0/Qsirch/localfolder/sunmoon.mp4", "POKEMON");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
