package com.example.application;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Parcours_Favoris extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours__favoris);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("EMAIL", "");

        String nomServeur = getResources().getString(R.string.nom_serveur);
        RequeteParcoursFavoris requete = new RequeteParcoursFavoris(this, 0);
        requete.execute(nomServeur+"/parcours_favoris.php?email="+email);
    }
}
