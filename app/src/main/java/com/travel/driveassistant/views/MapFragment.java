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
import com.google.android.gms.maps.model.PolylineOptions;
import com.travel.driveassistant.R;
import com.travel.driveassistant.databinding.FragmentMapBinding;
import com.travel.driveassistant.lib_speed_data.OverSpeedResult;
import com.travel.driveassistant.lib_speed_data.OverSpeedUtil;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.lib_utils.MapUtil;
import com.travel.driveassistant.managers.DataInputManager;
import com.travel.driveassistant.tracker.events.LocationUpdateOverSpeedEvent;
import com.travel.driveassistant.tracker.utils.TrackerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private Logger logger = new Logger(getClass().getName());

//    private TTSManager ttsManager;
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

//        ttsManager = new TTSManager(getActivity().getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        EventBus.getDefault().register(this);
        DataInputManager.startTakingLocationUpdates(getContext().getApplicationContext());

        drawCanalRoad();

        googleMap.setOnMapClickListener(
                new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
//                        boolean result = PolyUtil.isLocationOnPath(latLng, OverSpeedUtil.getCanalRoadPolyLine(), false, 90);
//                        android.util.Log.i("Farhan", "Result = "+result);
                    }
                }
        );
    }

//    public void drawCanalRoad1() {
//        final String jsonString = "{ \"geocoded_waypoints\" : [ { \"geocoder_status\" : \"OK\", \"place_id\" : \"ChIJTZafxAESGTkRTh97ji7Kyw4\", \"types\" : [ \"route\" ] }, { \"geocoder_status\" : \"OK\", \"place_id\" : \"ChIJmUP2uCICGTkRKo1LP3_BOs4\", \"types\" : [ \"establishment\", \"park\", \"point_of_interest\" ] } ], \"routes\" : [ { \"bounds\" : { \"northeast\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"southwest\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 } }, \"copyrights\" : \"Map data ©2019 Google\", \"legs\" : [ { \"distance\" : { \"text\" : \"28.9 km\", \"value\" : 28856 }, \"duration\" : { \"text\" : \"38 mins\", \"value\" : 2286 }, \"end_address\" : \"Thokar Niaz Biag Canal Park, Thokar Niaz Baig Flyover, Irrigation Colony, Lahore, Punjab, Pakistan\", \"end_location\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 }, \"start_address\" : \"Canal Bank Rd, Khaira, Lahore, Punjab, Pakistan\", \"start_location\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"steps\" : [ { \"distance\" : { \"text\" : \"6.1 km\", \"value\" : 6129 }, \"duration\" : { \"text\" : \"11 mins\", \"value\" : 656 }, \"end_location\" : { \"lat\" : 31.5736246, \"lng\" : 74.4376965 }, \"html_instructions\" : \"Head \\u003cb\\u003ewest\\u003c/b\\u003e on \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by the gas station (on the left)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"{ex_Eg{ueML|@RzA@FFVHh@DRFXDNPh@ZvARbAHZF\\\\LbAF^\\\\fD\\\\nClBlOJbAD^RvBhAfJ^tCRdBPrALjAFd@Db@BLP|AlB`PDb@Fd@BRFd@BRH\\\\Hf@Jr@BZB^Db@D^@T@FH\\\\EJMPAPAJAFAH@F@JHh@LbAf@dEj@vEl@|E@Ft@dGZpCLjAP~AhAvIt@fHfAbIXzBdAbJZfCJx@NjA`AnIXnBNhAf@hEj@xE^lD^bDJx@X~BDXLfAb@`Ep@fFb@tDJx@PvA`@`Dj@fGLdA^xCJ|@@NR~APpAh@rE^vCJfAb@tDD\\\\xAnLj@~Ez@pGbAnIdCdR\" }, \"start_location\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 486 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 31 }, \"end_location\" : { \"lat\" : 31.5726208, \"lng\" : 74.4327134 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eJoginder Lal Mandal Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Pak Mobile Point (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"cvu_EsrieMB`ANdAb@vCV`B?BPpABJRzARbBBVZfEX`B\" }, \"start_location\" : { \"lat\" : 31.5736246, \"lng\" : 74.4376965 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"4.7 km\", \"value\" : 4716 }, \"duration\" : { \"text\" : \"6 mins\", \"value\" : 375 }, \"end_location\" : { \"lat\" : 31.56285849999999, \"lng\" : 74.38430079999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Decent uniforms shop (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"{ou_EmsheMbA`J|@fIFh@Hb@nB~N@RHtAZvCR|BHx@D`@DZNdARtALhAB^ZhCRbBTxA\\\\zBd@bDT~ABRPpAb@rCNdAD\\\\BPJn@TxA?@NdALx@fAvKDd@RtBZvCXfCFx@D\\\\h@dGDTd@|C`@fC?@l@pEp@nGf@tDR|AXtBd@bDjA`IFh@PpAXxBh@vGdC`Up@xF\\\\dCX~DBNV`Bn@dFHb@BLNtADl@Fr@LjALnAf@xER|AJv@VzBvAzJ\" }, \"start_location\" : { \"lat\" : 31.5726208, \"lng\" : 74.4327134 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 673 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.5614809, \"lng\" : 74.3773941 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eHabba Khatoon Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Pasha Tyres (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"{rs_E{d_eMBv@@Tl@dFLz@Lp@Jt@V~ABJL|@Ff@Ff@@DHn@JdALrAb@|Eb@rD@H@JBJFN\" }, \"start_location\" : { \"lat\" : 31.56285849999999, \"lng\" : 74.38430079999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.1 km\", \"value\" : 124 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 9 }, \"end_location\" : { \"lat\" : 31.5611333, \"lng\" : 74.37615049999999 }, \"html_instructions\" : \"Continue straight onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e/\\u003cb\\u003ePeer Muhammad Sadiq Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eDrive along Irrigation Deparment Workshops (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"gjs_Euy}dMXzBj@zB\" }, \"start_location\" : { \"lat\" : 31.5614809, \"lng\" : 74.3773941 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"1.1 km\", \"value\" : 1083 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 71 }, \"end_location\" : { \"lat\" : 31.5577252, \"lng\" : 74.36553789999999 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Directorate of Land Reclamation Punjab (on the left in 1.0&nbsp;km)\\u003c/div\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"ahs_E}q}dMCRAP?VBX|@`F`@lDt@hE\\\\lBrApH`A|DJ^vCfKdA~CzAjELVJL\" }, \"start_location\" : { \"lat\" : 31.5611333, \"lng\" : 74.37615049999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 550 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 41 }, \"end_location\" : { \"lat\" : 31.5548863, \"lng\" : 74.360805 }, \"html_instructions\" : \"Continue straight onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e/\\u003cb\\u003ePeer Muhammad Sadiq Rd\\u003c/b\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"yrr_Eso{dMDJFNZjAh@pAfAzBp@xAh@fA^n@dApBj@hA\\\\p@V^h@v@JN`AtA\" }, \"start_location\" : { \"lat\" : 31.5577252, \"lng\" : 74.36553789999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 729 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 48 }, \"end_location\" : { \"lat\" : 31.5501086, \"lng\" : 74.3555664 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e at jabbar pan shop onto \\u003cb\\u003eHussain Shaheed Soherwordi Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"aar_E_rzdMDLVd@DF`@j@LPVZd@j@h@l@JLPThApAn@r@RV`AfAJLlAvAb@f@j@l@p@p@rAxA\\\\^rBnBnAbA\" }, \"start_location\" : { \"lat\" : 31.5548863, \"lng\" : 74.360805 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 578 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.54602659999999, \"lng\" : 74.35180389999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Spectrum Publisher (on the left in 300&nbsp;m)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"ecq_EiqydMZZvDnDlCjC`A`Al@p@|CbCLLh@b@b@\\\\bBpA\" }, \"start_location\" : { \"lat\" : 31.5501086, \"lng\" : 74.3555664 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 593 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 42 }, \"end_location\" : { \"lat\" : 31.5419067, \"lng\" : 74.3478362 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eFaiz Ahmed Faiz Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Maktaba Tul Madinah (on the right)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"uip_EwyxdMxAtAfDxCdD`DtBvB~B~BxClC\" }, \"start_location\" : { \"lat\" : 31.54602659999999, \"lng\" : 74.35180389999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 718 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 55 }, \"end_location\" : { \"lat\" : 31.5368876, \"lng\" : 74.343076 }, \"html_instructions\" : \"Continue onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by the bridge (on the right in 450&nbsp;m)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"}oo_E_axdMpBjBx@|@`B~AdA`ArBlB|BtBl@j@b@^z@t@hC~BrAlAx@v@\" }, \"start_location\" : { \"lat\" : 31.5419067, \"lng\" : 74.3478362 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 527 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 48 }, \"end_location\" : { \"lat\" : 31.5331892, \"lng\" : 74.3396362 }, \"html_instructions\" : \"Slight \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eWaris Shah Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-left\", \"polyline\" : { \"points\" : \"qpn_EgcwdMRHPLVRTNp@l@b@\\\\VRl@b@LJx@n@JHXXJJBBHJPNNNv@v@FDtApA~ArAX\\\\b@f@HNJX\" }, \"start_location\" : { \"lat\" : 31.5368876, \"lng\" : 74.343076 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.8 km\", \"value\" : 752 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 58 }, \"end_location\" : { \"lat\" : 31.5279291, \"lng\" : 74.3346751 }, \"html_instructions\" : \"Slight \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-left\", \"polyline\" : { \"points\" : \"mym_EwmvdMRVTVZ\\\\d@b@t@r@r@p@BBbA~@rBjBl@j@\\\\Z`@\\\\h@d@p@l@^\\\\j@b@JJRPJLRVLPNPX\\\\TXXV@@VRVNFHpCrB\" }, \"start_location\" : { \"lat\" : 31.5331892, \"lng\" : 74.3396362 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 452 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 32 }, \"end_location\" : { \"lat\" : 31.524734, \"lng\" : 74.331749 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eJustice A. R. Cornelius Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"qxl_EwnudMDJBFDDFFNPJJNLZT^Vf@^NL^XJHVRj@j@ZX@@^\\\\pApAhBjBVRVTDBDB`@P\" }, \"start_location\" : { \"lat\" : 31.5279291, \"lng\" : 74.3346751 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.6 km\", \"value\" : 2636 }, \"duration\" : { \"text\" : \"3 mins\", \"value\" : 188 }, \"end_location\" : { \"lat\" : 31.5063223, \"lng\" : 74.3142873 }, \"html_instructions\" : \"\\u003cb\\u003eJustice A. R. Cornelius Underpass\\u003c/b\\u003e turns slightly \\u003cb\\u003eright\\u003c/b\\u003e and becomes \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by standard college oc Commerce canal bank (on the left in 1.3&nbsp;km)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"qdl_Em|tdMNTFF^^t@j@fAv@z@r@hBlB`B~AJPFH^\\\\VR\\\\VbAv@fA~@f@^j@`@b@Zx@l@d@`@rApAt@z@?@XZX\\\\f@l@b@d@PP\\\\XVXXZx@~@VVVZ|A|Al@j@|@x@p@n@p@p@r@p@fB~ARRdAfAdAx@bCtB~FfFZXDDr@j@@@`D~CnChCfAbAtAvAhCbCjE~D|CxCfCzB`@ZjAhA\" }, \"start_location\" : { \"lat\" : 31.524734, \"lng\" : 74.331749 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 658 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.5017064, \"lng\" : 74.309956 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eWaris Mir Underpass\\u003c/b\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"oqh_EioqdMbCbCjB|AdAx@tAhAn@l@@@f@d@f@d@DDh@h@pEhE|@x@jAjA\" }, \"start_location\" : { \"lat\" : 31.5063223, \"lng\" : 74.3142873 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.0 km\", \"value\" : 2031 }, \"duration\" : { \"text\" : \"2 mins\", \"value\" : 140 }, \"end_location\" : { \"lat\" : 31.4882726, \"lng\" : 74.29552889999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Bus Stop (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"utg_EgtpdMhCjC^\\\\bC`CfCzBfDpCfEbEvBnB|@|@tFdFvC~ChChCfBlBrAxAb@h@LPr@|@t@x@`HhInBlCd@l@`B`CvAnBLNjAhBb@v@bAbB\" }, \"start_location\" : { \"lat\" : 31.5017064, \"lng\" : 74.309956 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 511 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 33 }, \"end_location\" : { \"lat\" : 31.4854954, \"lng\" : 74.2912451 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"u`e_EazmdM^f@f@p@d@t@v@nAt@jA@@h@`AR^R\\\\P\\\\b@v@d@z@Pb@HNp@xARb@z@dB\" }, \"start_location\" : { \"lat\" : 31.4882726, \"lng\" : 74.29552889999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.8 km\", \"value\" : 2815 }, \"duration\" : { \"text\" : \"3 mins\", \"value\" : 193 }, \"end_location\" : { \"lat\" : 31.4759725, \"lng\" : 74.26396870000001 }, \"html_instructions\" : \"Continue onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by The Pelham Lahore (on the right in 2.2&nbsp;km)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"kod_Ei_mdMLVb@x@jD`IR\\\\BDz@nBzAfDdCrGhEzLn@nBP|@rAdD~AbFz@nC`@nBx@lEFNJR`@bBp@bDr@bDx@`EHZH`@^xB~@dFr@bElA~IBNBJDVJv@l@vEDZfCxQRpA`@nCl@xD\" }, \"start_location\" : { \"lat\" : 31.4854954, \"lng\" : 74.2912451 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"1.5 km\", \"value\" : 1509 }, \"duration\" : { \"text\" : \"2 mins\", \"value\" : 99 }, \"end_location\" : { \"lat\" : 31.4725884, \"lng\" : 74.248559 }, \"html_instructions\" : \"Continue straight to stay on \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Law mart (on the left in 1.0&nbsp;km)\\u003c/div\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"ysb_EytgdMtAnIJp@NdAhAnKr@bGF`@F^RvA|CtUHf@Jp@bBhL\\\\pCxAfL\" }, \"start_location\" : { \"lat\" : 31.4759725, \"lng\" : 74.26396870000001 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 586 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 38 }, \"end_location\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eThokar Niaz Baig Flyover\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eDestination will be on the right\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"u~a_EotddMj@pEj@lEd@dCb@jCNhAr@~Fb@nD\" }, \"start_location\" : { \"lat\" : 31.4725884, \"lng\" : 74.248559 }, \"travel_mode\" : \"DRIVING\" } ], \"traffic_speed_entry\" : [], \"via_waypoint\" : [] } ], \"overview_polyline\" : { \"points\" : \"{ex_Eg{ueMb@`D^nBVx@n@zCPx@TbBz@vHxBpQXvChB|Nz@jHhCpTXbCb@lCR~BB\\\\H\\\\EJMPAPCR?PJt@nC~T`BjNzAvLt@fHfAbI~A~Mf@`EpAzKh@xDrAbL~@pIx@zGtAhLn@nFr@xFx@lI`AfIfBdOnDbZ~B`RdCdRB`Ar@|EVdBT|Af@~D^~EX`BbA`JdApJxBbPJhBn@tGd@|D`AhIh@|DbA~GnAxIxA`KlA|Ln@lGpAdNlA|H~A`Nz@rGrCdSj@jEh@vGdC`UnA~J\\\\nEfAfILp@j@bGt@hH^tCnBvNDlAz@`HXfBp@pE^bDp@pHd@|DDVFNXzBj@zBCRAh@`AzF`@lDt@hEpB~KlA|EvCfKdA~ChBbFPXb@zAh@pAfAzBzA`DnD|G`AvAlAdB\\\\r@f@r@tBfCfInJtE`FpCnCjB~AdIzHnBrBxFrEbBpAxAtAfDxCdD`DtFvFjGxFzC|CxDnDjGvF|ElEx@v@RHh@`@fA|@vB`BjB~An@n@~@|@tDdD|@dATh@jBpBpDhD`DvC~@x@fDtC^\\\\^d@lAzAZXn@b@xC|BHRh@j@rBzArAdAhBdBzD|Dn@h@JF`@PNTf@f@|BbBz@r@hBlB`B~AJPf@f@t@j@jCvBrA`A|AhAxBrBhBvBjArAn@j@bClCtBxBjBdB~FrFxAzAhEnDtIrHzJnJ~EzEhJxIhDvCnElEpDvCdCvBvArAdKxJhDhDjG|FfDpCfEbEtDlDtFdFvC~ChChCfBlBvBbC`AnAvIbKtCzDxDpFxAxBfBzCfAxAtCrEbB|ChArBZr@nCzFnEzJVb@vCvGdCrGhEzLn@nBP|@rAdD~AbFz@nC`@nB`A|EJR`@bBdBfIlBxJrBhLpAnJbArHbEvY~C`ShAnKr@bGN`ApDlXTxA`CzPdCxRj@lEd@dCr@tEvAnL\" }, \"summary\" : \"Canal Bank Rd\", \"warnings\" : [], \"waypoint_order\" : [] } ], \"status\" : \"OK\" } ";
//
//        final PolylineOptions polylineOptions = new PolylineOptions().clickable(true);
//        final RouteResponse routeResponse =  new Gson().fromJson(jsonString, RouteResponse.class);
//        for (Step step : routeResponse.routes[0].legs[0].steps) {
//            polylineOptions.add(new LatLng(step.start_location.lat, step.start_location.lng));
//        }
//        googleMap.addPolyline(polylineOptions);
//        // Set listeners for click events.
////        googleMap.setOnPolylineClickListener(this);
//    }

    public void drawCanalRoad() {
        List<LatLng> listLatLong = OverSpeedUtil.getCanalRoadPolyLine();

        LatLng[] pointsArray = new LatLng[listLatLong.size()];
        pointsArray = listLatLong.toArray(pointsArray);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(android.R.color.darker_gray));
        polylineOptions.add(pointsArray);
        googleMap.addPolyline(polylineOptions);

