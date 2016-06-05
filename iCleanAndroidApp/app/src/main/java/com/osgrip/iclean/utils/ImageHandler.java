package com.osgrip.iclean.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Pranjal on 12-Jan-16.
 */
public class ImageHandler {
    private static Context context;
    public ImageHandler(Context context){
        ImageHandler.context =context;
    }
    public static void saveToInternalStorage(Bitmap bitmapImage, String fileName) throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Pictures/Swachh AppDemo");
        myDir.mkdirs();
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage = scaleDown(bitmapImage,320,true);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public static void downloadImage(String imageURL, String imageName){
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            URL url = new URL(imageURL);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            saveToInternalStorage(bitmap, imageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap loadImageFromStorage(String imageName)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        String path=root+"/Pictures/Swachh AppDemo/";

        try {
            Bitmap b = BitmapFactory.decodeFile(path + imageName);
            return b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static String loadImagePath(String imageName)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        String path=root+"/Pictures/Swachh AppDemo/";

        try {
            File f=new File(path, imageName);
            if (f.exists())
            {
                return path + imageName;
            }
            else return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
