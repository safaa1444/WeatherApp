package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.CurrentWeatherScreen
import com.example.weatherapp.ui.theme.ForecastScreen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    mainViewModel: MainViewModel = viewModel()
) {

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    fun getDeviceLocation() {

        try {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {

                        val coordinates =
                            "${location.latitude},${location.longitude}"

                        mainViewModel.loadWeather(coordinates)

                    } else {

                        val tokenSource = CancellationTokenSource()

                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            tokenSource.token
                        ).addOnSuccessListener { currentLocation ->

                            if (currentLocation != null) {

                                val coordinates =
                                    "${currentLocation.latitude},${currentLocation.longitude}"

                                mainViewModel.loadWeather(coordinates)

                            } else {

                                mainViewModel.loadWeather("Halifax, NS")
                            }

                        }.addOnFailureListener {

                            mainViewModel.loadWeather("Halifax, NS")
                        }
                    }
                }
                .addOnFailureListener {

                    mainViewModel.loadWeather("Halifax, NS")
                }

        } catch (e: SecurityException) {

            mainViewModel.loadWeather("Halifax, NS")
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {

                getDeviceLocation()

            } else {

                mainViewModel.loadWeather("Halifax, NS")
            }
        }

    LaunchedEffect(Unit) {
        mainViewModel.loadWeather("Halifax, NS")

        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (
            finePermission == PackageManager.PERMISSION_GRANTED ||
            coarsePermission == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    MaterialTheme {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {

                        Text(
                            text = mainViewModel.weather?.location?.name
                                ?: "Weather App"
                        )
                    },

                    actions = {

                        TextButton(
                            onClick = { getDeviceLocation() }
                        ) {

                            Text("Refresh")
                        }
                    }
                )
            }

        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                TabRow(
                    selectedTabIndex = selectedTab
                ) {

                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Current Weather") }
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Daily Forecast") }
                    )
                }

                when {

                    mainViewModel.isLoading -> {

                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    }

                    mainViewModel.errorMessage != null -> {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Text(
                                text = "Unable to load weather"
                            )

                            Text(
                                text = mainViewModel.errorMessage ?: ""
                            )

                            Button(
                                onClick = {
                                    getDeviceLocation()
                                }
                            ) {

                                Text("Try Again")
                            }
                        }
                    }

                    mainViewModel.weather != null -> {

                        if (selectedTab == 0) {

                            CurrentWeatherScreen(
                                weather = mainViewModel.weather!!
                            )

                        } else {

                            ForecastScreen(
                                weather = mainViewModel.weather!!
                            )
                        }
                    }
                }
            }
        }
    }
}
