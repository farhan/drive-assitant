package com.travel.driveassistant.lib_data_util.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import driveassitant.travel.com.lib_data_util.R;

public class DataUtilActivity extends AppCompatActivity {

//    private String API_KEY = "AIzaSyDAGc6Lc6U_pHEe2mTEl_Jre6yREoDPwIw";
    private String API_KEY = "AIzaSyCTSfK7O-L_9Y5QmzHCKEla-wE405AFr4Q";

    private String OUTPUT_FORMAT = "json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_util);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DownloadImageTask().execute();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urls) {
//            final String jsonString = serverCall(getDirectionsApiURL());
//            final String jsonString = "{ \"geocoded_waypoints\" : [ { \"geocoder_status\" : \"OK\", \"place_id\" : \"ChIJTZafxAESGTkRTh97ji7Kyw4\", \"types\" : [ \"route\" ] }, { \"geocoder_status\" : \"OK\", \"place_id\" : \"ChIJmUP2uCICGTkRKo1LP3_BOs4\", \"types\" : [ \"establishment\", \"park\", \"point_of_interest\" ] } ], \"routes\" : [ { \"bounds\" : { \"northeast\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"southwest\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 } }, \"copyrights\" : \"Map data ©2019 Google\", \"legs\" : [ { \"distance\" : { \"text\" : \"28.9 km\", \"value\" : 28856 }, \"duration\" : { \"text\" : \"38 mins\", \"value\" : 2286 }, \"end_address\" : \"Thokar Niaz Biag Canal Park, Thokar Niaz Baig Flyover, Irrigation Colony, Lahore, Punjab, Pakistan\", \"end_location\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 }, \"start_address\" : \"Canal Bank Rd, Khaira, Lahore, Punjab, Pakistan\", \"start_location\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"steps\" : [ { \"distance\" : { \"text\" : \"6.1 km\", \"value\" : 6129 }, \"duration\" : { \"text\" : \"11 mins\", \"value\" : 656 }, \"end_location\" : { \"lat\" : 31.5736246, \"lng\" : 74.4376965 }, \"html_instructions\" : \"Head \\u003cb\\u003ewest\\u003c/b\\u003e on \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by the gas station (on the left)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"{ex_Eg{ueML|@RzA@FFVHh@DRFXDNPh@ZvARbAHZF\\\\LbAF^\\\\fD\\\\nClBlOJbAD^RvBhAfJ^tCRdBPrALjAFd@Db@BLP|AlB`PDb@Fd@BRFd@BRH\\\\Hf@Jr@BZB^Db@D^@T@FH\\\\EJMPAPAJAFAH@F@JHh@LbAf@dEj@vEl@|E@Ft@dGZpCLjAP~AhAvIt@fHfAbIXzBdAbJZfCJx@NjA`AnIXnBNhAf@hEj@xE^lD^bDJx@X~BDXLfAb@`Ep@fFb@tDJx@PvA`@`Dj@fGLdA^xCJ|@@NR~APpAh@rE^vCJfAb@tDD\\\\xAnLj@~Ez@pGbAnIdCdR\" }, \"start_location\" : { \"lat\" : 31.5863759, \"lng\" : 74.500525 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 486 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 31 }, \"end_location\" : { \"lat\" : 31.5726208, \"lng\" : 74.4327134 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eJoginder Lal Mandal Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Pak Mobile Point (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"cvu_EsrieMB`ANdAb@vCV`B?BPpABJRzARbBBVZfEX`B\" }, \"start_location\" : { \"lat\" : 31.5736246, \"lng\" : 74.4376965 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"4.7 km\", \"value\" : 4716 }, \"duration\" : { \"text\" : \"6 mins\", \"value\" : 375 }, \"end_location\" : { \"lat\" : 31.56285849999999, \"lng\" : 74.38430079999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Decent uniforms shop (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"{ou_EmsheMbA`J|@fIFh@Hb@nB~N@RHtAZvCR|BHx@D`@DZNdARtALhAB^ZhCRbBTxA\\\\zBd@bDT~ABRPpAb@rCNdAD\\\\BPJn@TxA?@NdALx@fAvKDd@RtBZvCXfCFx@D\\\\h@dGDTd@|C`@fC?@l@pEp@nGf@tDR|AXtBd@bDjA`IFh@PpAXxBh@vGdC`Up@xF\\\\dCX~DBNV`Bn@dFHb@BLNtADl@Fr@LjALnAf@xER|AJv@VzBvAzJ\" }, \"start_location\" : { \"lat\" : 31.5726208, \"lng\" : 74.4327134 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 673 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.5614809, \"lng\" : 74.3773941 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eHabba Khatoon Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Pasha Tyres (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"{rs_E{d_eMBv@@Tl@dFLz@Lp@Jt@V~ABJL|@Ff@Ff@@DHn@JdALrAb@|Eb@rD@H@JBJFN\" }, \"start_location\" : { \"lat\" : 31.56285849999999, \"lng\" : 74.38430079999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.1 km\", \"value\" : 124 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 9 }, \"end_location\" : { \"lat\" : 31.5611333, \"lng\" : 74.37615049999999 }, \"html_instructions\" : \"Continue straight onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e/\\u003cb\\u003ePeer Muhammad Sadiq Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eDrive along Irrigation Deparment Workshops (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"gjs_Euy}dMXzBj@zB\" }, \"start_location\" : { \"lat\" : 31.5614809, \"lng\" : 74.3773941 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"1.1 km\", \"value\" : 1083 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 71 }, \"end_location\" : { \"lat\" : 31.5577252, \"lng\" : 74.36553789999999 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Directorate of Land Reclamation Punjab (on the left in 1.0&nbsp;km)\\u003c/div\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"ahs_E}q}dMCRAP?VBX|@`F`@lDt@hE\\\\lBrApH`A|DJ^vCfKdA~CzAjELVJL\" }, \"start_location\" : { \"lat\" : 31.5611333, \"lng\" : 74.37615049999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 550 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 41 }, \"end_location\" : { \"lat\" : 31.5548863, \"lng\" : 74.360805 }, \"html_instructions\" : \"Continue straight onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e/\\u003cb\\u003ePeer Muhammad Sadiq Rd\\u003c/b\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"yrr_Eso{dMDJFNZjAh@pAfAzBp@xAh@fA^n@dApBj@hA\\\\p@V^h@v@JN`AtA\" }, \"start_location\" : { \"lat\" : 31.5577252, \"lng\" : 74.36553789999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 729 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 48 }, \"end_location\" : { \"lat\" : 31.5501086, \"lng\" : 74.3555664 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e at jabbar pan shop onto \\u003cb\\u003eHussain Shaheed Soherwordi Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"aar_E_rzdMDLVd@DF`@j@LPVZd@j@h@l@JLPThApAn@r@RV`AfAJLlAvAb@f@j@l@p@p@rAxA\\\\^rBnBnAbA\" }, \"start_location\" : { \"lat\" : 31.5548863, \"lng\" : 74.360805 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 578 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.54602659999999, \"lng\" : 74.35180389999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Spectrum Publisher (on the left in 300&nbsp;m)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"ecq_EiqydMZZvDnDlCjC`A`Al@p@|CbCLLh@b@b@\\\\bBpA\" }, \"start_location\" : { \"lat\" : 31.5501086, \"lng\" : 74.3555664 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 593 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 42 }, \"end_location\" : { \"lat\" : 31.5419067, \"lng\" : 74.3478362 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eFaiz Ahmed Faiz Underpass\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Maktaba Tul Madinah (on the right)\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"uip_EwyxdMxAtAfDxCdD`DtBvB~B~BxClC\" }, \"start_location\" : { \"lat\" : 31.54602659999999, \"lng\" : 74.35180389999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 718 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 55 }, \"end_location\" : { \"lat\" : 31.5368876, \"lng\" : 74.343076 }, \"html_instructions\" : \"Continue onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by the bridge (on the right in 450&nbsp;m)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"}oo_E_axdMpBjBx@|@`B~AdA`ArBlB|BtBl@j@b@^z@t@hC~BrAlAx@v@\" }, \"start_location\" : { \"lat\" : 31.5419067, \"lng\" : 74.3478362 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 527 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 48 }, \"end_location\" : { \"lat\" : 31.5331892, \"lng\" : 74.3396362 }, \"html_instructions\" : \"Slight \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eWaris Shah Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-left\", \"polyline\" : { \"points\" : \"qpn_EgcwdMRHPLVRTNp@l@b@\\\\VRl@b@LJx@n@JHXXJJBBHJPNNNv@v@FDtApA~ArAX\\\\b@f@HNJX\" }, \"start_location\" : { \"lat\" : 31.5368876, \"lng\" : 74.343076 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.8 km\", \"value\" : 752 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 58 }, \"end_location\" : { \"lat\" : 31.5279291, \"lng\" : 74.3346751 }, \"html_instructions\" : \"Slight \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-left\", \"polyline\" : { \"points\" : \"mym_EwmvdMRVTVZ\\\\d@b@t@r@r@p@BBbA~@rBjBl@j@\\\\Z`@\\\\h@d@p@l@^\\\\j@b@JJRPJLRVLPNPX\\\\TXXV@@VRVNFHpCrB\" }, \"start_location\" : { \"lat\" : 31.5331892, \"lng\" : 74.3396362 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 452 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 32 }, \"end_location\" : { \"lat\" : 31.524734, \"lng\" : 74.331749 }, \"html_instructions\" : \"Slight \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eJustice A. R. Cornelius Underpass\\u003c/b\\u003e\", \"maneuver\" : \"turn-slight-right\", \"polyline\" : { \"points\" : \"qxl_EwnudMDJBFDDFFNPJJNLZT^Vf@^NL^XJHVRj@j@ZX@@^\\\\pApAhBjBVRVTDBDB`@P\" }, \"start_location\" : { \"lat\" : 31.5279291, \"lng\" : 74.3346751 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.6 km\", \"value\" : 2636 }, \"duration\" : { \"text\" : \"3 mins\", \"value\" : 188 }, \"end_location\" : { \"lat\" : 31.5063223, \"lng\" : 74.3142873 }, \"html_instructions\" : \"\\u003cb\\u003eJustice A. R. Cornelius Underpass\\u003c/b\\u003e turns slightly \\u003cb\\u003eright\\u003c/b\\u003e and becomes \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by standard college oc Commerce canal bank (on the left in 1.3&nbsp;km)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"qdl_Em|tdMNTFF^^t@j@fAv@z@r@hBlB`B~AJPFH^\\\\VR\\\\VbAv@fA~@f@^j@`@b@Zx@l@d@`@rApAt@z@?@XZX\\\\f@l@b@d@PP\\\\XVXXZx@~@VVVZ|A|Al@j@|@x@p@n@p@p@r@p@fB~ARRdAfAdAx@bCtB~FfFZXDDr@j@@@`D~CnChCfAbAtAvAhCbCjE~D|CxCfCzB`@ZjAhA\" }, \"start_location\" : { \"lat\" : 31.524734, \"lng\" : 74.331749 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.7 km\", \"value\" : 658 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 43 }, \"end_location\" : { \"lat\" : 31.5017064, \"lng\" : 74.309956 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eWaris Mir Underpass\\u003c/b\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"oqh_EioqdMbCbCjB|AdAx@tAhAn@l@@@f@d@f@d@DDh@h@pEhE|@x@jAjA\" }, \"start_location\" : { \"lat\" : 31.5063223, \"lng\" : 74.3142873 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.0 km\", \"value\" : 2031 }, \"duration\" : { \"text\" : \"2 mins\", \"value\" : 140 }, \"end_location\" : { \"lat\" : 31.4882726, \"lng\" : 74.29552889999999 }, \"html_instructions\" : \"Merge onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Bus Stop (on the left)\\u003c/div\\u003e\", \"maneuver\" : \"merge\", \"polyline\" : { \"points\" : \"utg_EgtpdMhCjC^\\\\bC`CfCzBfDpCfEbEvBnB|@|@tFdFvC~ChChCfBlBrAxAb@h@LPr@|@t@x@`HhInBlCd@l@`B`CvAnBLNjAhBb@v@bAbB\" }, \"start_location\" : { \"lat\" : 31.5017064, \"lng\" : 74.309956 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.5 km\", \"value\" : 511 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 33 }, \"end_location\" : { \"lat\" : 31.4854954, \"lng\" : 74.2912451 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"u`e_EazmdM^f@f@p@d@t@v@nAt@jA@@h@`AR^R\\\\P\\\\b@v@d@z@Pb@HNp@xARb@z@dB\" }, \"start_location\" : { \"lat\" : 31.4882726, \"lng\" : 74.29552889999999 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"2.8 km\", \"value\" : 2815 }, \"duration\" : { \"text\" : \"3 mins\", \"value\" : 193 }, \"end_location\" : { \"lat\" : 31.4759725, \"lng\" : 74.26396870000001 }, \"html_instructions\" : \"Continue onto \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by The Pelham Lahore (on the right in 2.2&nbsp;km)\\u003c/div\\u003e\", \"polyline\" : { \"points\" : \"kod_Ei_mdMLVb@x@jD`IR\\\\BDz@nBzAfDdCrGhEzLn@nBP|@rAdD~AbFz@nC`@nBx@lEFNJR`@bBp@bDr@bDx@`EHZH`@^xB~@dFr@bElA~IBNBJDVJv@l@vEDZfCxQRpA`@nCl@xD\" }, \"start_location\" : { \"lat\" : 31.4854954, \"lng\" : 74.2912451 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"1.5 km\", \"value\" : 1509 }, \"duration\" : { \"text\" : \"2 mins\", \"value\" : 99 }, \"end_location\" : { \"lat\" : 31.4725884, \"lng\" : 74.248559 }, \"html_instructions\" : \"Continue straight to stay on \\u003cb\\u003eCanal Bank Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Law mart (on the left in 1.0&nbsp;km)\\u003c/div\\u003e\", \"maneuver\" : \"straight\", \"polyline\" : { \"points\" : \"ysb_EytgdMtAnIJp@NdAhAnKr@bGF`@F^RvA|CtUHf@Jp@bBhL\\\\pCxAfL\" }, \"start_location\" : { \"lat\" : 31.4759725, \"lng\" : 74.26396870000001 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0.6 km\", \"value\" : 586 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 38 }, \"end_location\" : { \"lat\" : 31.4712561, \"lng\" : 74.2425815 }, \"html_instructions\" : \"Keep \\u003cb\\u003eright\\u003c/b\\u003e to continue on \\u003cb\\u003eThokar Niaz Baig Flyover\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eDestination will be on the right\\u003c/div\\u003e\", \"maneuver\" : \"keep-right\", \"polyline\" : { \"points\" : \"u~a_EotddMj@pEj@lEd@dCb@jCNhAr@~Fb@nD\" }, \"start_location\" : { \"lat\" : 31.4725884, \"lng\" : 74.248559 }, \"travel_mode\" : \"DRIVING\" } ], \"traffic_speed_entry\" : [], \"via_waypoint\" : [] } ], \"overview_polyline\" : { \"points\" : \"{ex_Eg{ueMb@`D^nBVx@n@zCPx@TbBz@vHxBpQXvChB|Nz@jHhCpTXbCb@lCR~BB\\\\H\\\\EJMPAPCR?PJt@nC~T`BjNzAvLt@fHfAbI~A~Mf@`EpAzKh@xDrAbL~@pIx@zGtAhLn@nFr@xFx@lI`AfIfBdOnDbZ~B`RdCdRB`Ar@|EVdBT|Af@~D^~EX`BbA`JdApJxBbPJhBn@tGd@|D`AhIh@|DbA~GnAxIxA`KlA|Ln@lGpAdNlA|H~A`Nz@rGrCdSj@jEh@vGdC`UnA~J\\\\nEfAfILp@j@bGt@hH^tCnBvNDlAz@`HXfBp@pE^bDp@pHd@|DDVFNXzBj@zBCRAh@`AzF`@lDt@hEpB~KlA|EvCfKdA~ChBbFPXb@zAh@pAfAzBzA`DnD|G`AvAlAdB\\\\r@f@r@tBfCfInJtE`FpCnCjB~AdIzHnBrBxFrEbBpAxAtAfDxCdD`DtFvFjGxFzC|CxDnDjGvF|ElEx@v@RHh@`@fA|@vB`BjB~An@n@~@|@tDdD|@dATh@jBpBpDhD`DvC~@x@fDtC^\\\\^d@lAzAZXn@b@xC|BHRh@j@rBzArAdAhBdBzD|Dn@h@JF`@PNTf@f@|BbBz@r@hBlB`B~AJPf@f@t@j@jCvBrA`A|AhAxBrBhBvBjArAn@j@bClCtBxBjBdB~FrFxAzAhEnDtIrHzJnJ~EzEhJxIhDvCnElEpDvCdCvBvArAdKxJhDhDjG|FfDpCfEbEtDlDtFdFvC~ChChCfBlBvBbC`AnAvIbKtCzDxDpFxAxBfBzCfAxAtCrEbB|ChArBZr@nCzFnEzJVb@vCvGdCrGhEzLn@nBP|@rAdD~AbFz@nC`@nB`A|EJR`@bBdBfIlBxJrBhLpAnJbArHbEvY~C`ShAnKr@bGN`ApDlXTxA`CzPdCxRj@lEd@dCr@tEvAnL\" }, \"summary\" : \"Canal Bank Rd\", \"warnings\" : [], \"waypoint_order\" : [] } ], \"status\" : \"OK\" } ";

//            Log.i("Farhan", jsonString);

//            RouteResponse routeResponse =  new Gson().fromJson(jsonString, RouteResponse.class);

            return null;
        }
    }

    protected String getDirectionsApiURL() {
        LatLng start = new LatLng(31.586412, 74.500520);
        LatLng dest = new LatLng(31.471282, 74.242574);

        final String sJsonURL = "https://maps.googleapis.com/maps/api/directions/" + OUTPUT_FORMAT + "?";

        final StringBuffer mBuf = new StringBuffer(sJsonURL);
        mBuf.append("origin=");
        mBuf.append(start.latitude);
        mBuf.append(',');
        mBuf.append(start.longitude);
        mBuf.append("&destination=");
        mBuf.append(dest.latitude);
        mBuf.append(',');
        mBuf.append(dest.longitude);
        mBuf.append("&sensor=true&mode=driving");
        mBuf.append("&key=");
        mBuf.append(API_KEY);

        return mBuf.toString();
    }

    public String serverCall(@NonNull String urlStr) {
        try {
            final URL url = new URL(urlStr);
            final URLConnection urlConnection = url.openConnection();
            final InputStream in = urlConnection.getInputStream();
            final String stream = getStringFromInputStream(in);
            return stream;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader bufferReader = null;
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            bufferReader = new BufferedReader(new InputStreamReader(is));
            while ((line = bufferReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }
}
