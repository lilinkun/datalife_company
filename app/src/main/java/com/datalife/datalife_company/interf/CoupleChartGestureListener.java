package com.datalife.datalife_company.interf;

import android.graphics.Matrix;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * Created by LG on 2019/7/25.
 */
public class CoupleChartGestureListener implements OnChartGestureListener {

    private static final String TAG = CoupleChartGestureListener.class.getSimpleName();

    private LineChart srcChart;
    private OnEdgeListener edgeListener;//滑动到边缘的监听器
    private boolean isLoadMore;//是否加载更多
    private boolean canLoad;//K线图手指交互已停止，正在惯性滑动

    public CoupleChartGestureListener(LineChart srcChart) {
        this.srcChart = srcChart;
        isLoadMore = false;
    }

    public CoupleChartGestureListener(OnEdgeListener edgeListener, LineChart srcChart) {
        this.edgeListener = edgeListener;
        this.srcChart = srcChart;
        isLoadMore = true;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        canLoad = false;
        syncCharts();
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (isLoadMore) {
            float leftX = srcChart.getLowestVisibleX();
            float rightX = srcChart.getHighestVisibleX();
            if (leftX == srcChart.getXChartMin()) {//滑到最左端
                canLoad = false;
                if (edgeListener != null) {
                    edgeListener.edgeLoad(leftX, true);
                }
            } else if (rightX == srcChart.getXChartMax()) {//滑到最右端
                canLoad = false;
                if (edgeListener != null) {
//                    edgeListener.edgeLoad(rightX, false);
                }
            } else {
                canLoad = true;
            }
        }
        syncCharts();
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        syncCharts();
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        syncCharts();
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        syncCharts();
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        syncCharts();
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//        Log.d(TAG, "onChartScale " + scaleX + "/" + scaleY + " X=" + me.getX() + "Y=" + me.getY());

        syncCharts();
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
//        Log.d(TAG, "onChartTranslate " + dX + "/" + dY + " X=" + me.getX() + "Y=" + me.getY());
        if (canLoad) {
            float leftX = srcChart.getLowestVisibleX();
            float rightX = srcChart.getHighestVisibleX();
            float minX = srcChart.getXChartMin();
            if ((int) leftX == minX) {//滑到最左端
                canLoad = false;
                if (edgeListener != null) {
                    edgeListener.edgeLoad(leftX, true);
                }
            } else if (rightX == srcChart.getXChartMax()) {//滑到最右端
                canLoad = false;
                if (edgeListener != null) {
//                    edgeListener.edgeLoad(rightX, false);
                }
            }
        }
        syncCharts();
    }

    public void syncCharts() {
        Matrix srcMatrix;
        float[] srcVals = new float[9];
        Matrix dstMatrix;
        float[] dstVals = new float[9];
        // get src chart translation matrix:
        srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
        srcMatrix.getValues(srcVals);
        srcChart.getViewPortHandler().refresh(srcMatrix, srcChart, true);
        // apply X axis scaling and position to dst charts:
       /* for (Chart dstChart : dstCharts) {
            if (dstChart.getVisibility() == View.VISIBLE) {
                dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                dstMatrix.getValues(dstVals);

                dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
                dstVals[Matrix.MSKEW_X] = srcVals[Matrix.MSKEW_X];
                dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
                dstVals[Matrix.MSKEW_Y] = srcVals[Matrix.MSKEW_Y];
                dstVals[Matrix.MSCALE_Y] = srcVals[Matrix.MSCALE_Y];
                dstVals[Matrix.MTRANS_Y] = srcVals[Matrix.MTRANS_Y];
                dstVals[Matrix.MPERSP_0] = srcVals[Matrix.MPERSP_0];
                dstVals[Matrix.MPERSP_1] = srcVals[Matrix.MPERSP_1];
                dstVals[Matrix.MPERSP_2] = srcVals[Matrix.MPERSP_2];

                dstMatrix.setValues(dstVals);
                dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
            }
        }*/
    }

    public interface OnEdgeListener {
        void edgeLoad(float x, boolean left);
    }

}

