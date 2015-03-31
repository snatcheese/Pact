package com.example.application;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BlindActivity extends ActionBarActivity {

    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
    public final static String keyGuide = "key_to_Guide";
    private LatLng latlng = null;
    private LatLng Paris = new LatLng(48.8534100, 2.3488000);
    private ArrayList<LatLng> tableau = new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);


        final EditText text = (EditText) findViewById(R.id.editText);


        final View buttonText2 = findViewById(R.id.button8);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(
                BlindActivity.this, 2);
        if (typeFaceButton2 != null)
            ((TextView) buttonText2).setTypeface(typeFaceButton2);

        final Button button = (Button) findViewById(R.id.button8);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (text.getText() == null) {

                }
                else if( (button.getText()).toString().compareTo("GENERATE!") == 0 )
                {

                    tableau.add(Paris);
                    tableau.add(SphericalUtil.computeOffset(Paris, 200 , 10));
                    String adresse = text.getText().toString();
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(makeUrl(adresse));
                    button.setText("GO!");

                }
                else if( (button.getText()).toString().compareTo("GO!") == 0 )
                {
                    Intent intent = new Intent( BlindActivity.this, Guide.class);

                    intent.putParcelableArrayListExtra(keyGuide, tableau);

                    startActivity(intent);

                    finish();
                }
            }



        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blind, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String makeUrl( String text)
    {

            String regexp = "[\\s,;\\n\\t]+"; // these are my delimiters
            String [] tokens = null;
            tokens = text.split(regexp);
            for(int i=0;i< tokens.length ;i++){
                System.out.println(tokens[i]);
            }
            String bis = null;
            for( int i = 0 ; i < tokens.length ; i++)
            {
                if( i == 0)
                {
                    bis = tokens[0] + "+";
                }
                else if( i != (tokens.length - 1))
                {
                    bis = bis + tokens[i] + "+";
                    System.out.println(bis);
                }
                else
                {
                    bis = bis + tokens[i];
                }
            }

        return ("http://maps.googleapis.com/maps/api/geocode/json?address=$" + bis + "&sensor=false");

    }

    private class ReadTask extends AsyncTask<String, Integer, String> {

		/*
		 * @Override protected void onPreExecute() { super.onPreExecute();
		 * Toast.makeText(getApplicationContext(),
		 * "D�but du traitement asynchrone", Toast.LENGTH_LONG).show(); }
		 *
		 * @Override protected void onProgressUpdate(Integer... values){
		 * super.onProgressUpdate(values); bar.setVisibility(View.VISIBLE); //
		 * Mise � jour de la ProgressBar bar.setProgress(values[0]); }
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

            try {
                JSONObject jObject = null ;
                try {
                    jObject = new JSONObject(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                };


                JSONArray results = jObject.getJSONArray("results");
                JSONObject resultsBis = results.getJSONObject(0);
                JSONObject geometry = resultsBis.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double latitude =  location.getDouble("lat");
                double longitude =  location.getDouble("lng");

                latlng =  new LatLng( latitude , longitude) ;
                tableau.add(latlng);








            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }


}

