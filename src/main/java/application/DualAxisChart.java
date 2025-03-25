package application;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DualAxisChart extends JFrame {

    public DualAxisChart(String title) {
        super(title);

        // Create dataset
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        
        
        MALTCall.connectMalt("192.168.115.176", 5000);
		String s = MALTCall.MALTGetData();
        

        String data = s;
        
        
        
        Scanner scanner = new Scanner(data);
        List<Double> times = new ArrayList<>();
        List<Double> testPressures = new ArrayList<>();
        List<Double> diffPressures = new ArrayList<>();
        scanner.nextLine(); // skip header
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t");
            double time = Double.parseDouble(line[1]);
            double testPressure = Double.parseDouble(line[2]);
            double diffPressure = Double.parseDouble(line[3]);
            times.add(time);
            testPressures.add(testPressure);
            diffPressures.add(diffPressure);
        }
        scanner.close();

        XYSeries series1 = new XYSeries("Test Pressure");
        XYSeries series2 = new XYSeries("Diff Pressure");
        for (int i = 0; i < times.size(); i++) {
            series1.add(times.get(i), testPressures.get(i));
            series2.add(times.get(i), diffPressures.get(i));
        }
        dataset1.addSeries(series1);
        dataset2.addSeries(series2);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "MALT Leak Test Chart",
                "Time (ms)",
                "Test Pressure",
                dataset1,
                PlotOrientation.VERTICAL,
                true, true, false);

        XYPlot plot = chart.getXYPlot();

        // Secondary dataset for Diff Pressure
        NumberAxis axis2 = new NumberAxis("Diff Pressure");
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        plot.setRenderer(0, renderer1);
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        plot.setRenderer(1, renderer2);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Customize the plot
        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.BLUE);
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.RED);

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DualAxisChart example = new DualAxisChart("Dual Axis Chart Example");
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
