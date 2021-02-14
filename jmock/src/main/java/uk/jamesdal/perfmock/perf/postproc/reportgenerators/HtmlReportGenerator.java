package uk.jamesdal.perfmock.perf.postproc.reportgenerators;

import uk.jamesdal.perfmock.perf.postproc.PerfStatistics;
import uk.jamesdal.perfmock.perf.postproc.ReportGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.getProperty;

public class HtmlReportGenerator implements ReportGenerator {

    private static final String DEFAULT_REPORT_PATH = getProperty("user.dir") + "/build/reports/junitperf_report.html";
    private static final String REPORT_TEMPLATE = "templates/report.twig";

    private PerfStatistics stats;

    @Override
    public void setStats(PerfStatistics stats) {
        this.stats = stats;
    }

    @Override
    public void generateReport(String testName) {
        double minMeasuredTime = stats.minMeasuredTime();
        double maxMeasuredTime = stats.maxMeasuredTime();

        List<Double> measuredTimes = stats.getMeasuredTimes();

        Integer[] buckets = new Integer[20];
        Arrays.fill(buckets, 0);

        for (Double measuredTime : measuredTimes) {
            Double norm = (measuredTime - minMeasuredTime) / (maxMeasuredTime - minMeasuredTime);
            int bucket = Math.min(19, (int) Math.floor(norm * 20));
            buckets[bucket]++;
        }

    }

    @Override
    public void generateReport() {
        generateReport(null);

        renderTemplate();
    }

    private void renderTemplate() {

    }
}
