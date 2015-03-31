package com.example.application;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ParcoursAdapter extends BaseAdapter {
    private final Context context;
    private final List<Parcours> values;
    private int position;
    private int alentours;
    public final static String keyGuide = "key_to_Guide";

    public ParcoursAdapter(Context context, List<Parcours> values, int alentours) {
        this.context = context;
        this.values = values;
        this.alentours = alentours;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favoris, parent,false);
        }

        TextView nom = (TextView) convertView.findViewById(R.id.nomFavoris);
        TextView note = (TextView) convertView.findViewById(R.id.noteFavoris);
        Button bouton = (Button) convertView.findViewById(R.id.buttonFavoris);
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkFavoris);
		TextView date = (TextView) convertView.findViewById(R.id.dateFavoris);
        
        if(alentours == 1) { // si activité parcours alentours, on désactive les checkboxs et la date
        	checkbox.setVisibility(View.GONE);
			date.setVisibility(View.GONE);
		}
        else if(alentours == 2) // si activité historique, on désactive les checkboxs
        	checkbox.setVisibility(View.GONE);

        this.position = position;
        final int position2 = position;
        

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String email = preferences.getString("EMAIL", "");

                String nomServeur = context.getResources().getString(R.string.nom_serveur);
                int idParcours = values.get(position2).getId();

                String variables = "/favoris.php?email="+email+"&idParcours="+idParcours+"&favori=";
                if(isChecked) // on ajoute le parcours aux favoris
                    new RequeteFavoris().execute(nomServeur+variables+"true");
                else // On supprime le parcours des favoris
                    new RequeteFavoris().execute(nomServeur+variables+"false");
            }
        });

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aller à l'activité de guidage
                // Passer en argument les points GPS
            	ArrayList<LatLng> points = values.get(position2).getPoints();
                Intent activiteAccueil = new Intent(context, Guide.class);
                
                activiteAccueil.putParcelableArrayListExtra(keyGuide, points);
                
                context.startActivity(activiteAccueil);
              
                ( (Activity) context).finish();
            }
        });

        nom.setText(values.get(position).getNom());
        note.setText(Integer.toString(values.get(position).getNote()));
		date.setText(values.get(position).getDate());

        return convertView;
    }
}