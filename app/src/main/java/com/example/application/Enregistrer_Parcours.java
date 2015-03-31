package com.example.application;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;





import com.google.android.gms.maps.model.LatLng;

public class Enregistrer_Parcours extends Activity {

	private RadioGroup reponse = null;
	private Spinner notes = null;
	private Button bouton = null;
	private EditText nomParcours = null;
	private CheckBox favori = null;
	private ArrayList<LatLng> parcours;
	public final static String key = "key_to_SavePath";
	public final static String keyGuide = "key_to_Guide";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enregistrer__parcours);

		
		
		reponse = (RadioGroup) findViewById(R.id.reponse);
		notes = (Spinner) findViewById(R.id.notes);
		bouton = (Button) findViewById(R.id.sauvegarder);
		nomParcours = (EditText) findViewById(R.id.nomParcours);
		favori = (CheckBox) findViewById(R.id.favori);

		// On ajoute les notes possibles au bouton de vote: de 1 à 5
		ArrayList<String> listeNotes = new ArrayList<String>();
		for (int i = 1; i <= 5; i++)
			listeNotes.add(Integer.toString(i));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listeNotes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		notes.setAdapter(adapter);

		reponse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.non:
					// Si l'utilisateur ne sauvegarde pas le parcours, retour
					// accueil
					 
					Intent activiteAccueil = new Intent(
							Enregistrer_Parcours.this, SecondScreen.class);
					
					startActivity(activiteAccueil);
					finish();
					break;
				}
			}
		});

		bouton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String nom = nomParcours.getText().toString();
				if (nom.isEmpty()) {
					Toast.makeText(Enregistrer_Parcours.this, R.string.nomVide,
							Toast.LENGTH_SHORT).show();
				} else {

					Intent retrieveIntent = getIntent();

				    parcours = retrieveIntent.getParcelableArrayListExtra(keyGuide);
				    

					
					double vitesse = 12.1;
					// vérifier parcours non vide
					

					SharedPreferences preferences = PreferenceManager
							.getDefaultSharedPreferences(Enregistrer_Parcours.this);
					String email = preferences.getString("EMAIL", "");
					String note = notes.getSelectedItem().toString();
					String date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
							.format(new Date());

					String nomServeur = getResources().getString(
							R.string.nom_serveur);

					new RequeteEnregistrerParcours(email, nom, parcours, note,
							favori.isChecked(), vitesse, date)
							.execute(nomServeur + "/enregistrer_parcours.php");

					// Retour accueil
					
					Intent activiteAccueil = new Intent(
							Enregistrer_Parcours.this, SecondScreen.class);
					
					startActivity(activiteAccueil);
					finish();
				}
			}
		});

	}
	
	
}