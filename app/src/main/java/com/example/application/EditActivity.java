package com.example.application;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.List;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class EditActivity extends FragmentActivity {

	private static final double EARTH_RADIUS = 6371000;
	private GoogleMap mMap; // Might be null if Google Play services APK is not
							// available.
	private Marker marker;
	private LatLng Paris = new LatLng(48.8534100, 2.3488000);
	ArrayList<LatLng> monTableauEdit = new ArrayList<LatLng>();
	ArrayList<Marker> monTableauMarkerEdit = new ArrayList<Marker>();
	private double randomLong;
	private Polyline polyline;
	private int i = 0;
	private Marker mMarker;
	public final static String key = "key_to_SavePath";
	public final static String keyGuide = "key_to_Guide";
	private double length;
	
	private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		setUpMapIfNeeded();



		mMap.addMarker(new MarkerOptions().position(Paris).draggable(true));

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 13));


		final View buttonText = findViewById(R.id.button1);
		Typeface typeFaceButton = CustomFontsLoader.getTypeface(
				EditActivity.this, 2);
		if (typeFaceButton != null)
			((TextView) buttonText).setTypeface(typeFaceButton);

		final View buttonText1 = findViewById(R.id.button2);
		Typeface typeFaceButton1 = CustomFontsLoader.getTypeface(
				EditActivity.this, 2);
		if (typeFaceButton1 != null)
			((TextView) buttonText1).setTypeface(typeFaceButton1);
		
		final View buttonText2 = findViewById(R.id.buttonSave);
		Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(
				EditActivity.this, 2);
		if (typeFaceButton2 != null)
			((TextView) buttonText2).setTypeface(typeFaceButton2);

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			// Lorsqu'on clique sur la map
			@Override
			public void onMapClick(LatLng latlng) {

				if (i == 0) {

					if (monTableauMarkerEdit.size() < 8) {

						int p = monTableauMarkerEdit.size();

						if (monTableauMarkerEdit.size() == 0) {

							monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
									.position(latlng)
									.title("D�part")
									.snippet("La souffrance commence ici!")
									.draggable(true)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
							monTableauEdit.add(latlng);

						} else if (monTableauMarkerEdit.size() == 7) {
							monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
									.position(latlng)
									.title("Arriv�e")
									.snippet("La souffrance termine ici!")
									.draggable(true)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
							monTableauEdit.add(latlng);
						} else {

							monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
									.position(latlng)
									.title("point de passage" + " "
											+ Integer.toString(p))
									.draggable(true)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
							monTableauEdit.add(latlng);
						}

						monTableauMarkerEdit.get(p).showInfoWindow();

					} else {
						Toast.makeText(EditActivity.this, R.string.marker,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(EditActivity.this, R.string.preparation,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final Button button = (Button) findViewById(R.id.button2);
		final Button button1 = (Button) findViewById(R.id.button1);
		final Button button2 = (Button) findViewById(R.id.buttonSave);

		// lorsqu'on clique sur le bouton d'id 2, bouton remove
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				if (i == 0) {
					if (!(monTableauMarkerEdit.isEmpty())) {
						monTableauMarkerEdit.get(
								monTableauMarkerEdit.size() - 1).remove();
						monTableauMarkerEdit.remove(monTableauMarkerEdit.size() - 1);
						monTableauEdit.remove(monTableauEdit.size() - 1);
					} else {
						Toast.makeText(EditActivity.this,
								R.string.deletemarker, Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					if (!(monTableauMarkerEdit.isEmpty())) {

						for (int j = 0; j < monTableauMarkerEdit.size(); j++) {
							monTableauMarkerEdit.get(j).remove();
						}
						monTableauMarkerEdit.clear();
						monTableauEdit.clear();
						polyline.remove();
						i = 0;
						button1.setText("EDIT");

					} else {
						Toast.makeText(EditActivity.this,
								R.string.deletemarker, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
		        if( monTableauEdit.size() > 2 )
		        {
		        
		        	Intent intent = new Intent(EditActivity.this, Enregistrer_Parcours.class);

					intent.putParcelableArrayListExtra(keyGuide, monTableauEdit ) ;

					startActivity(intent);

					finish();
		        }
		        else
		        {
		    
		        
		        	Toast.makeText(EditActivity.this, "Veuillez selectionner votre parcours" ,
							Toast.LENGTH_SHORT).show();
		        	
		        }
		        
			}

		});


		// lorsqu'on clique sur le bouton d'id 1, bouton edit
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				if ((i == 0) && (monTableauMarkerEdit.size() > 2)) {

					String url = ConnectionPath
							.getURLConnection(monTableauEdit);
					ReadTask downloadTask = new ReadTask();
					downloadTask.execute(url);
					i++;
					button1.setText("CHOISIR CE PARCOURS");

				} else if ((i != 0) && ((monTableauMarkerEdit.size() > 2))) {
					
					Intent intent = new Intent( EditActivity.this, Guide.class);
					
				
					intent.putParcelableArrayListExtra(keyGuide, monTableauEdit); 
					
					startActivity(intent);
					
					finish();
					
				
				

				

				} else {
					Toast.makeText(EditActivity.this, R.string.addmarker,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}



	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play
	 * services APK is correctly installed) and the map has not already been
	 * instantiated.. This will ensure that we only ever call
	 * {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p/>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt
	 * for the user to install/update the Google Play services APK on their
	 * device.
	 * <p/>
	 * A user can return to this FragmentActivity after following the prompt and
	 * correctly installing/updating/enabling the Google Play services. Since
	 * the FragmentActivity may not have been completely destroyed during this
	 * process (it is likely that it would only be stopped or paused),
	 * {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
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

	/**
	 * Returns the LatLng resulting from moving a distance from an origin in the
	 * specified heading (expressed in degrees clockwise from north).
	 * 
	 * @param from
	 *            The LatLng from which to start.
	 * @param distance
	 *            The distance to travel.
	 * @param heading
	 *            The heading in degrees clockwise from north.
	 */
	public static LatLng computeOffset(LatLng from, double distance,
			double heading) {
		distance /= EARTH_RADIUS;
		heading = toRadians(heading);
		// http://williams.best.vwh.net/avform.htm#LL
		double fromLat = toRadians(from.latitude);
		double fromLng = toRadians(from.longitude);
		double cosDistance = cos(distance);
		double sinDistance = sin(distance);
		double sinFromLat = sin(fromLat);
		double cosFromLat = cos(fromLat);
		double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat
				* cos(heading);
		double dLng = atan2(sinDistance * cosFromLat * sin(heading),
				cosDistance - sinFromLat * sinLat);
		return new LatLng(toDegrees(asin(sinLat)), toDegrees(fromLng + dLng));
	}

	static double havDistance(double lat1, double lat2, double dLng) {
		return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2);
	}

	static double hav(double x) {
		double sinHalf = sin(x * 0.5);
		return sinHalf * sinHalf;
	}

	/**
	 * Returns the length of the given path, in meters, on Earth.
	 */
	public static double computeLength(List<LatLng> path) {
		if (path.size() < 2) {
			return 0;
		}
		double length = 0;
		LatLng prev = path.get(0);
		double prevLat = toRadians(prev.latitude);
		double prevLng = toRadians(prev.longitude);
		for (LatLng point : path) {
			double lat = toRadians(point.latitude);
			double lng = toRadians(point.longitude);
			length += distanceRadians(prevLat, prevLng, lat, lng);
			prevLat = lat;
			prevLng = lng;
		}
		return length * EARTH_RADIUS;
	}

	private static double distanceRadians(double lat1, double lng1,
			double lat2, double lng2) {
		return arcHav(havDistance(lat1, lat2, lng1 - lng2));
	}

	static double arcHav(double x) {
		return 2 * asin(sqrt(x));
	}

	private class ReadTask extends AsyncTask<String, Integer, String> {

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
	
	polyline  = mMap.addPolyline(polyLineOptions);
	
	System.out.println("votre parcours fait " + " " + getLength(data3));
	
	Toast toast = Toast.makeText(EditActivity.this, "votre parcours fait" + " " + getLength(data3) + "km environ",
			Toast.LENGTH_SHORT); 
	toast.show();

	
  }

public double getLength( ArrayList<LatLng> retour)
{
	for( int n = 0 ; n < data3.size() ; n++)
	{
		if( n < (data3.size() - 1) )
		length = length + SphericalUtil.computeDistanceBetween( data3.get(n) , data3.get(n + 1));
	}
	
     length = length * (0.01);
	
	length = Math.round(length);
	
	length = length * (0.1);
	
	return length;
}

     
 }
	
	
}
	