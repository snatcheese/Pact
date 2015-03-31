package com.example.application;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.location.LocationListener;

public class Parcours_Alentours extends Activity implements
		android.location.LocationListener {
	private double lat;
	private double lng;
	private LocationManager lm;
	private boolean done = false;

	@Override
	protected void onResume() {
		super.onResume();

		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
					this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
				this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates((android.location.LocationListener) this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parcours__alentours);

	}

	@Override
	public void onLocationChanged(Location location) {
		lat = location.getLatitude();
		lng = location.getLongitude();
		if (!done) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			String email = preferences.getString("EMAIL", "");

			String nomServeur = getResources().getString(R.string.nom_serveur);
			RequeteParcoursFavoris requete = new RequeteParcoursFavoris(this, 1);

			requete.execute(nomServeur + "/parcours_alentours.php?lat=" + lat
					+ "&lng=" + lng + "&email=" + email);
		}
		done = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
