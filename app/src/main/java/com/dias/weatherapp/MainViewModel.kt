package com.nanda.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanda.weatherapp.dias.network.ApiConfig
import com.nanda.weatherapp.dias.response.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val weatherByCity = MutableLiveData<WeatherResponse>()

    fun searchByCity(city: String) {
        ApiConfig().getApiService().getWeatherByCity(city).enqueue(
            object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("FailureCall", t.message.toString())
                }

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>,
                ) {
                    if (response.isSuccessful) {
                        weatherByCity.postValue(response.body())
                    }
                }

            }
        )
    }

    fun getWeatherByCity(): MutableLiveData<WeatherResponse> = weatherByCity
}