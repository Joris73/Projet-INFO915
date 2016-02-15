package com.joris.projetinfo915;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by jobos on 05/02/2016.
 */
public class PriseFragment extends Fragment {

    private boolean priseEtat = false;
    private ImageView powerButton;

    public PriseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prise, container, false);
        powerButton = (ImageView) view.findViewById(R.id.power_button);
        powerButton.setOnClickListener(changeEtat);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private View.OnClickListener changeEtat = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Si la prise est allumée
            if (priseEtat) {
                priseEtat = false;
                powerButton.setBackgroundResource(R.drawable.power_red);
            } else {
                priseEtat = true;
                powerButton.setBackgroundResource(R.drawable.power_green);
            }
        }
    };

}