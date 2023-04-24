package dev.abdujabbor.locationsimple1

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dev.abdujabbor.locationsimple1.databinding.ActivityWeatherBinding
import dev.abdujabbor.locationsimple1.models.weather.WeatherData
import dev.abdujabbor.locationsimple1.retrofit.OpenWeatherMapService
import dev.abdujabbor.locationsimple1.utils.MyConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale


class WeatherActivity : AppCompatActivity() {
    val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val apiKey = "44ccba6d7fda73456aec8a2d8ba2315f"
        val openWeatherMapService = OpenWeatherMapService.create()
        val latLng = MyConstants.lattitude
        val weatherData =
            openWeatherMapService.getWeatherData(latLng.latitude, latLng.longitude, apiKey)

        weatherData.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val temperature = response.body()?.main?.temp
                    val humidity = response.body()?.main?.humidity
                    val icon = response.body()?.weather!![0]?.icon
                    val descrioption = response.body()?.weather!![0]?.description
                    val main = response.body()?.weather!![0].main

                    val imageurl = "https://openweathermap.org/img/wn/$icon@2x.png"
                    Glide.with(this@WeatherActivity)
                        .load(imageurl)
                        .into(binding.iconCelcius)

                    val temp = temperature?.toInt()!! - 273

                   val countryName =  getCountryName(this@WeatherActivity, latLng.latitude,latLng.longitude)
                   val locatlity =  getShtatName(this@WeatherActivity, latLng.latitude,latLng.longitude)

                    binding.main.text = main
                    binding.countryName.text = countryName +","+ locatlity
                    binding.percenthumidity.text = humidity.toString() + "%"
                    binding.celciusnow.text = temp.toString() + "°"
                    binding.description.text = descrioption.toString()
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Handle failure
            }
        })

    }

    fun getCountryName(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        var addresses: List<Address>? = null
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var result: Address
            return if (addresses != null && !addresses.isEmpty()) {
                addresses[0].getCountryName()
            }
            else null

        return addresses!![0].countryName
    }
    fun getShtatName(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        var addresses: List<Address>? = null
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var result: Address
            return if (addresses != null && !addresses.isEmpty()) {
                addresses[0].subAdminArea
            }
            else null
    }
}