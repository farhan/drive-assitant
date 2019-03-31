package com.travel.driveassistant.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.travel.driveassistant.BuildConfig;
import com.travel.driveassistant.R;
import com.travel.driveassistant.databinding.FragmentMapBinding;
import com.travel.driveassistant.managers.TTSManager;
import com.travel.driveassistant.util.FileLogger;
import com.travel.driveassistant.util.Logger;
import com.travel.driveassistant.util.MapUtil;
import com.travel.driveassistant.util.PermissionUtil;
import com.travel.driveassistant.util.PopUpUtil;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private final long GPS_TIME_INTERVAL = 500;
    private final long GPS_FASTEST_TIME_INTERVAL = 200;
    private final int GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    Logger logger = new Logger(getClass().getName());
    private TTSManager ttsManager;
    private FragmentMapBinding binding;
    private GoogleMap googleMap;
    private Location lastUserLocation = null;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserLocation();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        startLocationUpdates();

        ttsManager = new TTSManager(getActivity().getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getUserLocation();
    }

    public void getUserLocation() {
        if (!PermissionUtil.checkLocationPermissionGranted(getActivity())) {
            PermissionUtil.requestLocationPermission(getActivity());
        } else {
            getLastLocation();
        }
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            onUserLocationUpdate(task.getResult());
                        } else {
                            logger.error(task.getException());
                            logger.debug("getLastLocation:exception");
                            PopUpUtil.showSnackbar(binding.fragmentMapRoot, R.string.no_location_detected,
                                    android.R.string.ok, null);
                        }
                    }
                });
    }

    public void onUserLocationUpdate(@NonNull Location location) {
        FileLogger.write(getContext(), location);
        logger.debug("Got user location: "+location);

        binding.tvSpeed.setText(MapUtil.getNormalizedSpeed(location) + " kmph");
        if (!speedDiffExceeds5Kmph(location)) {
            return;
        }
        lastUserLocation = location;
        // Add a marker to user location and move the camera
        final LatLng userLocation = new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude());
        final int speed = MapUtil.getNormalizedSpeed(lastUserLocation);
        googleMap.addMarker(new MarkerOptions()
                .position(userLocation).title(speed+" kmph")
                .icon(MapUtil.getMarkerIcon("#ff0000")));

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        //Move the camera to the user's location and zoom in!
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));
        if (speed > 70) {
            ttsManager.speak("Your speed is "+speed);
        }
    }

    public boolean speedDiffExceeds5Kmph(@NonNull Location location) {
        if (lastUserLocation == null) {
            return true;
        }
        return Math.abs(MapUtil.getNormalizedSpeed(lastUserLocation) -
            MapUtil.getNormalizedSpeed(location)) > 5;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        logger.debug("onRequestPermissionResult");
        if (requestCode == PermissionUtil.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                logger.debug("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                PopUpUtil.showSnackbar(binding.fragmentMapRoot,
                        R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsManager.onDestroy();
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
            fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                    locationCallback,
            null /* Looper */);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            final Location location = locationResult.getLastLocation();
            if (location != null) {
                onUserLocationUpdate(location);
            }
        }
    };

    protected LocationRequest getLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(GPS_TIME_INTERVAL);
            locationRequest.setFastestInterval(GPS_FASTEST_TIME_INTERVAL);
            locationRequest.setPriority(GPS_PRIORITY);
        }
        return locationRequest;
    }
}
