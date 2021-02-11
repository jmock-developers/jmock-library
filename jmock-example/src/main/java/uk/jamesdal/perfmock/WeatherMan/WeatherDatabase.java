package uk.jamesdal.perfmock.WeatherMan;

import java.time.LocalDate;

public interface WeatherDatabase {
    WeatherInformation getInfo(LocalDate date);

    void storeInfo(WeatherInformation info);
}
