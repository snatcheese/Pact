package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondScreen extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_screen);
		
		

		View button1 = findViewById(R.id.button1);
		Typeface typeFaceButton = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton != null)
			((TextView) button1).setTypeface(typeFaceButton);

		View button3 = findViewById(R.id.button3);
		Typeface typeFaceButton3 = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton3 != null)
			((TextView) button3).setTypeface(typeFaceButton3);

		View button4 = findViewById(R.id.button4);
		Typeface typeFaceButton4 = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton4 != null)
			((TextView) button4).setTypeface(typeFaceButton4);

		View button5 = findViewById(R.id.button5);
		Typeface typeFaceButton5 = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton5 != null)
			((TextView) button5).setTypeface(typeFaceButton5);

		View button6 = findViewById(R.id.button6);
		Typeface typeFaceButton6 = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton6 != null)
			((TextView) button6).setTypeface(typeFaceButton6);

		View button7 = findViewById(R.id.button7);
		Typeface typeFaceButton7 = CustomFontsLoader.getTypeface(
				SecondScreen.this, 1);
		if (typeFaceButton7 != null)
			((TextView) button7).setTypeface(typeFaceButton7);

		final Button button = (Button) findViewById(R.id.button6);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				Intent intent = new Intent(SecondScreen.this,
						MainActivity.class);
				startActivity(intent);
				

			}
		});

		final Button buttonGot1 = (Button) findViewById(R.id.button1);
		buttonGot1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				

				Intent intent = new Intent(SecondScreen.this,
						MesParcoursActivity.class);
				startActivity(intent);
				

			}
		});

        final Button buttonGotOption = (Button) findViewById(R.id.button5);
        buttonGotOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                Intent intent = new Intent(SecondScreen.this,
                        BlindActivity.class);
                startActivity(intent);


            }
        });

		final Button buttonGot2 = (Button) findViewById(R.id.button4);
		buttonGot2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				Intent intent = new Intent(SecondScreen.this,
						EditActivity.class);
				startActivity(intent);
				

			}
		});

		final Button buttonGot7 = (Button) findViewById(R.id.button7);
		buttonGot7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				// On supprime les identifiants en local
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(SecondScreen.this);
				SharedPreferences.Editor editor = preferences.edit();
				editor.remove("EMAIL");
				editor.remove("PASSWORD");
				editor.commit();

				// Retour � la page de connexion
				Intent activiteConnexion = new Intent(SecondScreen.this,
						Connexion.class);
				startActivity(activiteConnexion);
				finish(); //on ne peut plus revenir sur secondScreen sans se connecter

			}
		});
		
		final Button buttonGot3 = (Button) findViewById(R.id.button3);
		buttonGot3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				Intent intent = new Intent(SecondScreen.this,
						Parcours_Alentours.class);
				
				startActivity(intent);
			
				
				

			}
		});


	}
}
