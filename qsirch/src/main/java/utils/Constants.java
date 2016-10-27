package utils;

import com.example.weber.qsirch_offlinefiles.BuildConfig;

import activity.MainActivity;

/**
 * Created by Weber on 2016/10/24.
 */

public class Constants {

    public static final String[] QnapApp =  {"Qfile", "Qmusic", "Qvideo", "Qphoto", "Qsirch"};
    public static final String Qfile = "com.qnap.qfile";
    public static final String Qvideo = "com.qnap.qvideo";
    public static final String Qphoto = "com.qnap.qphoto";
    public static final String Qmusic = "com.qnap.qmusic";
    public static final String Qsirch = BuildConfig.APPLICATION_ID;



    public static final String BROADST_GET_QFILE_DOWNLOADFOLDER = "com.qnap.mobile.qsirch.getAppDownloadFolder";
    public static final String RECEVER_GET_QFILE_DOWNLOADFOLDER = "com.qnap.mobile.qfile.returnAppDownloadFolder";
}
