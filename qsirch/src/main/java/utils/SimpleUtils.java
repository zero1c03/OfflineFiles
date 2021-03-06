package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.provider.DocumentFile;
import android.widget.Toast;

import preview.MimeTypes;

import com.example.weber.qsirch_offlinefiles.BuildConfig;
import com.example.weber.qsirch_offlinefiles.R;
import com.qnap.medialibrary.model.MultiMediaInfo;
import com.qnap.medialibrary.multimedia.MultiMediaViewPagerActivity;
import com.qnap.medialibrary.video.VideoActivity;

import org.videolan.libvlc.util.MediaInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;

public class SimpleUtils {

    private SimpleUtils() {
    }

    private static final int BUFFER = 16384;
    private static final long ONE_KB = 1024;
    private static final BigInteger KB_BI = BigInteger.valueOf(ONE_KB);
    private static final BigInteger MB_BI = KB_BI.multiply(KB_BI);
    private static final BigInteger GB_BI = KB_BI.multiply(MB_BI);
    private static final BigInteger TB_BI = KB_BI.multiply(GB_BI);

    // TODO: fix search with root
    private static void search_file(String dir, String fileName, ArrayList<String> n) {
        File rootDir = new File(dir);
        String[] list = rootDir.list();

        if (list != null && rootDir.canRead()) {
            for (String aList : list) {
                File check = new File(dir + "/" + aList);
                String name = check.getName();

                if (check.isFile() && name.toLowerCase().contains(fileName.toLowerCase())) {
                    n.add(check.getPath());
                } else if (check.isDirectory()) {
                    if (name.toLowerCase().contains(fileName.toLowerCase())) {
                        //n.add(check.getPath());

                        // change this!
                    } else if (check.canRead() && !dir.equals("/")) {
                        search_file(check.getAbsolutePath(), fileName, n);
                    }
                }
            }
        } else {

        }
    }

    public static ArrayList<String> listFiles(String path, Context c) {
        ArrayList<String> mDirContent = new ArrayList<>();

        if (!mDirContent.isEmpty())
            mDirContent.clear();

        final File file = new File(path);

        if (file.exists() && file.canRead()) {
            String[] list = file.list();

            // add files/folder to ArrayList depending on hidden status
            for (String aList : list) {
                mDirContent.add(path + "/" + aList);
            }
        }
        return mDirContent;
    }


