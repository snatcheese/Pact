package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Connexion extends Activity {
	
    private CheckBox checkBox;
    private Button button;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        // On récupère les identifiants sauvegardés si ils existent
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String emailSauvegarde = preferences.getString("EMAIL", "");
        String passwordSauvegarde = preferences.getString("PASSWORD", "");

        if(!emailSauvegarde.isEmpty() && !passwordSauvegarde.isEmpty()) { // Si déjà des identifiants
            // Changer d'activité
            /************** INTEGRATION ICI**************/
            Intent activiteAccueil = new Intent(this, SecondScreen.class);
            /********************************************/
            startActivity(activiteAccueil);
        }

        // Changer le nom du bouton si l'utilisateur coche la case d'inscription
        button = (Button) findViewById(R.id.boutonConnexion);
        checkBox = (CheckBox) findViewById(R.id.creation);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    button.setText(R.string.inscription);
                }
                else
                    button.setText(R.string.connexion);
            }
        });

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailChaine = email.getText().toString();
                String passwordChaine = password.getText().toString();

                // Contraintes email et password
                if(emailChaine.isEmpty() || passwordChaine.isEmpty() || !emailChaine.contains("@")) {
                    Toast.makeText(Connexion.this, R.string.invalides, Toast.LENGTH_SHORT).show();
                }
                else {
                    String nomServeur = getResources().getString(R.string.nom_serveur);
                    if (checkBox.isChecked()) {
                        // Cas de l'inscription

                        String lien = nomServeur + "/creation.php?email=" + emailChaine + "&password=" + passwordChaine;
                        new RequestStask(1, Connexion.this, emailChaine, passwordChaine).execute(lien);
                    } else {
                        String lien = nomServeur + "/connexion.php?email=" + emailChaine + "&password=" + passwordChaine;
                        new RequestStask(2, Connexion.this, emailChaine, passwordChaine).execute(lien);
                    }
                }

            }
        });
    }
}