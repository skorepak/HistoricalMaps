package com.example.mapsv4.app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Skorepa on 18.květen.2014.
 */
public class InsertLocationDialog extends DialogFragment implements View.OnClickListener {

    private int clickCount = 0;
    private LatLng mLatLng;

    private TextView LatLngInfo;
    private EditText czName;
    private EditText deName;
    private EditText enName;
    private Button saveButton;

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.insert_location_dialog, container, false);

        v.setOnClickListener(this);
        LatLngInfo = (TextView) v.findViewById(R.id.lan_lng_view);
        czName =     (EditText) v.findViewById(R.id.czName);
        deName =     (EditText) v.findViewById(R.id.deName);
        enName =     (EditText) v.findViewById(R.id.enName);
        saveButton = (Button)   v.findViewById(R.id.saveButton);

        if(getDialog() != null)
            getDialog().setTitle(getString(R.string.add_position));

        LatLngInfo.setText(String.format("Latitude: %f\nLongitude: %f", mLatLng.latitude, mLatLng.longitude));
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // MapsActivity.mLocations.insertLocation(mLatLng.latitude, mLatLng.longitude, czName.getText(), deName.getText(), enName.getText());
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {

    }
}