package uk.jamesdal.perfmock.WeatherMan;

import java.util.Date;

public interface WeatherDatabase {
    WeatherInformation getInfo(Date date);

    void storeInfo(WeatherInformation info);
}
