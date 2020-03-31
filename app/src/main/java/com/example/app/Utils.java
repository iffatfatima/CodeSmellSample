package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class Utils {
    static File saveImage(Bitmap bitmap) {
        FileOutputStream outStream = null;
        File externalDir = Environment.getExternalStorageDirectory();//PD
        File dir = new File(externalDir.getAbsolutePath() + "/MyStories");
        boolean created = dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static File [] loadImages() {
        File externalDir = Environment.getExternalStorageDirectory();//PD
        File dir = new File(externalDir.getAbsolutePath() + "/MyStories");
        if (dir.exists()) {
            return dir.listFiles();
        }
        return null;
    }

    static int dpToPx(Context context, int dp) {
        return (int) (dp / context.getResources().getDisplayMetrics().density);
    }
}
