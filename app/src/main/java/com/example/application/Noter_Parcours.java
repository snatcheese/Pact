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
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Noter_Parcours extends Activity {

    private RadioGroup reponse = null;
    private Spinner notes = null;
    private Button bouton = null;
    private CheckBox favori = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noter__parcours);

        reponse = (RadioGroup) findViewById(R.id.reponse2);
        notes = (Spinner) findViewById(R.id.notes2);
        bouton = (Button) findViewById(R.id.noter);
        favori = (CheckBox) findViewById(R.id.favori2);

        // On ajoute les notes possibles au bouton de vote: de 1 à 5
        ArrayList<String> listeNotes = new ArrayList<String>();
        for(int i = 1; i <= 5; i++)
            listeNotes.add(Integer.toString(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeNotes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notes.setAdapter(adapter);

        reponse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.non:
                        // Si l'utilisateur ne sauvegarde pas le parcours, retour accueil
                        /****** INTEGRATION ICI *******/
                        Intent activiteAccueil = new Intent(Noter_Parcours.this, Enregistrer_Parcours.class);
                        /*******************************/
                        startActivity(activiteAccueil);
                        break;
                }
            }
        });

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /************ INTEGRATION ICI **************/
                    double vitesse = 12.1;
                    int idParcours = 40;
                    // vérifier parcours non vide
                    /*******************************************/

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Noter_Parcours.this);
                    String email = preferences.getString("EMAIL", "");
                    String note = notes.getSelectedItem().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

                    String nomServeur = getResources().getString(R.string.nom_serveur);

                    new RequeteNoterParcours(idParcours, email, note, favori.isChecked(), vitesse, date).execute(nomServeur+"/noter_parcours.php");

                    // Retour accueil
                    /****** INTEGRATION ICI *******/
                    Intent activiteAccueil = new Intent(Noter_Parcours.this, Enregistrer_Parcours.class);
                    /*******************************/
                    startActivity(activiteAccueil);
            }
        });

    }
}