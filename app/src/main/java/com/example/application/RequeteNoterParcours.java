package com.example.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class RequeteNoterParcours extends AsyncTask<String, String, String> {
    private String email;
    private int idParcours;
    private String note;
    private boolean favori;
    private double vitesse;
    private String date;

    public RequeteNoterParcours(int idParcours, String email, String note, boolean favori, double vitesse, String date) {
        this.email = email;
        this.note = note;
        this.favori = favori;
        this.vitesse = vitesse;
        this.date = date;
        this.idParcours = idParcours;
    }

    @Override
    protected String doInBackground(String... uri) {
        StringBuffer sb = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(uri[0]));

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("idParcours", Integer.toString(idParcours)));
            pairs.add(new BasicNameValuePair("note", note));
            pairs.add(new BasicNameValuePair("email", email));
            pairs.add(new BasicNameValuePair("date", date));
            if(favori)
                pairs.add(new BasicNameValuePair("favori", "true"));
            pairs.add(new BasicNameValuePair("vitesse", Double.toString(vitesse)));

            request.setEntity(new UrlEncodedFormEntity(pairs));

            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            sb = new StringBuffer("");
            String line="";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}