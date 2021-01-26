package uk.jamesdal.perfmock.WeatherMan;

import java.util.Date;

public class WeatherController implements WeatherPredicter {

    private final WeatherApi api;
    private final WeatherDatabase database;

    public WeatherController(WeatherApi api, WeatherDatabase database) {
        this.api = api;
        this.database = database;
    }

    @Override
    public WeatherInformation predict(Date date) {
        WeatherInformation info = database.getInfo(date);

        // Database doesn't have the information
        if (info == null) {
            info = api.getInfo(date);

            Date now = new Date(System.currentTimeMillis());

            // Store information if date in past
            if (date.before(now)) {
                database.storeInfo(info);
            }
        }

        return info;
    }
}
