package com.basic.appbasiclibs.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip_Helper
{
    Context mContext;

    public Zip_Helper(Context mContext)
    {
        this.mContext=mContext;
    }

    public void zip(String[] _files, String zipFileName)
    {
        int BUFFER=1024;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zip(ArrayList<String> _files, String zipFileName)
    {
        int BUFFER=1024;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.size(); i++) {
                FileInputStream fi = new FileInputStream(_files.get(i));
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files.get(i).substring(_files.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String zipFile, String location) throws IOException
    {
        try
        {
            File f = new File(location);
            if (!f.isDirectory())
            {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try
            {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null)
                {
                    String path = location + File.separator + ze.getName();

                    if (ze.isDirectory())
                    {
                        File unzipFile = new File(path);
                        if (!unzipFile.isDirectory())
                        {
                            unzipFile.mkdirs();
                        }
                    }
                    else
                    {
                        FileOutputStream fout = new FileOutputStream(path, false);

                        try
                        {
                            for (int c = zin.read(); c != -1; c = zin.read())
                            {
                                fout.write(c);
                            }
                            zin.closeEntry();
                        }
                        finally
                        {
                            fout.close();
                        }
                    }
                }
            }
            finally
            {
                zin.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}