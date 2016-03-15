package it.tiwiz.rxjavacrunch.networking;

import it.tiwiz.rxjavacrunch.models.weather.OpenWeatherResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * Created by connieli on 3/14/16.
 */
public interface OpenWeatherService {
	public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
	public static final String APPID = "9147af6d08271e09dfe16b5cb47fec4d";

	/**
	 * http://api.openweathermap.org/data/2.5/weather?q=London&appid=APPID
	 * @param city
	 *      the city to look for
	 * @return current weather for city
	 */
	@GET("weather?")
	Observable<OpenWeatherResponse> getCityWeather(@Query("appid") String appid, @Query("q") String city);

}
