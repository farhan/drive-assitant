package com.travel.driveassistant.views;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travel.driveassistant.R;
import com.travel.driveassistant.databinding.FragmentMapBinding;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.managers.DataInputManager;
import com.travel.driveassistant.managers.TTSManager;
import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
import com.travel.driveassistant.tracker.logic.MonitorOverSpeed;
import com.travel.driveassistant.utils.MapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private Logger logger = new Logger(getClass().getName());

    private TTSManager ttsManager;
    private FragmentMapBinding binding;
    private GoogleMap googleMap;
    private Location lastUserLocation = null;

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
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ttsManager = new TTSManager(getActivity().getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        EventBus.getDefault().register(this);
        DataInputManager.startTakingLocationUpdates(getContext().getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationUpdateEvent event) {
        onUserLocationUpdate(event.location);
    }

    public void onUserLocationUpdate(@NonNull Location location) {
        FileLogger.write(getContext(), location);
        logger.debug("Got user location: "+location);

        binding.tvSpeed.setText(Math.round(MapUtil.getNormalizedSpeed(location)) + " kmph");
        if (!speedDiffExceeds5Kmph(location)) {
            return;
        }
        lastUserLocation = location;
        // Add a marker to user location and move the camera
        final LatLng userLocation = new LatLng(lastUserLocation.getLatitude(), lastUserLocation.getLongitude());
        final float speed = MapUtil.getNormalizedSpeed(lastUserLocation);
        googleMap.addMarker(new MarkerOptions()
                .position(userLocation).title(Math.round(speed)+" kmph")
                .icon(MapUtil.getMarkerIcon(MapUtil.getColorForSpeed(speed))));

        //Move the camera to the user's location and zoom in!
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));

        final int speedLimit = MonitorOverSpeed.getSpeedLimitOfLocation(location);
        if (speed > speedLimit) {
            ttsManager.speak("You are doing over speed. Speed limit it "+speedLimit+". Your speed is "+speed+".");
        }
        else if (speed > 75) {
            ttsManager.speak("Your speed is "+Math.round(speed));
        }
    }

    public boolean speedDiffExceeds5Kmph(@NonNull Location location) {
        if (lastUserLocation == null) {
            return true;
        }
        return Math.abs(MapUtil.getNormalizedSpeed(lastUserLocation) -
            MapUtil.getNormalizedSpeed(location)) > 5;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsManager.onDestroy();
        EventBus.getDefault().unregister(this);
        DataInputManager.stopTakingLocationUpdates(getContext().getApplicationContext());
    }
}
