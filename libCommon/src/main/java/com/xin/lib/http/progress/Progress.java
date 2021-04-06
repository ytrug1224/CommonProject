package com.xin.lib.http.progress;

/**
 * <br> ClassName:    Progress
 * <br> Description:  VSmart post 进度数据模型
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/11/6 16:42
 */
public class Progress {

    /**
     * 总长度
     */
    private long totalSize = 0;
    /**
     * 当前长度
     */
    private long currentSize = 0;
    /**
     * 进度
     */
    private float progress = 0;

    public Progress() {

    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        //更新进度
        this.progress = this.currentSize * 1.0f / this.totalSize;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
