package com.xin.lib.http.progress;

/**
 * <br> ClassName:    MultiProgress
 * <br> Description:  多阶段进度数据模型
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/11/7 14:25
 */
public class MultiProgress {

    /**
     * 多大分段数
     */
    private final int MAX_SEGMENT_NUM = 5;

    /**
     * 分段数
     */
    private int segmentSize;

    /**
     * 当前进度阶段
     */
    private int currentSegment = 0;

    /**
     * 当前进度 （0 - 1）
     */
    private float progress = 0;



    /**
     * 分段进度占比数组
     */
    private float[] segmentArr;


    /**
     *<br> Description: 平均分配每段进度占比
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/7 14:36
     */
    public MultiProgress(int size) {
        if (size > MAX_SEGMENT_NUM) {
            throw new IllegalArgumentException("MAX_SEGMENT_NUM is 5!");
        }
        segmentSize = size < 0 ? 1 : size;
        float perRatio = 1 * 1.0f / segmentSize;

        segmentArr = new float[segmentSize];
        for (int i = 0; i < segmentSize; i++) {
            segmentArr[i] = perRatio;
        }
    }

    /**
     *<br> Description: 自定义分配每段进度占比
     *                  总和 > 1 , throw exception
     *                  总和 < 1 , 最后一个补齐到 1
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/7 14:42
     */
    public MultiProgress(float... perRatio) {
        if (perRatio == null || perRatio.length == 0) {
            throw new IllegalArgumentException("Param can't be null, you must pass at least one param.");
        }
        float totalRatio = 0;
        segmentSize = perRatio.length;
        segmentArr = new float[segmentSize];
        for (int i = 0; i < segmentSize; i++) {
            totalRatio += perRatio[i];
            if (totalRatio > 1) {
                throw new IllegalArgumentException("The sum of perRatio value can't > 1, sum=" + totalRatio);
            }
            segmentArr[i] = perRatio[i];
        }
        if (totalRatio < 1) {
            float dif = 1 - totalRatio;
            segmentArr[segmentSize-1] += dif;
        }
    }

    /**
     *<br> Description: 更新进度
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/7 14:58
     */
    public void updateProgress(int index, float progress) {
        if (index < currentSegment || index >= segmentSize) {
            throw new IllegalArgumentException("You can only pass index from " + currentSegment + " to " + (segmentSize-1));
        }
        float addProgress = progress < 0 ? 0 : progress;
        addProgress = addProgress > 1 ? 1 : addProgress;

        float readyRatio = 0;
        for (int i = 0; i < index; i++) {
            readyRatio += segmentArr[i];
        }
        this.progress =  readyRatio + segmentArr[index] * addProgress;


    }


    /**
     *<br> Description: 获取分段数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/7 14:47
     */
    public int getSegmentSize() {
        return segmentSize;
    }

    /**
     *<br> Description: 获取当前进度
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/7 15:17
     */
    public float getProgress() {
        return this.progress;
    }

}
