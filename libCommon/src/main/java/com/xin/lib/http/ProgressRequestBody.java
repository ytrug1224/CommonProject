package com.xin.lib.http;

import androidx.annotation.Nullable;

import com.xin.lib.http.progress.Progress;
import com.xin.lib.http.progress.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * <br> ClassName:    ProgressRequestBody
 * <br> Description:  静态代理 RequestBody，增加进度回调
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/11/6 16:25
 */
public class ProgressRequestBody extends RequestBody {

    /**
     * 原 RequestBody
     */
    private RequestBody mRequestBody;

    /**
     * post 进度回调监听
     */
    private ProgressListener mProgressListener;

    public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
        this.mRequestBody = requestBody;
        this.mProgressListener = progressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return this.mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return this.mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        final Progress progress = new Progress();
        progress.setTotalSize(contentLength());

        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //累加写入数据
                progress.setCurrentSize(progress.getCurrentSize() + byteCount);
                if (ProgressRequestBody.this.mProgressListener != null) {
                    ProgressRequestBody.this.mProgressListener.onProgress(progress);
                }

            }
        };
        sink = Okio.buffer(forwardingSink);
        this.mRequestBody.writeTo(sink);
        sink.flush();
    }


}
