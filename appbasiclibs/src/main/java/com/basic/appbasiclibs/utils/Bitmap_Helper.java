package com.basic.appbasiclibs.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.print.PrintHelper;
import android.widget.ScrollView;

import com.basic.appbasiclibs.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Bitmap_Helper
{
    Context mContext;

    Bitmap bitmap=null;

    byte[] bytes;

    public Bitmap_Helper(Context mContext)
    {
        this.mContext=mContext;
    }

    public void PrintPhoto(Bitmap bitmap)
    {
        PrintHelper photoprinter=new PrintHelper(mContext);
        photoprinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoprinter.printBitmap("Print",bitmap);
    }

    public Bitmap bitmap_from_drawable(int drawable)
    {
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),drawable);

        //Drawable myDrawable = mContext.getResources().getDrawable(drawable);
        //bitmap = ((BitmapDrawable) myDrawable).getBitmap();

        return bitmap;
    }

    public Bitmap bitmap_from_sdcard(String image_path)
    {
        bitmap= BitmapFactory.decodeFile(image_path);

        return bitmap;
    }

    public Bitmap bitmap_from_url(String image_url)
    {
        try
        {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(image_url).getContent());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }

    public Bitmap bitmap_from_bytes(byte[] b)
    {
        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

        return bitmap;
    }

    public byte[] bytes_from_bitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        bytes = baos.toByteArray();

        return bytes;
    }

    public static Bitmap ResizedBitmap(Bitmap bm, float newHeight, float newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,matrix, false);

        return resizedBitmap;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter)
    {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());

        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,height, filter);

        return newBitmap;
    }

    public boolean saveBitmap(Bitmap bitmap, String image_name)
    {
        FileOutputStream fos;

        try
        {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+image_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return true;
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean saveBitmap(Bitmap bitmap, String image_name, String path)
    {
        FileOutputStream fos;

        try
        {
            fos = new FileOutputStream(path+"/"+image_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return true;
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean saveBytes(byte[] b, String image_name, String path)
    {
        return saveBitmap(bitmap_from_bytes(b),image_name,path);
    }

    public boolean saveBytes(byte[] b,String image_name)
    {
        return saveBitmap(bitmap_from_bytes(b),image_name);
    }

    public Drawable setTint(Drawable drawable, int color)
    {
        final Drawable newDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newDrawable, color);
        return newDrawable;
    }

    public Uri getImageUri(Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public long getFileSize(String imagePath)
    {
        long length = 0;

        try
        {
            File file = new File(imagePath);
            length = file.length();
            length = length / 1024;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return length;
    }

    public Bitmap getBitmapFromScrollView(ScrollView scrollView)
    {
        int h = 0;
        Bitmap bitmap = null;

        //get the actual height of scrollview
        for (int i = 0; i < scrollView.getChildCount(); i++)
        {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.color.white);
        }

        // create bitmap with target size
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        return bitmap;
    }

	public byte[] convertToByteArray(InputStream inputStream)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            int next = inputStream.read();
            while (next > -1)
            {
                bos.write(next);
                next = inputStream.read();
            }
            bos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }
	
	public String getRealPathFromURI(Uri contentUri)
    {
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
        {
            return contentUri.getPath();
        }
        else
        {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height)
    {
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(),mContext.getResources().getIdentifier(iconName, "drawable",mContext.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    //new things
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getFileName(Uri uri)
    {
        String result = null;
        if (uri.getScheme().equals("content"))
        {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            try
            {
                if (cursor != null && cursor.moveToFirst())
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
            finally
            {
                cursor.close();
            }
        }
        if (result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
                result =result.substring(cut + 1);
        }

        return result;
    }

    public static String getFilePath(Activity act, Uri uri) throws URISyntaxException
    {
        String selection = null;
        String[] selectionArgs = null;

        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(act.getApplicationContext(), uri))
        {
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type))
                {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try
            {
                cursor = act.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst())
                    return cursor.getString(column_index);
            }
            catch (Exception e)
            {

            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }

        return null;
    }
}
