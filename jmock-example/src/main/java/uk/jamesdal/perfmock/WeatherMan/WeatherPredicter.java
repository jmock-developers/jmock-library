package uk.jamesdal.perfmock.WeatherMan;

import java.util.Date;

public interface WeatherPredicter {
    WeatherInformation predict(Date date);
}