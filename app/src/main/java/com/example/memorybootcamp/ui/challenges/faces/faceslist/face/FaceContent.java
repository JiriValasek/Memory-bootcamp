package com.example.memorybootcamp.ui.challenges.faces.faceslist.face;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FaceContent {

    //https://github.com/syonip/android-recycler-example/blob/master/app/src/main/java/com/example/myapplication/PictureContent.java

    /**
     * An array of face items.
     */
    public static final List<FaceItem> ITEMS = new ArrayList<>();

    public static void loadSavedImages(Context context, boolean fillInNames) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        ITEMS.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    loadImage(file, fillInNames);
                }
            }
        }
    }

    public static void deleteSavedImages(Context context) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        int deletedCount = 0;
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                Log.d("MEMORYBOOTCAMP", "Deleting " + file.getName() + "...");

                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    if (file.delete()) {
                        deletedCount++;
                    }
                }
            }
        }
        if (ITEMS.size() > deletedCount){
            Log.e("MEMORYBOOTCAMP","Image removal failed.");
        } else {
            Log.d("MEMORYBOOTCAMP","Deleting succesful.");
        }
        ITEMS.clear();
    }

    public static void downloadImage(DownloadManager downloadmanager, Context context,
                                     String imageUrl, String imageName) {
        Uri uri = Uri.parse(imageUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("My File");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(false);
        String fileName = imageName + ".jpg";
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        downloadmanager.enqueue(request);
    }

    public static String getFaceNameFromUri(Uri uri){
        String[] split = uri.getPath().split("/");
        String name = split[split.length - 1];
        name = name.split("\\.")[0];
        name = name.replace("_", " ");
        return name;
    }

    public static int loadImage(File file, boolean fillInName) {
        FaceItem newItem = new FaceItem();
        newItem.uri = Uri.fromFile(file);
        newItem.faceName = new SpannableStringBuilder("");
        if (fillInName) {
            newItem.faceName.append(getFaceNameFromUri(newItem.uri));
        }
        return addItem(newItem);
    }

    private static int addItem(FaceItem item) {
        if (!ITEMS.contains(item)) ITEMS.add(item);
        return ITEMS.indexOf(item);
    }

    /**
     * A face item representing a piece of content.
     */
    public static class FaceItem {
        public SpannableStringBuilder faceName;
        public Uri uri;
    }
}