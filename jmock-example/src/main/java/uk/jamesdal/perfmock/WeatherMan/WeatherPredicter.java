package uk.jamesdal.perfmock.WeatherMan;

import java.time.LocalDate;

public interface WeatherPredicter {
    WeatherInformation predict(LocalDate date);
}