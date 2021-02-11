package uk.jamesdal.perfmock.WeatherMan;

import java.time.LocalDate;

public interface WeatherApi {
    WeatherInformation getInfo(LocalDate date);
}
