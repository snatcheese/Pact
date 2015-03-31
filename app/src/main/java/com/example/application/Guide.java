package com.example.application;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Guide extends FragmentActivity implements
        android.location.LocationListener {

    public final static String keyGuide = "key_to_Guide";

    private ArrayList<LatLng> tableauIndic = new ArrayList<LatLng>();

    private LatLng myPosition;
    private boolean isFull = false;
    private LocationManager lm;
    private GoogleMap mMap;
    private Marker myMarkerPosition;
    private boolean done = false;
    private ArrayList<Indication> retour = new ArrayList<Indication>();
    private ArrayList<String> data2 = new ArrayList<String>();
    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
    private Polyline polylineRandom;
    private boolean isInstructionSet = false;
    private LatLng currentCheckPoint;
    private LatLng currentCheckPointLost;
    private boolean isLost = false;
    private int indiceLost = 0;
    private int indiceNotLost = 1;
    private double currentTolerance = 0.00;
    private double currentToleranceLost = 0.00;
    private LatLng Paris = new LatLng(48.8534100, 2.3488000);
    private double testAngle = 0;
    private double length = 0;
    private  ArrayList<Direction> directionFinal = new ArrayList<Direction>();
    private  ArrayList<Direction> directionFinalFinal = new ArrayList<Direction>();
    private boolean besoin = true;
    private boolean need = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        setUpMapIfNeeded();


        mMap.setMyLocationEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 13));


        Intent intent = getIntent();

        tableauIndic = intent.getParcelableArrayListExtra(keyGuide);

        String url = ConnectionPath.getURLConnection(tableauIndic);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

        }
    }

    private void setUpMap() {

        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0))
                .title("Marker").snippet("Snippet"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0,
                    this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0,
                this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {

        myPosition = new LatLng(location.getLatitude(), location.getLongitude());

        if (done) {

            myMarkerPosition.remove();
            done = false;
            guidage(myPosition);

        }

        myMarkerPosition = mMap.addMarker(new MarkerOptions().position(
                myPosition).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        done = true;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 16));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class ReadTask extends AsyncTask<String, Integer, String> {

		/*
		 * @Override protected void onPreExecute() { super.onPreExecute();
		 * Toast.makeText(getApplicationContext(),
		 * "Début du traitement asynchrone", Toast.LENGTH_LONG).show(); }
		 *
		 * @Override protected void onProgressUpdate(Integer... values){
		 * super.onProgressUpdate(values); bar.setVisibility(View.VISIBLE); //
		 * Mise à jour de la ProgressBar bar.setProgress(values[0]); }
		 */

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // bar.setVisibility(View.INVISIBLE);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask
            extends
            AsyncTask<String, Void, ArrayList<ArrayList<HashMap<String, String>>>> {

        @Override
        protected ArrayList<ArrayList<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            ArrayList<ArrayList<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
                data2 = parser.getInfo(jObject);
                data3 = parser.getInfo2(jObject);
                // System.out.println("parseur");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(
                ArrayList<ArrayList<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                ArrayList<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    // System.out.println("derniere boucle for");
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }
            Log.i("ParserTask", "Poly");

            // Initialiser les objet pour le retour
            data2 = RetirerInformation.getinfo(data2);


            retour.clear();
            directionFinal.clear();


            if (data2.size() == data3.size()) {

                retour.add(new Indication(tableauIndic.get(0), "début "));


                for (int i = 0; i < data2.size() - 1; i++) {

                    if (SphericalUtil.computeDistanceBetween(retour.get(retour.size() - 1).getLatLng(), data3.get(i)) < 35) {

                    } else {

                        retour.add(new Indication(data3.get(i), data2
                                .get(i + 1)));
                    }

                }

                if (!(retour.get(retour.size() - 1).getLatLng().equals(tableauIndic.get(tableauIndic.size() - 1)))) {
                    retour.add(new Indication(tableauIndic.get(tableauIndic.size() - 1), "fin"));
                }


            }


            ArrayList<Direction> direction = new ArrayList<Direction>();

            for (int j = 0; j < retour.size(); j++) {
                if (j == 0) {
                    direction.add(new Direction(tableauIndic.get(0), 0.00, retour.get(j).getInstruction()));
                } else if (j != (retour.size() - 1)) {
                    testAngle = 180 - SphericalUtil.computeHeading(retour.get(j - 1).getLatLng(), retour.get(j).getLatLng()) + SphericalUtil.computeHeading(retour.get(j).getLatLng(), retour.get(j + 1).getLatLng());


                    direction.add(new Direction(retour.get(j).getLatLng(), testAngle, retour.get(j).getInstruction()));
                } else {
                    direction.add(new Direction(retour.get(j).getLatLng(), 0.00, retour.get(j).getInstruction()));
                }
            }

            // myPosition = new LatLng( retour.get(0).getLat() ,
            // retour.get(0).getLng());

            for (int p = 0; p < direction.size(); p++) {
                if (p == 0) {

                } else if (p == (direction.size() - 1)) {

                } else {
                    testAngle = direction.get(p).getAngle();

                    if (testAngle > 360) {
                        testAngle = testAngle - 360;
                    }

                    if ((testAngle <= 180 && testAngle >= 0) || (testAngle < -180 && testAngle > -360)) {

                        if ((testAngle >= 150) && (testAngle <= 180)) {

                            direction.get(p).setInfo("tout droit");

                        } else if ((testAngle <= (-180)) && (testAngle >= (-210))) {

                            direction.get(p).setInfo("tout droit");

                        } else {

                            direction.get(p).setInfo("gauche");

                        }
                    } else if ((testAngle < 360 && testAngle > 180) || (testAngle <= 0 && testAngle >= -180)) {

                        if ((testAngle >= 180) && (testAngle <= 210)) {

                            direction.get(p).setInfo("tout droit");

                        } else if (testAngle <= (-150) && testAngle >= (-180)) {

                            direction.get(p).setInfo("tout droit");

                        } else {

                            direction.get(p).setInfo("droite");
                        }
                    } else {

                        direction.get(p).setInfo("gauche");

                    }


                }

            }


            for (int y = 0; y < direction.size(); y++) {
                if (!(direction.get(y).getInfo().contains("tout droit"))) {
                    directionFinal.add(direction.get(y));
                }
            }
             isFull = true;

            isInstructionSet = true;

            polylineRandom = mMap.addPolyline(polyLineOptions);

            currentCheckPoint = directionFinal.get(1).getLatlng();

            currentTolerance = 1.1 * SphericalUtil.computeDistanceBetween( directionFinal.get(0).getLatlng() , currentCheckPoint );


            for (int i = 0; i < directionFinal.size(); i++) {

                mMap.addMarker(new MarkerOptions().position(directionFinal.get(i).getLatlng()).draggable(true));
                System.out.println(directionFinal.get(i).getInfo());

            }


        }

    }

    public double getLength(ArrayList<Indication> retour) {
        int n = 0;

        while (!(retour.get(n + 1).getInstruction().contains("fin"))) {
            length = length + SphericalUtil.computeDistanceBetween(retour.get(n).getLatLng(), retour.get(n + 1).getLatLng());
            n++;
        }

        return length;
    }

    public void guidage(LatLng position) {
        if (!(isLost)) {


            if( besoin )
            {
                directionFinalFinal = directionFinal;
                besoin = false;
            }

            if ( SphericalUtil.computeDistanceBetween( position, currentCheckPoint) < 15 ) {

                Toast toast = Toast.makeText(this, "moins de 35 metres",
                        Toast.LENGTH_SHORT);
                toast.show();

                if (isFull) {

                    if (indiceNotLost == (directionFinalFinal.size() - 1)) {

                        Intent intent = new Intent(Guide.this, Enregistrer_Parcours.class);
                        intent.putParcelableArrayListExtra(keyGuide, tableauIndic);
                        startActivity(intent);
                        finish();

                    }

                    String indication = directionFinalFinal.get(indiceNotLost).getInfo();
                    vibrer(indication);
                    indiceNotLost++;
                    currentCheckPoint = directionFinal.get(indiceNotLost).getLatlng();
                    currentTolerance = 1.1 * SphericalUtil.computeDistanceBetween(position, currentCheckPoint); // on change de tolérance

                }
            }
            else if ( SphericalUtil.computeDistanceBetween( position , currentCheckPoint) > currentTolerance)
            {
                isLost = true ;
                ArrayList<LatLng> tableauLost = new ArrayList<LatLng>();
                tableauLost.add(position);
                tableauLost.add(currentCheckPoint);
                String url = ConnectionPath.getURLConnection(tableauLost);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
            }

        } else {

            if( need )
            {
               currentCheckPointLost = directionFinal.get(1).getLatlng();
               currentToleranceLost = 1.1 * SphericalUtil.computeDistanceBetween( position , currentCheckPointLost );
            }

            if ( SphericalUtil.computeDistanceBetween( position, currentCheckPointLost) < 15 ) {


                if (indiceLost == (directionFinal.size() - 1)) {

                    isLost = false;
                    //on passera à nouveau dans le bloc !(isLost)

                }
                else
                {

                    String indication = directionFinal.get(indiceNotLost).getInfo();
                    vibrer(indication);
                    indiceLost++;
                    currentCheckPointLost = directionFinal.get(indiceNotLost).getLatlng();
                    currentTolerance = 1.1 * SphericalUtil.computeDistanceBetween(position, currentCheckPointLost); // on change de tolérance

                }

            }
            else if ( SphericalUtil.computeDistanceBetween( position , currentCheckPointLost) > currentToleranceLost)
            {
                //on va faire vibrer les deux chaussures pour que la personne regarde son téléphone...
            }




        }
    }

    public void vibrer(String instruction) {

        if (instruction.contains("droite")) {
            Toast toast = Toast.makeText(this, "droite", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if( instruction.contains("gauche"))
        {
            Toast toast = Toast.makeText(this, "gauche", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if( instruction.contains("fin"))
        {
            Toast toast = Toast.makeText(this, "fin", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