//        try {
//            if (polylineCanalRoad == null) {
//                drawOnMap(new MyParser().parse());
//            }
//        } catch (RouteException e) {
//            e.printStackTrace();
//        }
    }

//    public void drawOnMap(List<Route> route) {
//
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.color(getResources().getColor(android.R.color.black));
//
//        LatLng[] pointsArray = new LatLng[route.get(0).getPoints().size()];
//        pointsArray = route.get(0).getPoints().toArray(pointsArray);
//
//        StringBuilder stringBuilder = new StringBuilder();
//
////        final List<Segment> segments = route.get(0).getSegments();
////        final LatLng[] pointsArray = new LatLng[segments.size()];
////        for (Segment segment : segments) {
////            stringBuilder.append("add(new LatLng("+segment.startPoint().latitude+", "+segment.startPoint().longitude+"));\n");
////        }
//
//        for (LatLng latLng : pointsArray) {
//            stringBuilder.append("add(new LatLng("+latLng.latitude+", "+latLng.longitude+"));\n");
//        }
//
//        android.util.Log.i("Farhan", stringBuilder.toString());
//        FileLogger.write(getActivity(), stringBuilder.toString());
//
//        polylineOptions.add(pointsArray);
//
//        polylineCanalRoad = googleMap.addPolyline(polylineOptions);
//
////        // Start marker
////        MarkerOptions options = new MarkerOptions();
////        options.position(start);
////        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
////        map.addMarker(options);
////
////        // End marker
////        options = new MarkerOptions();
////        options.position(end);
////        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
////        map.addMarker(options);
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationUpdateOverSpeedEvent event) {
        final float userSpeed = MapUtil.getNormalizedSpeed(event.userLocation);
        binding.tvSpeed.setText(Math.round(userSpeed) + " kmph");

        if (lastUserLocation == null) {
            // Plot user location on map
            final LatLng userLatLng = new LatLng(event.userLocation.getLatitude(), event.userLocation.getLongitude());

            // Add a marker to user userLocation and move the camera
            googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng).title("User location")
                    .icon(MapUtil.getMarkerIcon(getPinColorBySpeed(event.overSpeedResult, userSpeed))));

            // Move the camera to the user's userLocation and zoom in!
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16.0f));
        }

        if (!TrackerUtils.isValuableSpeedDiff(userSpeed, lastUserLocation, event.userLocation)) {
            return;
        }

        lastUserLocation = event.userLocation;
        final LatLng userLatLng = new LatLng(event.userLocation.getLatitude(), event.userLocation.getLongitude());

        // Add a marker to user userLocation and move the camera
        googleMap.addMarker(new MarkerOptions()
                .position(userLatLng).title(Math.round(userSpeed) + " kmph")
                .icon(MapUtil.getMarkerIcon(getPinColorBySpeed(event.overSpeedResult, userSpeed))));

        // Move the camera to the user's userLocation and zoom in!
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16.0f));
    }

    public static String getPinColorBySpeed(@Nullable OverSpeedResult result, float userSpeed) {
        if (result != null && result.isOverSpeed) {
            return "#FF4500";
        }
        if (userSpeed > 60) {
            return "#ffd0c1";
//            return "#e4ffc1";
        }
        if (userSpeed > 50) {
            return "#e4ffc1";
        }
        return "#fbffc1";
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(OverSpeedEvent event) {
//        final OverSpeedResult result = event.overSpeedResult;
//
//        ttsManager.speak("Please avoid over speeding");
//
//        // Add a marker to user userLocation and move the camera
//        googleMap.addMarker(new MarkerOptions()
//                .position(result.userLatLong).title(Math.round(result.userSpeedNormalized) + " kmph")
//                .icon(MapUtil.getMarkerIcon(MapUtil.OVER_SPEED_COLOR)));
//
//        // Move the camera to the user's userLocation and zoom in!
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(result.userLatLong, 16.0f));
//    }
//
//    public void onUserLocationUpdate(@NonNull Location userLocation) {
//        final float userSpeed = MapUtil.getNormalizedSpeed(userLocation);
//
//        binding.tvSpeed.setText(Math.round(userSpeed) + " kmph");
//
//        // If speed is not good candidate for speed check
//        if (userSpeed < 45f || !speedDiffExceeds1Kmph(userLocation)) {
//            return;
//        }
//
////        lastUserLocation = userLocation;
//
//        final LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
//
//        // Add a marker to user userLocation and move the camera
//        googleMap.addMarker(new MarkerOptions()
//                .position(userLatLng).title(Math.round(userSpeed) + " kmph")
//                .icon(MapUtil.getMarkerIcon(MapUtil.getColorForSpeed(userSpeed)));
//
//        // Move the camera to the user's userLocation and zoom in!
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16.0f));
//
////        final int speedLimit = MonitorOverSpeed.getSpeedLimitOfLocation(userLocation);
////        if (userSpeed > speedLimit) {
////            ttsManager.speak("You are doing over userSpeed. Speed limit is " + speedLimit + ". Your userSpeed is " + userSpeed + ".");
////        } else if (userSpeed > 78) {
////            ttsManager.speak("Your userSpeed is " + Math.round(userSpeed));
////        }
//    }

//    public boolean speedDiffExceeds1Kmph(@NonNull Location location) {
//        if (lastUserLocation == null) {
//            return true;
//        }
//        return Math.abs(MapUtil.getNormalizedSpeed(lastUserLocation) -
//                MapUtil.getNormalizedSpeed(location)) > 1;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ttsManager.onDestroy();
        EventBus.getDefault().unregister(this);
        DataInputManager.stopTakingLocationUpdates(getContext().getApplicationContext());
    }
}
