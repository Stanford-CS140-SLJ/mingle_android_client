package com.example.mingle;

import android.app.Activity;
import android.app.Application;
import com.example.mingle.HttpHelper;

/**
 * Created by Tempnote on 2014-06-12.
 */

public class MingleApplication extends Application {
    public HttpHelper initHelper;
    public MingleUser currUser;
    public HttpHelper chatHelper;
}
