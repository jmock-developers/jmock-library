package uk.jamesdal.perfmock.WeatherMan;

import java.time.LocalDate;

public class WeatherController implements WeatherPredicter {

    private final WeatherApi api;
    private final WeatherDatabase database;

    public WeatherController(WeatherApi api, WeatherDatabase database) {
        this.api = api;
        this.database = database;
    }

    @Override
    public WeatherInformation predict(LocalDate date) {
        WeatherInformation info = database.getInfo(date);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Database doesn't have the information
        if (info == null) {
            info = api.getInfo(date);

            LocalDate now = LocalDate.now();

            // Store information if date in past
            if (date.isBefore(now)) {
                database.storeInfo(info);
            }
        }

        return info;
    }
}
