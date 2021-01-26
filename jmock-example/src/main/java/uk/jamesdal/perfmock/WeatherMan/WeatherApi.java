package uk.jamesdal.perfmock.WeatherMan;

import java.util.Date;

public interface WeatherApi {
    WeatherInformation getInfo(Date date);
}
