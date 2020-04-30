package com.basic.appbasiclibs.utils;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

public class Json_Helper
{
    Context mContext;

    public Json_Helper(Context mContext)
    {
        this.mContext=mContext;
    }

    public String callApi(String url,List<NameValuePair> params)
    {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response;
        StatusLine statusLine;

        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            response = client.execute(httpPost);
            statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) // Status OK
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content,"utf-8"),8);
                String line;
                while ((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
            }
            else
            {
                Log.e("Log", "Failed to download result..");
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return str.toString();
    }

    public String callApi(String url)
    {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response;
        StatusLine statusLine;

        try
        {
            //httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            response = client.execute(httpPost);
            statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) // Status OK
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content,"utf-8"),8);
                String line;
                while ((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
            }
            else
            {
                Log.e("Log", "Failed to download result..");
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return str.toString();
    }

    public String callApi2(String url, List<NameValuePair> params)
    {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try
        {
            SSLSocketFactory sslFactory = null;
            sslFactory = new SSLSocket_Helper(null);

            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Enable HTTP parameters
            HttpParams hparams = new BasicHttpParams();
            HttpProtocolParams.setVersion(hparams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(hparams, HTTP.UTF_8);

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslFactory, 443));

            // Create a new connection manager using the newly created registry and then create a new HTTP client
            // using this connection manager
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(hparams, registry);
            client = new DefaultHttpClient(ccm, hparams);

            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
            }
            else
            {
                Log.e("Log", "Failed to download result.."+statusCode);
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        catch (UnrecoverableKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (KeyStoreException e)
        {
            e.printStackTrace();
        }

        return str.toString();
    }

    public String callApi2(String url)
    {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try
        {
            SSLSocketFactory sslFactory = null;
            sslFactory = new SSLSocket_Helper(null);

            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Enable HTTP parameters
            HttpParams hparams = new BasicHttpParams();
            HttpProtocolParams.setVersion(hparams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(hparams, HTTP.UTF_8);

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslFactory, 443));

            // Create a new connection manager using the newly created registry and then create a new HTTP client
            // using this connection manager
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(hparams, registry);
            client = new DefaultHttpClient(ccm, hparams);

            //httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
            }
            else
            {
                Log.e("Log", "Failed to download result.."+statusCode);
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        catch (UnrecoverableKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (KeyStoreException e)
        {
            e.printStackTrace();
        }

        return str.toString();
    }
}