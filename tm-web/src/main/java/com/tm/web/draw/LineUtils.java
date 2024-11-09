package com.tm.web.draw;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static org.jfree.chart.ChartUtils.writeChartAsJPEG;

public class LineUtils {
    public static final Integer DEFAULT_BAR_WIDTH = 800;
    public static final Integer DEFAULT_BAR_HEIGHT = 500;
    private LineUtils() {

    }

    public static JFreeChart createLineChart(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel) {
        JFreeChart chart = ChartFactory.createLineChart(
                title,//图名字
                categoryAxisLabel,//横坐标
                valueAxisLabel,//纵坐标
                dataset,//数据集
                PlotOrientation.VERTICAL,
                true, // 显示图例
                true, // 采用标准生成器
                false);// 是否生成超链接
        chart.setBorderVisible(false);
        chart.setBorderPaint(ChartColor.WHITE);

        Font font =  new Font("黑体", Font.BOLD, 24);
        TextTitle textTitle =  new TextTitle(title, font);
        chart.setTitle(textTitle);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelFont(new Font("宋体", Font.PLAIN, 12));
        renderer.setItemLabelAnchorOffset(2d);

        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        plot.setRenderer(renderer);


        plot.setBackgroundPaint(ChartColor.WHITE);
        //背景底部横虚线
        plot.setRangeGridlinePaint(ChartColor.LIGHT_GRAY);
        //边界线
        plot.setOutlinePaint(ChartColor.WHITE);

        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));

        return chart;
    }

    public static String createLineBase64(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createLineBytes(title, dataset, categoryAxisLabel, valueAxisLabel));
    }

    public static String createLineBase64(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel, Integer width, Integer height) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createLineBytes(title, dataset, categoryAxisLabel, valueAxisLabel, width, height));
    }

    public static void createLineFile(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel, String filePath, Integer width, Integer height) throws IOException {
        JFreeChart chart = createLineChart(title, dataset, categoryAxisLabel, valueAxisLabel);
        ChartUtils.saveChartAsJPEG(new File(filePath), chart, width, height, null);
    }

    public static void createLineFile(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel, String filePath) throws IOException {
        createLineFile(title, dataset, categoryAxisLabel, valueAxisLabel, filePath, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT);
    }

    public static byte[] createLineBytes(String title, DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel, Integer width, Integer height) throws IOException {
        JFreeChart objChart = createLineChart(title, dataset, categoryAxisLabel, valueAxisLabel);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeChartAsJPEG(outputStream, objChart, width, height);

        return outputStream.toByteArray();
    }

    public static byte[] createLineBytes(String title,DefaultCategoryDataset dataset, String categoryAxisLabel, String valueAxisLabel) throws IOException {
        return createLineBytes(title, dataset, categoryAxisLabel, valueAxisLabel, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT);
    }
}
