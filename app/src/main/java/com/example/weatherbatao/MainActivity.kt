package com.example.weatherbatao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherRecyclerView = findViewById(R.id.weatherRecyclerView)
        weatherRecyclerView.layoutManager = LinearLayoutManager(this)

        weatherAdapter = WeatherAdapter(emptyList())
        weatherRecyclerView.adapter = weatherAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApiService = retrofit.create(WeatherApiService::class.java)
        val cities = listOf("London", "New York", "Paris", "Tokyo")

        for (city in cities) {
            weatherApiService.getWeather(city, "YOUR_API_KEY").enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        val weather = Weather(city, weatherResponse.main.temp - 273.15, weatherResponse.weather[0].description)
                        weatherAdapter.weatherList += weather
                        weatherAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    // Handle API call failure
                }
            })
        }
    }
}