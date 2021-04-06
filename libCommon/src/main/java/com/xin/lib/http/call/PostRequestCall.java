package com.xin.lib.http.call;

import android.text.TextUtils;

import com.xin.lib.http.ProgressRequestBody;
import com.xin.lib.http.HttpTools;
import com.xin.lib.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * <br> ClassName:    PostRequestCall
 * <br> Description:  Post请求
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 14:51
 */
public class PostRequestCall extends RequestCall {


    /**
     * Json
     */
    private String mContent = "";

    /**
     * 普通表单
     */
    private LinkedHashMap<String, String> mSimpleParams = new LinkedHashMap<>();

    /**
     * MultiPart
     */
    private LinkedHashMap<String, List<MultiPartFile>> mFileParams;

    /**
     * 二进制流
     */
    private byte[] mByteData;


    /**
     * <br> Description: Post请求
     * <br> Author:      zouxinjie
     * <br> Date:        2020/6/13 15:41
     *
     * @param url String
     */
    public PostRequestCall(String url) {
        super(url);
    }


    /**
     * <br> Description: 增加Json参数
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     *
     * <br> Author:      zouxinjie
     * <br> Date:        2020/6/13 15:41
     *
     * @param json String
     * @return PostRequestCall
     */
    public PostRequestCall addJson(String json) {
        this.mContent = json;
        return this;
    }

    @Override
    public PostRequestCall setTag(String tag) {
        super.setTag(tag);
        return this;
    }

    /**
     * <br> Description: 增加 byte[] 参数
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     * <br> Author:      zouxinjie
     * <br> Date:        2020/3/5 11:41
     */
    public PostRequestCall addBytes(byte[] data) {
        this.mByteData = data;
        return this;
    }

    /**
     * <br> Description: 增加表单数据K-V
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     *
     * <br> Author:      zouxinjie
     * <br> Date:        2020/12/21 14:47
     *
     * @param key   键
     * @param value 值
     * @return PostRequestCall
     */
    public PostRequestCall addParams(String key, String value) {
        mSimpleParams.put(key, value);
        return this;
    }

    /**
     * <br> Description: 批量增加表单数据K-V （Map数据格式）
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     *
     * <br> Author:      zouxinjie
     * <br> Date:        2020/12/21 14:48
     *
     * @param paramsMap Map数据格式表单数据
     * @return PostRequestCall
     */
    public PostRequestCall addParams(Map<String, String> paramsMap) {
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                mSimpleParams.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    /**
     * <br> Description: 增加文件（Multipart提交）
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     *
     * <br> Author:      zouxinjie
     * <br> Date:        2020/12/21 14:48
     *
     * @param key      key
     * @param file     上传文件
     * @param fileName 上传文件名
     * @param contentType 上传文件content-type，
     *                    exp：MediaType.parse("image/png")
     * @return PostRequestCall
     */
    public PostRequestCall addParams(String key, File file, String fileName, MediaType contentType) {
        if (!TextUtils.isEmpty(key) && file != null) {
            if (mFileParams == null) {
                mFileParams = new LinkedHashMap<>();
            }

            List<MultiPartFile> fileList = mFileParams.get(key);
            if (fileList == null) {
                fileList = new ArrayList<>();
                mFileParams.put(key, fileList);
            }
            fileList.add(new MultiPartFile(file,
                    !TextUtils.isEmpty(fileName) ? fileName : file.getName(), contentType));
        }
        return this;
    }

    /**
     * <br> Description: 增加文件（Multipart提交）-指定文件 content-type
     * *优先级顺序（互斥）：addJson > addBytes > addParams(普通) > addParams(文件)
     *
     * <br> Author:      zouxinjie
     * <br> Date:        2020/12/21 14:48
     *
     * @param key      key
     * @param file     上传文件
     * @param fileName 上传文件名
     * @return PostRequestCall
     */
    public PostRequestCall addParams(String key, File file, String fileName) {
        if (!TextUtils.isEmpty(key) && file != null) {
            if (mFileParams == null) {
                mFileParams = new LinkedHashMap<>();
            }

            List<MultiPartFile> fileList = mFileParams.get(key);
            if (fileList == null) {
                fileList = new ArrayList<>();
                mFileParams.put(key, fileList);
            }
            fileList.add(new MultiPartFile(file,
                    !TextUtils.isEmpty(fileName) ? fileName : file.getName()));
        }
        return this;
    }

    /**
     * <br> Description: 增加本地文件（Multipart提交）
     *
     * <br> Author:      zouxinjie
     *
     * @param key            key
     * @param filePaths     上传文件
     * @return PostRequestCall
     */
    public PostRequestCall addParams(String key, List<String> filePaths) {
        if (filePaths != null && filePaths.size() > 0) {
            if (mFileParams == null) {
                mFileParams = new LinkedHashMap<>();
            }

            List<MultiPartFile> fileList = mFileParams.get(key);
            if (fileList == null) {
                fileList = new ArrayList<>();
                mFileParams.put(key, fileList);
            }
            for (String filePath : filePaths) {
                if (!TextUtils.isEmpty(filePath)) {
                    File tmpFile = new File(filePath);
                    if (tmpFile.exists()) {
                        fileList.add(new MultiPartFile(tmpFile, tmpFile.getName()));
                    }
                }
            }
        }
        return this;
    }

    /**
     * <br> Description: 修饰 RequestBody
     * <br> Author:      zouxinjie
     * <br> Date:        2020/11/6 16:49
     */
    private RequestBody generateRequestBody(RequestBody requestBody) {
        if (getProgressListener() == null) {
            return requestBody;
        }
        return new ProgressRequestBody(requestBody, getProgressListener());
    }


    @Override
    public Request generateRequest() {
        Request.Builder mBuilder = new Request.Builder().url(getGenerateUrl());


        //默认："application/json; charset=utf-8"
        String jsonContent = mContent;
        //请求数据转换器（暂时只支持json请求）
        if (getRequestConverter() != null) {
            jsonContent = getRequestConverter().convert(mContent);
        }
        if (!TextUtils.isEmpty(jsonContent) ||
                (mSimpleParams.isEmpty() && (mFileParams == null || mFileParams.isEmpty()))
                        && (mByteData == null || mByteData.length == 0)) {
            if (HttpTools.getDebugMode()) {
                Logger.i("requestURL=" + getGenerateUrl());
                Logger.json(jsonContent);
            }
            return mBuilder
                    .post(generateRequestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonContent)))
                    .build();
        }