    // filePath = currentDir + "/" + item
    // newName = new name
    public static boolean renameTarget(String filePath, String newName) {
        File src = new File(filePath);

        String temp = filePath.substring(0, filePath.lastIndexOf("/"));
        File dest = new File(temp + "/" + newName);

        if (src.renameTo(dest)) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DocumentFile document = DocumentFile.fromFile(src);

                if (document.renameTo(dest.getAbsolutePath())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void deleteTarget(String path) {
        File target = new File(path);

        if (target.isFile() && target.canWrite()) {
            target.delete();
        } else if (target.isDirectory() && target.canRead() && target.canWrite()) {
            String[] fileList = target.list();

            if (fileList != null && fileList.length == 0) {
                target.delete();
                return;
            } else if (fileList != null && fileList.length > 0) {
                for (String aFile_list : fileList) {
                    File tempF = new File(target.getAbsolutePath() + "/"
                            + aFile_list);

                    if (tempF.isDirectory())
                        deleteTarget(tempF.getAbsolutePath());
                    else if (tempF.isFile()) {
                        tempF.delete();
                    }
                }
            }

            if (target.exists())
                target.delete();
        }
    }

    public static ArrayList<String> searchInDirectory(String dir, String fileName) {
        ArrayList<String> names = new ArrayList<>();
        search_file(dir, fileName, names);
        return names;
    }

    public static void openFile(final Context context, final File target) {
        final String mime = MimeTypes.getMimeType(target);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", target);
            intent.setDataAndType(contentUri, mime);

            if (mime != null) {
                intent.setDataAndType(contentUri, mime);

            } else {
                intent.setDataAndType(contentUri, "*/*");
            }
        } else {
            if (mime != null) {
                intent.setDataAndType(Uri.fromFile(target), mime);
            } else {
                intent.setDataAndType(Uri.fromFile(target), "*/*");
            }
        }

        if (context.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
            Toast.makeText(context, R.string.cantopenfile, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.cantopenfile) + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void openFile(final Context context, final File target, final String openfileprogram) {
        final String mime = MimeTypes.getMimeType(target);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", target);
            intent.setDataAndType(contentUri, mime);

            if (mime != null) {
                intent.setDataAndType(contentUri, mime);
                intent.setPackage(openfileprogram);
            } else {
                intent.setDataAndType(contentUri, "*/*");
            }
        } else {
            if (mime != null) {
                intent.setDataAndType(Uri.fromFile(target), mime);
                intent.setPackage(openfileprogram);
            } else {
                intent.setDataAndType(Uri.fromFile(target), "*/*");
            }
        }

        if (context.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
            Toast.makeText(context, R.string.cantopenfile, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.cantopenfile) + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void openFileWithMMM(final Context context, final File target) {
        ArrayList<MultiMediaInfo> mediaList = new ArrayList<MultiMediaInfo>();
        final String mime = MimeTypes.getMimeType(target);
        boolean isImage = MimeTypes.isPicture(target);
        boolean isVideo = MimeTypes.isVideo(target);
        boolean isAudio = MimeTypes.isaudio(target);
        // boolean isApk = target.getName().endsWith(".apk");
        if (isImage == true) {
            MultiMediaInfo PhotoMedia = new MultiMediaInfo();
            PhotoMedia.type = MultiMediaInfo.TYPE_PHOTO;
            PhotoMedia.photoUrl = target.getPath();
            PhotoMedia.photoTitle = target.getName();
            mediaList.add(PhotoMedia);
//            VideoActivity.start(context, target.getPath(), target.getName());
        } else if (isVideo == true) {
            MultiMediaInfo VideoMedia = new MultiMediaInfo();
            VideoMedia.type = MultiMediaInfo.TYPE_VIDEO;
            VideoMedia.media = new MediaInfo(target.toString(), "", "", target.getName(), "http://blogs-images.forbes.com/olliebarder/files/2015/06/dragon_ball_super_logo.jpg");
            mediaList.add(VideoMedia);
//            VideoActivity.start(context, target.getPath(), target.getName());
        } else if (isAudio == true) {
            MultiMediaInfo AudioMedia = new MultiMediaInfo();
            AudioMedia.type = MultiMediaInfo.TYPE_AUDIO;
            AudioMedia.media = new MediaInfo(target.toString(), "", "", target.getName(), "http://blogs-images.forbes.com/olliebarder/files/2015/06/dragon_ball_super_logo.jpg");
            mediaList.add(AudioMedia);
//            VideoActivity.start(context, target.getPath(), target.getName());
        } else {

        }
        MultiMediaViewPagerActivity.start(context, mediaList, 0);
    }

    // get MD5 or SHA1 checksum from a file
    public static String getChecksum(File file, String algorithm) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest digester = MessageDigest.getInstance(algorithm);
            byte[] bytes = new byte[2 * BUFFER];
            int byteCount;
            String result = "";

            while ((byteCount = fis.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }

            for (byte aB : digester.digest()) {
                result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return null;
    }

    public static String formatCalculatedSize(long ls) {
        BigInteger size = BigInteger.valueOf(ls);
        String displaySize;

        if (size.divide(TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(TB_BI)) + " TB";
        } else if (size.divide(GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(GB_BI)) + " GB";
        } else if (size.divide(MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(MB_BI)) + " MB";
        } else if (size.divide(KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    public static long getDirectorySize(File directory) {
        final File[] files = directory.listFiles();
        long size = 0;

        if (files == null) {
            return 0L;
        }

        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    size += sizeOf(file);
                    if (size < 0) {
                        break;
                    }
                }
            } catch (IOException ioe) {
                // ignore exception when asking for symlink
            }
        }

        return size;
    }

    private static boolean isSymlink(File file) throws IOException {
        File fileInCanonicalDir;

        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    private static long sizeOf(File file) {
        if (file.isDirectory()) {
            return getDirectorySize(file);
        } else {
            return file.length();
        }
    }

    public static String getExtension(String name) {
        String ext;

        if (name.lastIndexOf(".") == -1) {
            ext = "";

        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    public static boolean isSupportedArchive(File file) {
        String ext = getExtension(file.getName());
        return ext.equalsIgnoreCase("zip");
    }
}