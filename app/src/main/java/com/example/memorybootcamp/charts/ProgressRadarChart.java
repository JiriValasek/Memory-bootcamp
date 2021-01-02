package com.example.memorybootcamp.charts;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.example.memorybootcamp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

public class ProgressRadarChart {

    private final RadarChart chart;
    private RadarData data;
    private RadarDataSet bestDataSet;
    private RadarDataSet weeksDataSet;
    private final String[] categories = {"Cards", "Numbers", "Faces", "Binary\nNumbers", "Words"};

    public ProgressRadarChart(RadarChart radarChart){

        chart = radarChart;
        chart.getDescription().setEnabled(false);
        chart.setDrawWeb(true);
        chart.setWebLineWidth(3f);
        chart.setWebLineWidthInner(2f);
        chart.setWebAlpha(255);
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);
        chart.setTouchEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value) {
                return categories[(int) value % categories.length];
            }
        });

        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawLabels(false);
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(Typeface.DEFAULT);
        l.setTextSize(9f);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    public void updateValues(float[] best, float[] week) {

        ArrayList<RadarEntry> bestEntries = new ArrayList<>();
        ArrayList<RadarEntry> weeksEntries = new ArrayList<>();
        Log.d("MEMO","Lengths are " + best.length + ", " + week.length);
        for (int i = 0; i < categories.length; i++) {
            bestEntries.add(new RadarEntry(best[i]));
            weeksEntries.add(new RadarEntry(week[i]));
        }

        bestDataSet = new RadarDataSet(bestEntries, "Personal bests");
        bestDataSet.setDrawFilled(true);
        bestDataSet.setLineWidth(2f);
        bestDataSet.setDrawHighlightCircleEnabled(false);
        bestDataSet.setDrawHighlightIndicators(false);

        weeksDataSet = new RadarDataSet(weeksEntries, "This Week bests");
        weeksDataSet.setDrawFilled(true);
        weeksDataSet.setLineWidth(2f);
        weeksDataSet.setDrawHighlightCircleEnabled(false);
        weeksDataSet.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(bestDataSet);
        sets.add(weeksDataSet);

        data = new RadarData(sets);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLUE);
        data.setDrawValues(true);

        chart.setData(data);
        chart.getData().setHighlightEnabled(true);
        chart.invalidate();

    }

    public void updateColors(){
        TypedValue typedValue = new TypedValue();
        chart.getContext().getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        chart.getContext().getTheme().resolveAttribute(R.attr.colorSecondary, typedValue, true);
        @ColorInt int yLabelsColor = typedValue.data;
        chart.getContext().getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        @ColorInt int lineColor = typedValue.data;
        @ColorInt int weekFillColor = chart.getContext().getColor(R.color.graph_this_week_fill);
        @ColorInt int weekColor = chart.getContext().getColor(R.color.graph_this_week);
        @ColorInt int bestFillColor = chart.getContext().getColor(R.color.graph_best_fill);
        @ColorInt int bestColor = chart.getContext().getColor(R.color.graph_best);

        chart.setWebColor(lineColor);
        chart.setWebColorInner(lineColor);
        chart.getXAxis().setTextColor(foregroundColor);
        chart.getXAxis().setAxisLineColor(lineColor);
        chart.getXAxis().setGridColor(lineColor);
        chart.getYAxis().setTextColor(yLabelsColor);
        chart.getYAxis().setAxisLineColor(lineColor);
        chart.getYAxis().setGridColor(lineColor);
        chart.getLegend().setTextColor(lineColor);

        bestDataSet.setColor(bestColor);
        bestDataSet.setFillColor(bestFillColor);

        weeksDataSet.setColor(weekColor);
        weeksDataSet.setFillColor(weekFillColor);

        data.setValueTextColor(yLabelsColor);
    }

}
