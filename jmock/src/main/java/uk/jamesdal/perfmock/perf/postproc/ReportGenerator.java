package uk.jamesdal.perfmock.perf.postproc;

public interface ReportGenerator {

    public void setStats(PerfStatistics stats);

    public void generateReport(String testName);

    public void generateReport();

}
