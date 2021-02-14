package uk.jamesdal.perfmock.WeatherMan;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.integration.junit4.perf.PerfMockery;
import uk.jamesdal.perfmock.perf.PerfRule;
import uk.jamesdal.perfmock.perf.PerfTest;
import org.junit.Test;
import org.junit.Rule;
import uk.jamesdal.perfmock.perf.models.Uniform;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.HtmlReportGenerator;

import java.time.LocalDate;

public class WeatherControllerTest {

    @Rule
    public PerfRule perfRule = new PerfRule(new HtmlReportGenerator());

    @Rule
    public PerfMockery ctx = new PerfMockery(perfRule.getReportGenerator(), perfRule.getSimulation());

    private final WeatherDatabase weatherDatabase = ctx.mock(WeatherDatabase.class);
    private final WeatherApi weatherApi = ctx.mock(WeatherApi.class);

    private final WeatherInformation info = ctx.mock(WeatherInformation.class);

    // First ~25ms, then ~0ms
    LocalDate date = LocalDate.parse("2050-02-12");

    @Test
    @PerfTest(iterations = 2000, warmups = 10)
    public void grabsFuturePrediction() {
        WeatherController ctlr = new WeatherController(weatherApi, weatherDatabase);

        ctx.checking(new Expectations() {{
            oneOf(weatherDatabase).getInfo(date); will(returnValue(null)); taking(seconds(5));
            oneOf(weatherApi).getInfo(date); will(returnValue(info)); taking(seconds(new Uniform(5.0, 10.0)));
        }});

        ctlr.predict(date);
    }

    @Test
    public void grabsFuturePredictionRepeat() {
        WeatherController ctlr = new WeatherController(weatherApi, weatherDatabase);

        ctx.repeat(2000, 10, () -> {
            ctx.checking(new Expectations() {{
                oneOf(weatherDatabase).getInfo(date); will(returnValue(null)); taking(seconds(5));
                oneOf(weatherApi).getInfo(date); will(returnValue(info)); taking(seconds(new Uniform(5.0, 10.0)));
            }});

            ctlr.predict(date);
        });
    }
}