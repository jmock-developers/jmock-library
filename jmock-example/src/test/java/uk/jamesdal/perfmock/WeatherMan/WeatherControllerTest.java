package uk.jamesdal.perfmock.WeatherMan;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherControllerTest {

    @Rule
    public JUnitRuleMockery ctx = new JUnitRuleMockery();
    private final WeatherDatabase weatherDatabase = ctx.mock(WeatherDatabase.class);
    private final WeatherApi weatherApi = ctx.mock(WeatherApi.class);

    private final WeatherInformation info = new WeatherInformation();

    @Test
    public void grabsFuturePrediction() throws ParseException {
        WeatherController ctlr = new WeatherController(weatherApi, weatherDatabase);
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2021");

        ctx.checking(new Expectations() {{
            oneOf(weatherDatabase).getInfo(date); will(returnValue(null));
            oneOf(weatherApi).getInfo(date); will(returnValue(info));
        }});
        ctlr.predict(date);
    }
  
}