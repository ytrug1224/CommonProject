package com.xin.lib.http.call;


import com.xin.lib.http.callback.AbstractCallback;
import com.xin.lib.http.callback.RangeRequestCallback;

import java.io.File;

import okhttp3.Request;

/**
 * <br> ClassName:    RangeRequestCall
 * <br> Description:  Range 断点下载请求
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/10/14 10:54
 */
public class RangeRequestCall extends RequestCall {
    private final String mFileName;
    private long mRangeSPos;
    private String mDownLoadPath;
    private String mFilePath;
    private boolean mIsRest;

    public RangeRequestCall(String url, String downLoadPath, String fileName) {
        super(url);
        mDownLoadPath = downLoadPath;
        mFileName = fileName;
        mFilePath = downLoadPath + "/" + mFileName;
    }

    /**
     *<br> Description: 重置起始位置为0
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/14 10:55
     */
    public void resetRangeSPos() {
        mIsRest = true;
        mRangeSPos = 0;
    }

    /**
     *<br> Description: 重置请求成功
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/14 10:55
     */
    public void resetOK() {
        mIsRest = false;
    }

    @Override
    public Request generateRequest() {
        if (!mIsRest) {
            //向服务器定位下载位置的起始点
            mRangeSPos = new File(mFilePath).length();
//            Logger.i("RangeRequestCall","rangeStart : " + mRangeSPos);
        }
        return (new Request.Builder()).url(this.getUrl()).addHeader("RANGE", "bytes=" + mRangeSPos + "-").build();
    }

    @Override
    public RequestCall execute(AbstractCallback abstractCallback) {
        if (abstractCallback == null) {
            return this;
        }

        if (abstractCallback instanceof RangeRequestCallback) {
            ((RangeRequestCallback)abstractCallback).onStart(mDownLoadPath, mFileName);
        }
        return super.execute(abstractCallback);
    }
}
