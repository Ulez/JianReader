package comulez.github.jianreader.mvp.utils;

import android.util.Log;
import android.widget.Toast;

import java.io.Closeable;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import comulez.github.jianreader.mvp.MyApplication;

/**
 * Created by Ulez on 2017/3/30.
 * Email：lcy1532110757@gmail.com
 */


public class Utils {
    private static final String TAG = "Utils";
    public static final boolean Debug = true;

    public static void close(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * MD5加密获取key;
     *
     * @param url
     * @return
     */
    public static String keyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void t(String s) {
        Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public static void t(int resId) {
        Toast.makeText(MyApplication.getContext(), MyApplication.getContext().getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void i(String s) {
        if (Debug)
            Log.i("lcy", s);
    }

    public static void i(String tag, String s) {
        if (Debug)
            Log.i(tag, s);
    }

    public static void e(String s) {
        if (Debug)
            Log.e("lcy", s);
    }

    public static void e(String tag, String s) {
        if (Debug)
            Log.e(tag, s);
    }

    public static void d(String s) {
        if (Debug)
            Log.d("lcy", s);
    }

    public static void d(String tag, String s) {
        if (Debug)
            Log.d(tag, s);
    }

    public static void w(String s) {
        if (Debug)
            Log.w("lcy", s);
    }

    public static void w(String tag, String s) {
        if (Debug)
            Log.w(tag, s);
    }
}
