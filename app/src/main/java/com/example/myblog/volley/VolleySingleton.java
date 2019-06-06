package com.example.myblog.volley;

// https://developer.android.com/training/volley/requestqueue.html

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myblog.MyBlogApplication;

public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton () {
        mRequestQueue = getRequestQueue();
    }

    public static VolleySingleton getInstance () {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(MyBlogApplication.getAppContext());
        }
        return mRequestQueue;
    }

    public void addRequestQueue (Request request) {
        mRequestQueue.add(request);
    }
}
