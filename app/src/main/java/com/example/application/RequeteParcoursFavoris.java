package com.example.application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

class RequeteParcoursFavoris extends AsyncTask<String, String, String> {
    private Activity activity;
    private List<Parcours> liste;
    private int alentours;

    public RequeteParcoursFavoris(Activity activity, int alentours) {
        this.activity = activity;
        this.alentours = alentours;
    }

    public List<Parcours> getListeParcours() {
        return liste;
    }

    @Override
    protected String doInBackground(String... uri) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri[0]));

            HttpResponse response = client.execute(request);

            HttpEntity messageEntity = response.getEntity();
            InputStream is = messageEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);

        try {
            JSONArray parcours = new JSONArray(jsonResult);

            liste = new ArrayList<Parcours>();

            for(int i = 0; i < parcours.length(); i++) {
                // Pour chaque parcours favoris
                String nom = parcours.getJSONObject(i).getString("nom");
                int id = parcours.getJSONObject(i).getInt("id");
                int note = parcours.getJSONObject(i).getInt("note");
                String parcoursChaine = parcours.getJSONObject(i).getString("parcours");
                boolean favori = parcours.getJSONObject(i).getBoolean("favori");
				String date = parcours.getJSONObject(i).getString("date");
				
				System.out.println("DBG : "+nom);

                Parcours item = new Parcours(id, nom, note, parcoursChaine, favori, date);
                liste.add(item);
            }

            ListView vue = (ListView) activity.findViewById(R.id.listeFavoris);

            ParcoursAdapter customAdapter = new ParcoursAdapter(activity, liste, alentours);
            vue.setAdapter(customAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}