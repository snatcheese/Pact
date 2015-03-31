package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MesParcoursActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_parcours);
		
		View button1 = findViewById(R.id.button1);
		Typeface typeFaceButton = CustomFontsLoader.getTypeface( MesParcoursActivity.this , 1); 
		if(typeFaceButton!=null) ((TextView) button1).setTypeface(typeFaceButton);
		
		View button2 = findViewById(R.id.button2);
		Typeface typeFaceButton2 = CustomFontsLoader.getTypeface( MesParcoursActivity.this , 1);  
		if(typeFaceButton2!=null) ((TextView) button2).setTypeface(typeFaceButton2);
		
       final Button buttonGot2 = (Button) findViewById(R.id.button2);
       final Button buttonGot3 = (Button) findViewById(R.id.button1);
		

		
		buttonGot2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent( MesParcoursActivity.this , Parcours_Favoris.class);
				startActivity(intent);
			}

		});
		
		buttonGot3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent( MesParcoursActivity.this , Parcours_Historique.class);
				startActivity(intent);
			}

		});

	}
	
}
