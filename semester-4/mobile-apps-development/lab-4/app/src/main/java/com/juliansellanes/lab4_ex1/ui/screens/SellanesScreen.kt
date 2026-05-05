package com.juliansellanes.lab4_ex1.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.juliansellanes.lab4_ex1.data.Attraction
import com.juliansellanes.lab4_ex1.data.TravelData
import java.util.Locale

private const val GEOFENCE_RADIUS_METERS = 300f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellanesScreen(
    navController: NavController,
    attractionId: String?
) {
    val attraction = TravelData.attractionById(attractionId ?: "")

    if (attraction == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Map") },
                    navigationIcon = {
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text("Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Attraction not found")
            }
        }
        return
    }

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var hasLocationPermission by remember {
        mutableStateOf(hasLocationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasLocationPermission = hasLocationPermission(context)
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var lastTappedPoint by remember { mutableStateOf<LatLng?>(null) }
    var droppedPin by remember { mutableStateOf<LatLng?>(null) }
    var useSatelliteMap by rememberSaveable { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(attraction.latLng, 13f)
    }

    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2500L)
            .build()
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    DisposableEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (_: SecurityException) {
            }
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    val distanceToAttraction = userLocation?.let { distanceBetween(it, attraction.latLng) }
    val insideGeofence = distanceToAttraction != null && distanceToAttraction <= GEOFENCE_RADIUS_METERS

    var previousInsideGeofence by remember { mutableStateOf(false) }

    LaunchedEffect(insideGeofence) {
        if (insideGeofence && !previousInsideGeofence) {
            Toast.makeText(
                context,
                "You entered the geofence for ${attraction.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
        previousInsideGeofence = insideGeofence
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(attraction.name) },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            val isWideScreen = maxWidth >= 700.dp

            if (isWideScreen) {
                Row(modifier = Modifier.fillMaxSize()) {
                    MapSection(
                        attraction = attraction,
                        hasLocationPermission = hasLocationPermission,
                        userLocation = userLocation,
                        droppedPin = droppedPin,
                        onMapTap = { lastTappedPoint = it },
                        onMapLongPress = { droppedPin = it },
                        useSatelliteMap = useSatelliteMap,
                        onToggleMapType = { useSatelliteMap = !useSatelliteMap },
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    InfoSection(
                        attraction = attraction,
                        hasLocationPermission = hasLocationPermission,
                        distanceToAttraction = distanceToAttraction,
                        insideGeofence = insideGeofence,
                        lastTappedPoint = lastTappedPoint,
                        onRequestPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        onCenterLandmark = {
                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(attraction.latLng, 15f)
                        },
                        onCenterUser = {
                            userLocation?.let {
                                cameraPositionState.position =
                                    CameraPosition.fromLatLngZoom(it, 15f)
                            }
                        },
                        canCenterUser = userLocation != null,
                        modifier = Modifier.weight(0.8f)
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    MapSection(
                        attraction = attraction,
                        hasLocationPermission = hasLocationPermission,
                        userLocation = userLocation,
                        droppedPin = droppedPin,
                        onMapTap = { lastTappedPoint = it },
                        onMapLongPress = { droppedPin = it },
                        useSatelliteMap = useSatelliteMap,
                        onToggleMapType = { useSatelliteMap = !useSatelliteMap },
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoSection(
                        attraction = attraction,
                        hasLocationPermission = hasLocationPermission,
                        distanceToAttraction = distanceToAttraction,
                        insideGeofence = insideGeofence,
                        lastTappedPoint = lastTappedPoint,
                        onRequestPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        onCenterLandmark = {
                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(attraction.latLng, 15f)
                        },
                        onCenterUser = {
                            userLocation?.let {
                                cameraPositionState.position =
                                    CameraPosition.fromLatLngZoom(it, 15f)
                            }
                        },
                        canCenterUser = userLocation != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun MapSection(
    attraction: Attraction,
    hasLocationPermission: Boolean,
    userLocation: LatLng?,
    droppedPin: LatLng?,
    onMapTap: (LatLng) -> Unit,
    onMapLongPress: (LatLng) -> Unit,
    useSatelliteMap: Boolean,
    onToggleMapType: () -> Unit,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                mapType = if (useSatelliteMap) MapType.SATELLITE else MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = hasLocationPermission,
                compassEnabled = true,
                mapToolbarEnabled = true
            ),
            onMapClick = onMapTap,
            onMapLongClick = onMapLongPress
        ) {
            Marker(
                state = MarkerState(position = attraction.latLng),
                title = attraction.name,
                snippet = attraction.address
            )

            Circle(
                center = attraction.latLng,
                radius = GEOFENCE_RADIUS_METERS.toDouble(),
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                strokeColor = MaterialTheme.colorScheme.primary,
                strokeWidth = 3f
            )

            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Your location",
                    snippet = "Live location"
                )

                Polyline(
                    points = listOf(it, attraction.latLng),
                    color = MaterialTheme.colorScheme.tertiary,
                    width = 8f
                )
            }

            droppedPin?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Dropped pin",
                    snippet = "Long press location"
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onToggleMapType) {
                Text(if (useSatelliteMap) "Normal" else "Satellite")
            }
        }
    }
}

@Composable
private fun InfoSection(
    attraction: Attraction,
    hasLocationPermission: Boolean,
    distanceToAttraction: Float?,
    insideGeofence: Boolean,
    lastTappedPoint: LatLng?,
    onRequestPermission: () -> Unit,
    onCenterLandmark: () -> Unit,
    onCenterUser: () -> Unit,
    canCenterUser: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = attraction.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(text = attraction.address)
            Text(text = attraction.description)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (hasLocationPermission) {
                    "Location permission: granted"
                } else {
                    "Location permission: not granted"
                }
            )

            Text(
                text = "Distance to attraction: ${formatDistance(distanceToAttraction)}"
            )

            Text(
                text = if (insideGeofence) {
                    "Geofence status: inside 300 m zone"
                } else {
                    "Geofence status: outside 300 m zone"
                }
            )

            Text(
                text = if (lastTappedPoint == null) {
                    "Last map tap: tap anywhere on the map"
                } else {
                    "Last map tap: ${formatLatLng(lastTappedPoint)}"
                }
            )

            Text("Long press on the map to drop a custom pin.")
            Text("The purple line is a simple route preview from you to the attraction.")

            Spacer(modifier = Modifier.height(8.dp))

            if (!hasLocationPermission) {
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Grant Location Permission")
                }
            }

            OutlinedButton(
                onClick = onCenterLandmark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Center on Landmark")
            }

            OutlinedButton(
                onClick = onCenterUser,
                enabled = canCenterUser,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Center on My Location")
            }
        }
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarse = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fine || coarse
}

private fun distanceBetween(from: LatLng, to: LatLng): Float {
    val result = FloatArray(1)
    Location.distanceBetween(
        from.latitude,
        from.longitude,
        to.latitude,
        to.longitude,
        result
    )
    return result[0]
}

private fun formatDistance(distance: Float?): String {
    if (distance == null) return "Unknown"
    return if (distance < 1000f) {
        String.format(Locale.getDefault(), "%.0f m", distance)
    } else {
        String.format(Locale.getDefault(), "%.2f km", distance / 1000f)
    }
}

private fun formatLatLng(latLng: LatLng): String {
    return String.format(
        Locale.getDefault(),
        "%.5f, %.5f",
        latLng.latitude,
        latLng.longitude
    )
}