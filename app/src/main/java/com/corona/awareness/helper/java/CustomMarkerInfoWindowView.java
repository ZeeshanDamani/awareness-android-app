package com.corona.awareness.helper.java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.corona.awareness.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {
    private final View markerItemView;



    public CustomMarkerInfoWindowView(Context context) {
        markerItemView = LayoutInflater.from(context).inflate(R.layout.map_custom_info_window, null);  // 1
    }

    @Override
    public View getInfoWindow(Marker marker) { // 2
        TextView itemNameTextView = (TextView)  markerItemView.findViewById(R.id.nameTxt);
        TextView itemAddressTextView = (TextView)  markerItemView.findViewById(R.id.addressTxt);

        return markerItemView;  // 4
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}