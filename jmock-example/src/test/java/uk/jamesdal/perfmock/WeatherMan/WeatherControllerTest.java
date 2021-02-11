package uk.jamesdal.perfmock.WeatherMan;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.integration.junit4.perf.PerfMockery;
import uk.jamesdal.perfmock.perf.PerfRule;
import uk.jamesdal.perfmock.perf.PerfTest;
import org.junit.Test;
import org.junit.Rule;
import uk.jamesdal.perfmock.perf.models.Uniform;

import java.time.LocalDate;

public class WeatherControllerTest {

    @Rule
    public PerfRule perfRule = new PerfRule();

    @Rule
    public PerfMockery ctx = new PerfMockery(perfRule.getSimulation());

    private final WeatherDatabase weatherDatabase = ctx.mock(WeatherDatabase.class);
    private final WeatherApi weatherApi = ctx.mock(WeatherApi.class);

    private final WeatherInformation info = ctx.mock(WeatherInformation.class);

    // First ~25ms, then ~0ms
    LocalDate date = LocalDate.parse("2021-02-12");

    @Test
    @PerfTest(iterations = 10, warmups = 10)
    public void grabsFuturePrediction() {
        // Always ~0ms
        WeatherController ctlr = new WeatherController(weatherApi, weatherDatabase);

        // First ~10ms, then ~0ms
        ctx.checking(new Expectations() {{
            oneOf(weatherDatabase).getInfo(date); will(returnValue(null)); taking(seconds(5));
            oneOf(weatherApi).getInfo(date); will(returnValue(info)); taking(seconds(new Uniform(5.0, 10.0)));
        }});

        // Always around 0ms
        ctlr.predict(date);
    }

    @Test
    public void grabsFuturePredictionRepeat() {
        // Always ~0ms
        WeatherController ctlr = new WeatherController(weatherApi, weatherDatabase);

        // First ~10ms, then ~0ms
        ctx.repeat(10, 10, () -> {
            ctx.checking(new Expectations() {{
                oneOf(weatherDatabase).getInfo(date); will(returnValue(null)); taking(seconds(5));
                oneOf(weatherApi).getInfo(date); will(returnValue(info)); taking(seconds(new Uniform(5.0, 10.0)));
            }});

            // Always around 0ms
            ctlr.predict(date);
        });
    }
}