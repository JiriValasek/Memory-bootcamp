package com.example.memorybootcamp.charts;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.example.memorybootcamp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class ProgressLineChart{
    private final LineChart chart;
    private LineDataSet dataSet;

    public ProgressLineChart(LineChart lineChart) {
        this.chart = lineChart;

        YAxis yAxis = this.chart.getAxisLeft();
        yAxis.setTypeface(Typeface.DEFAULT);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawLabels(true);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        yAxis.setDrawLimitLinesBehindData(false);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
    }

    public void updateValues(float[] day, float[] score) {
        ArrayList<Entry> entries = new ArrayList<>();
        LineData data;

        for (int i = 0; i < day.length; i++) {
            entries.add(new Entry(day[i], score[i]));
        }
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            dataSet.setValues(entries);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            dataSet = new LineDataSet(entries, "Sample Data");
            dataSet.setDrawIcons(false);
            dataSet.setLineWidth(1f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawFilled(true);
            dataSet.setFormLineWidth(1f);
            dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            dataSet.setFormSize(15.f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            data = new LineData(dataSets);
            chart.setData(data);
        }
        chart.invalidate();
    }

    public void updateColors(){

        TypedValue typedValue = new TypedValue();
        chart.getContext().getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        chart.getContext().getTheme().resolveAttribute(R.attr.colorSecondary, typedValue, true);
        @ColorInt int labelsColor = typedValue.data;
        chart.getContext().getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        @ColorInt int lineColor = typedValue.data;

        chart.getXAxis().setTextColor(foregroundColor);
        chart.getXAxis().setGridColor(lineColor);
        chart.getXAxis().setAxisLineColor(lineColor);
        chart.getAxisLeft().setTextColor(foregroundColor);
        chart.getAxisLeft().setGridColor(lineColor);
        chart.getAxisLeft().setAxisLineColor(lineColor);
        if (Utils.getSDKInt() >= 18) {
            //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {lineColor, Color.argb(0,0,0,0)});
            gd.setCornerRadius(0f);
            dataSet.setFillDrawable(gd);
        } else {
            dataSet.setFillColor(lineColor);
        }
        dataSet.setColor(lineColor);
        dataSet.setCircleColor(lineColor);
        dataSet.setValueTextColor(labelsColor);
        dataSet.setHighLightColor(labelsColor);
    }
}
