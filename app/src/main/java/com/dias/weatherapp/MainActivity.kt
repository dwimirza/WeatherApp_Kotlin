package com.nanda.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dias.weatherapp.R
import com.dias.weatherapp.databinding.ActivityMainBinding
import com.nanda.weatherapp.nanda.WeatherAdapter
import com.nanda.weatherapp.nanda.response.ForecastResponse
import com.nanda.weatherapp.nanda.response.WeatherResponse
import com.nanda.weatherapp.nanda.utils.HelperFunctions.formatterDegree
import com.nanda.weatherapp.nanda.utils.LOCATION_PERMISSION_REQ_CODE
import com.nanda.weatherapp.nanda.utils.iconSizeWeather4x
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var _viewModel: MainViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private var _weatherAdapter: WeatherAdapter? = null
    private val weatherAdapter by lazy { WeatherAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightNavigationBars = true

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        searchCity()
        viewModel.getWeatherByCity().observe(this) {
            setupView(it, null)
        }


        viewModel.getForecastByCity().observe(this) {
            setupView(null, it)
        }

        getWeatherCurrentLocation()
        getWeatherByCity()
    }

    private fun getWeatherByCity() {
        viewModel.getWeatherByCity().observe(this){
            setupView(it, null)
        }

        viewModel.getForecastByCity().observe(this){
            setupView(null, it)
        }
    }


    private fun getWeatherCurrentLocation() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_REQ_CODE
            )
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val lat = it.latitude
            val lon = it.longitude

            viewModel.weatherByCity(lat, lon)
            viewModel.forecastByCurrentLocation(lat, lon)
        }
            .addOnFailureListener {
                Log.e("MainActivity", "FusedLocationError: Failed getting current location ")
            }
        viewModel.getForecastByCurrentLocation().observe(this) {
            setupView(null, it)
        }
        viewModel.getWeatherByCurrentLocation().observe(this) {
            setupView(it, null)
        }

    }

    private fun setupView(weather: WeatherResponse?, forecast: ForecastResponse?) {
        weather?.let {
            binding.apply {
                tvDegree.text = formatterDegree(it.main?.temp)
                tvCity.text = weather.name

                val icon = weather.weather?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + icon + iconSizeWeather4x
                Glide.with(applicationContext).load(iconUrl)
                    .into(binding.imgIcWeather)

                setupBackgroundWeather(weather.weather?.get(0)?.id, icon)
                rvForecastWeather.apply {
                    layoutManager =
                        LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    adapter = weatherAdapter
                }


            }
        }
    }

    private fun setupBackgroundWeather(idWeather: Int?, icon: String?) {
        idWeather?.let {
            when (idWeather){
                in resources.getIntArray(R.array.thunderstorm_id_list) ->
                    setImageBackground(R.drawable.thunderstorm)

                in resources.getIntArray(R.array.drizzle_id_list) ->
                    setImageBackground(R.drawable.drizzle)

                in resources.getIntArray(R.array.rain_id_list) ->
                    setImageBackground(R.drawable.rain)

                in resources.getIntArray(R.array.freezing_rain_id_list) ->
                    setImageBackground(R.drawable.freezing_rain)

                in resources.getIntArray(R.array.snow_id_list) ->
                    setImageBackground(R.drawable.snow)

                in resources.getIntArray(R.array.sleet_id_list) ->
                    setImageBackground(R.drawable.sleet)

                in resources.getIntArray(R.array.clear_id_list) -> {
                    when (icon) {
                        "01d" -> setImageBackground(R.drawable.clear)
                        "01n" -> setImageBackground(R.drawable.clear_night)
                    }
                }


                in resources.getIntArray(R.array.clouds_id_list) ->
                    setImageBackground(R.drawable.lightcloud)

                in resources.getIntArray(R.array.heavy_clouds_id_list) ->
                    setImageBackground(R.drawable.heavycloud)

                in resources.getIntArray(R.array.fog_id_list) ->
                    setImageBackground(R.drawable.fog)

                in resources.getIntArray(R.array.sand_id_list) ->
                    setImageBackground(R.drawable.sand)

                in resources.getIntArray(R.array.dust_id_list) ->
                    setImageBackground(R.drawable.dust)

                in resources.getIntArray(R.array.volcanic_ash_id_list) ->
                    setImageBackground(R.drawable.volcanic)

                in resources.getIntArray(R.array.squalls_id_list) ->
                    setImageBackground(R.drawable.squalls)

                in resources.getIntArray(R.array.tornado_id_list) ->
                    setImageBackground(R.drawable.tornado)
            }
        }
    }

    private fun setImageBackground(image: Int) {
        Glide.with(this).load(image).into(binding.imgBgWeather)
    }

    private fun searchCity() {
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchByCity(query.toString())
                viewModel.getForecastByCity(query.toString())
                // hide keyboard
                try {
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error hiding keyboard: ${e.message}")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}
