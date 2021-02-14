package uk.jamesdal.perfmock.perf.postproc.reportgenerators;

import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;

import java.text.DecimalFormat;

public class ConsoleReportGenerator implements ReportGenerator {

    private PerfStatistics stats;

    @Override
    public void setStats(PerfStatistics stats) {
        this.stats = stats;
    }

    @Override
    public void generateReport(String testName) {
        System.out.println("============");
        System.out.println("Test Results: " + testName);
        System.out.println("============");

        printReport();
    }

    @Override
    public void generateReport() {
        System.out.println("============");
        System.out.println("Test Results");
        System.out.println("============");

        printReport();
    }

    private void printReport() {
        DecimalFormat df = new DecimalFormat("#.00");

        System.out.println("Iterations: " + stats.count());
        System.out.println("Average Measured Time: " + df.format(stats.meanMeasuredTime()) + "ms (2dp)");
        System.out.println("Measured Time Variance: " + df.format(stats.varMeasuredTime()) + " (2dp)");
        System.out.println("Min Measured Time: " + df.format(stats.minMeasuredTime()) + "ms (2dp)");
        System.out.println("Max Measured Time: " + df.format(stats.maxMeasuredTime()) + "ms (2dp)");
        System.out.println("Total Runtime: " + df.format(stats.totalRuntime()) + "ms (2dp)");
        System.out.println("Average Runtime: " + df.format(stats.meanRuntime()) + "ms (2dp)");
        System.out.println("Runtime Variance: " + df.format(stats.varRuntime()) + " (2dp)");
        System.out.println("Min Runtime: " + df.format(stats.minRuntime()) + "ms (2dp)");
        System.out.println("Max Runtime: " + df.format(stats.maxRuntime()) + "ms (2dp)");
    }
}
