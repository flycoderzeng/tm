package com.tm.worker.draw;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static org.jfree.chart.ChartUtils.writeChartAsJPEG;

public class BarUtils {
    public static final Integer DEFAULT_BAR_WIDTH = 800;
    public static final Integer DEFAULT_BAR_HEIGHT = 500;
    private BarUtils() {

    }

    public static JFreeChart createBarChart(String title, String subtitle, double[][] data, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel) {
        CategoryDataset dataset = DatasetUtils.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

        //设置网格背景颜色
        CategoryPlot plot = chart.getCategoryPlot();
        //设置网格竖线颜色
        plot.setBackgroundPaint(ChartColor.WHITE);
        //设置网格横线颜色
        plot.setDomainGridlinePaint(ChartColor.LIGHT_GRAY);
        //显示每个柱的数值，并修改该数值的字体属性
        plot.setRangeGridlinePaint(ChartColor.PINK);


        Font font =  new Font("黑体", Font.BOLD, 24);
        TextTitle textTitle =  new TextTitle(title, font);
        chart.setTitle(textTitle);

        if(StringUtils.isNoneBlank(subtitle)) {
            TextTitle subTextTitle = new TextTitle(subtitle, new Font("黑体", Font.BOLD, 14));
            chart.addSubtitle(subTextTitle);
        }

        BarRenderer renderer = new IntervalBarRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        //设置柱子上字体是否可见
        renderer.setDefaultItemLabelsVisible(true);
        //设置柱子上的数字字体及大小
        renderer.setDefaultItemLabelFont(new Font("宋体", Font.PLAIN, 12));
        //设置每个地区所包含的平行柱的之间距离,占每组宽的30%
        renderer.setItemMargin(0.2);
        //柱子上的字体跟柱顶之间的距离
        renderer.setItemLabelAnchorOffset(5d);
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        for (int i = 0; i < data.length; i++) {
            if(i < ColorSet.colors.length) {
                renderer.setSeriesPaint(i, ColorSet.colors[i]);
            }
        }
        // 设置柱子为平面图不是立体的
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.1);

        plot.setRenderer(renderer);
        //x轴标题显示在下端(柱子竖直)或左侧(柱子水平)
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        // 将y轴标题显示在上端(柱子水平)或右侧(柱子竖直)
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setOutlinePaint(ChartColor.WHITE);


        chart.setBorderVisible(false);
        chart.setBorderPaint(ChartColor.WHITE);

        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));

        return chart;
    }

    public static void createBarFile(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel, Integer width, Integer height, String filePath) throws IOException {
        JFreeChart chart = createBarChart(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel);
        ChartUtils.saveChartAsJPEG(new File(filePath), chart, width, height, null);
    }

    public static void createBarFile(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel, String filePath) throws IOException {
        createBarFile(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT, filePath);
    }

    public static byte[] createBarBytes(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel, Integer width, Integer height) throws IOException {
        JFreeChart objChart = createBarChart(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeChartAsJPEG(outputStream, objChart, width, height);
        return outputStream.toByteArray();
    }

    public static byte[] createBarBytes(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel) throws IOException {
        JFreeChart objChart = createBarChart(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeChartAsJPEG(outputStream, objChart, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT);
        return outputStream.toByteArray();
    }


    public static String createBarBase64(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createBarBytes(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel));
    }

    public static String createBarBase64(String title, String subtitle, double[][] dataset, String[] rowKeys, String[] columnKeys, String categoryAxisLabel, String valueAxisLabel, Integer width, Integer height) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createBarBytes(title, subtitle, dataset, rowKeys, columnKeys, categoryAxisLabel, valueAxisLabel, width, height));
    }
}
