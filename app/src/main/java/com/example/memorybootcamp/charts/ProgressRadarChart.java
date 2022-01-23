package com.example.memorybootcamp.charts;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.database.ScoreType;
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
import java.util.List;

/** Radar chart showing current progress in all challenges. */
public class ProgressRadarChart {

    /** Radar chart object. */
    private final RadarChart chart;
    /** Data shown in the chart. */
    private RadarData data;
    /** Dataset of best result. */
    private RadarDataSet bestDataSet;
    /** Dataset of best week's result. */
    private RadarDataSet weeksDataSet;
    /** Label for best scores. */
    private final String BEST_SCORES_LABEL = "bests";
    /** Label for week's best scores. */
    private final String WEEKS_BEST_SCORES_LABEL = "weeksBests";
    /** Labels for categories, order according to ResultDAO query. */
    private final String[] categories = {"Words", "Faces", "Numbers", "Binary\nNumbers", "Cards"};
    /** Y-axis maximum as coefficient of maximal achieved score regardless of a challenge type. */
    private final float MAXIMUM_SCORE_TO_YAXIS_MAXIMUM = 1f;

    /** Constructor */
    public ProgressRadarChart(RadarChart radarChart){
        // radar chart setup
        chart = radarChart;
        chart.getDescription().setEnabled(false);
        chart.setDrawWeb(true);
        chart.setWebLineWidth(3f);
        chart.setWebLineWidthInner(2f);
        chart.setWebAlpha(255);
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);
        chart.setTouchEnabled(true);
        // x-axis setup
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value) {
                return categories[(int) value % categories.length];
            }
        });
        // y-axis setup
        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setAxisMinimum(0f);
        yAxis.setMaxWidth(1f);
        yAxis.setDrawLabels(false);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        // legend setup
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(Typeface.DEFAULT);
        l.setTextSize(9f);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        // initializations
        initializeValues();
        initializeColors();
    }

    /** Initialize values for graphs. */
    public void initializeValues() {
        // initialize data with zero values
        ArrayList<RadarEntry> bestEntries = new ArrayList<>();
        ArrayList<RadarEntry> weeksEntries = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            bestEntries.add(new RadarEntry(0f));
            weeksEntries.add(new RadarEntry(0f));
        }
        // setup plot for best results
        bestDataSet = new RadarDataSet(bestEntries, BEST_SCORES_LABEL);
        bestDataSet.setDrawFilled(true);
        bestDataSet.setLineWidth(2f);
        bestDataSet.setDrawHighlightCircleEnabled(false);
        bestDataSet.setDrawHighlightIndicators(false);
        // setup plot for week's best results 
        weeksDataSet = new RadarDataSet(weeksEntries, WEEKS_BEST_SCORES_LABEL);
        weeksDataSet.setDrawFilled(true);
        weeksDataSet.setLineWidth(2f);
        weeksDataSet.setDrawHighlightCircleEnabled(false);
        weeksDataSet.setDrawHighlightIndicators(false);
        // prepare datasets
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(bestDataSet);
        sets.add(weeksDataSet);
        // prepare data
        data = new RadarData(sets);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLUE);
        data.setDrawValues(true);
        // plot
        chart.setData(data);
        chart.getData().setHighlightEnabled(true);
    }

    /** Initialize colors of the graph. */
    public void initializeColors(){
        // prepare colors
        TypedValue typedValue = new TypedValue();
        chart.getContext().getTheme()
                .resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        chart.getContext().getTheme()
                .resolveAttribute(R.attr.colorSecondary, typedValue, true);
        @ColorInt int yLabelsColor = typedValue.data;
        chart.getContext().getTheme()
                .resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        @ColorInt int lineColor = typedValue.data;
        @ColorInt int weekFillColor = chart.getContext().getColor(R.color.graph_this_week_fill);
        @ColorInt int weekColor = chart.getContext().getColor(R.color.graph_this_week);
        @ColorInt int bestFillColor = chart.getContext().getColor(R.color.graph_best_fill);
        @ColorInt int bestColor = chart.getContext().getColor(R.color.graph_best);
        // set colors for axes, web and legend
        chart.setWebColor(lineColor);
        chart.setWebColorInner(lineColor);
        chart.getXAxis().setTextColor(foregroundColor);
        chart.getXAxis().setAxisLineColor(lineColor);
        chart.getXAxis().setGridColor(lineColor);
        chart.getYAxis().setTextColor(yLabelsColor);
        chart.getYAxis().setAxisLineColor(lineColor);
        chart.getYAxis().setGridColor(lineColor);
        chart.getYAxis().resetAxisMaximum();
        chart.getLegend().setTextColor(lineColor);
        // set colors for plots
        bestDataSet.setColor(bestColor);
        bestDataSet.setFillColor(bestFillColor);
        weeksDataSet.setColor(weekColor);
        weeksDataSet.setFillColor(weekFillColor);
        // set colors for y-labels
        data.setValueTextColor(yLabelsColor);
    }

    /** Update best results shown on the graph. */
    public void updateBests(List<ScoreType> scores){
        chart.getYAxis();
        if (scores.size() == categories.length) {
            int maxValue = 0;
            for (int i = 0; i < categories.length; i++) {
                chart.getData().getDataSetByLabel(BEST_SCORES_LABEL, false)
                        .getEntryForIndex(i).setY(scores.get(i).getScore());
                if (scores.get(i).getScore() > maxValue){ maxValue = scores.get(i).getScore(); }
            }
            chart.getYAxis().setAxisMaximum(MAXIMUM_SCORE_TO_YAXIS_MAXIMUM*maxValue);
            chart.notifyDataSetChanged();

        }
    }

    /** Update week's best results shown on the graph. */
    public void updateWeeksBests(List<ScoreType> scores){
        if (scores.size() == categories.length) {
            for (int i = 0; i < categories.length; i++) {
                chart.getData().getDataSetByLabel(WEEKS_BEST_SCORES_LABEL, false)
                        .getEntryForIndex(i).setY(scores.get(i).getScore());
            }
            chart.notifyDataSetChanged();
        }
    }

}