        //二进制流：application/octet-stream
        if (mByteData != null) {
            return mBuilder
                    .post(generateRequestBody(RequestBody.create(MediaType.parse("application/octet-stream"), mByteData)))
                    .build();
        }

        //普通表单提交：application/x-www-form-urlencoded
        if (!mSimpleParams.isEmpty()) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (String key : mSimpleParams.keySet()) {
                bodyBuilder.addEncoded(key, String.valueOf(mSimpleParams.get(key)));
            }
            return mBuilder
                    .post(generateRequestBody(bodyBuilder.build()))
                    .build();
        }

        //表单中上传文件：multipart/form-data
        MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!mSimpleParams.isEmpty()) {
            for (String key : mSimpleParams.keySet()) {
                multipartBodybuilder.addFormDataPart(key, String.valueOf(mSimpleParams.get(key)));
            }
        }
        //拼接文件
        for (Map.Entry<String, List<MultiPartFile>> entry : mFileParams.entrySet()) {
            List<MultiPartFile> fileValues = entry.getValue();
            for (MultiPartFile multiPartFile : fileValues) {
                RequestBody fileBody;
                if (multiPartFile.mContentType == null) {
                    fileBody = RequestBody.create(MultipartBody.FORM, multiPartFile.mFile);
                } else {
                    fileBody = RequestBody.create(multiPartFile.mContentType, multiPartFile.mFile);
                }
                multipartBodybuilder.addFormDataPart(entry.getKey(), multiPartFile.mFileName, fileBody);
            }
        }
        return mBuilder
                .post(generateRequestBody(multipartBodybuilder.build()))
                .build();
    }


    /**
     * 文件类型的包装类
     */
    private static final class MultiPartFile {

        /**
         * 文件
         */
        private File mFile;
        /**
         * 文件名
         */
        private String mFileName;
        /**
         * 文件content-type
         */
        private MediaType mContentType;

        /**
         * <br> Description: 构造函数
         * <br> Author:      zouxinjie
         * <br> Date:        2020/12/21 14:55
         *
         * @param file     文件
         * @param fileName 文件名
         */
        private MultiPartFile(File file, String fileName) {
            this.mFile = file;
            this.mFileName = fileName;
        }

        /**
         *<br> Description: 构造函数
         *<br> Author:      zouxinjie
         *<br> Date:        2020/4/29 11:57
         *
         * @param file     文件
         * @param fileName 文件名
         * @param contentType 文件content-type
         */
        private MultiPartFile(File file, String fileName, MediaType contentType) {
            this.mFile = file;
            this.mFileName = fileName;
            this.mContentType = contentType;
        }

    }
}
