package com.tm.web.draw;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import static org.jfree.chart.ChartUtils.saveChartAsJPEG;
import static org.jfree.chart.ChartUtils.writeChartAsJPEG;

public class PieUtils {
    public static final Integer DEFAULT_PIE_WIDTH = 800;
    public static final Integer DEFAULT_PIE_HEIGHT = 500;
    private PieUtils() {

    }

    public static String createPieBase64(String title, String subtitle, Map<String, Double> dataset) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createPieBytes(title, subtitle, dataset, DEFAULT_PIE_WIDTH, DEFAULT_PIE_HEIGHT));
    }

    public static String createPieBase64(String title, String subtitle, Map<String, Double> dataset, Integer width, Integer height) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/jpeg;base64," + encoder.encode(createPieBytes(title, subtitle, dataset, width, height));
    }

    public static byte[] createPieBytes(String title, String subtitle, Map<String, Double> dataset, Integer width, Integer height) throws IOException {
        JFreeChart objChart = createPieChart(title, subtitle, dataset);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeChartAsJPEG(outputStream, objChart, width, height);
        return outputStream.toByteArray();
    }

    public static byte[] createPieBytes(String title, String subtitle, Map<String, Double> dataset) throws IOException {
        return createPieBytes(title, subtitle, dataset, DEFAULT_PIE_WIDTH, DEFAULT_PIE_HEIGHT);
    }

    public static void createPieFile(String title, String subtitle, Map<String, Double> dataset, Integer width, Integer height, String filePath) throws IOException {
        JFreeChart objChart = createPieChart(title, subtitle, dataset);
        saveChartAsJPEG(new File(filePath), objChart, width, height);
    }

    public static void createPieFile(String title, String subtitle, Map<String, Double> dataset, String filePath) throws IOException {
        JFreeChart objChart = createPieChart(title, subtitle, dataset);
        saveChartAsJPEG(new File(filePath), objChart, DEFAULT_PIE_WIDTH, DEFAULT_PIE_HEIGHT);
    }

    public static JFreeChart createPieChart(String title, String subtitle, Map<String, Double> dataset) {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        dataset.forEach((series, value) -> {
            pieDataset.setValue(series+"("+value+")", value);
        });

        JFreeChart objChart = ChartFactory.createPieChart (
                title,
                pieDataset,
                true,
                true,
                false
        );
        Font font =  new Font("黑体", Font.BOLD, 24);
        TextTitle textTitle =  new TextTitle(title, font);
        objChart.setTitle(textTitle);

        if(StringUtils.isNoneBlank(subtitle)) {
            TextTitle subTextTitle = new TextTitle(subtitle, new Font("黑体", Font.BOLD, 14));
            objChart.addSubtitle(subTextTitle);
        }

        objChart.setBorderVisible(false);
        objChart.setBorderPaint(ChartColor.WHITE);

        PiePlot plot = ((PiePlot)objChart.getPlot());

        for (int i = 0; i < dataset.keySet().size(); i++) {
            if(i < ColorSet.colors.length) {
                plot.setSectionPaint(dataset.keySet().toArray()[i].toString(), ColorSet.colors[i]);
            }
        }

        plot.setBackgroundPaint(ChartColor.WHITE);

        plot.setOutlinePaint(ChartColor.WHITE);
        plot.setLabelBackgroundPaint(ChartColor.WHITE);
        plot.setLabelOutlinePaint(ChartColor.WHITE);

        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);
        plot.setShadowPaint(ChartColor.WHITE);

        plot.setLabelFont( new Font( "黑体", Font.TRUETYPE_FONT, 14));


        objChart.getLegend().setItemFont(new Font("黑体", Font.PLAIN, 14));
        return objChart;
    }

}
