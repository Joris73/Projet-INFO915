package com.joris.projetinfo915;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.felipecsl.gifimageview.library.GifImageView;

/**
 * Created by jobos on 05/02/2016.
 */
public class VoletFragment extends Fragment {

    private ImageView voletGif;
    private ImageView upButton;
    private ImageView downButton;

    public VoletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volet, container, false);
        voletGif = (ImageView) view.findViewById(R.id.volet_gif);
        upButton = (ImageView) view.findViewById(R.id.up_button);
        downButton = (ImageView) view.findViewById(R.id.down_button);
        upButton.setOnClickListener(openVolet);
        downButton.setOnClickListener(closeVolet);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private View.OnClickListener openVolet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            voletGif.setBackgroundResource(R.drawable.volet_open);
        }
    };

    private View.OnClickListener closeVolet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            voletGif.setBackgroundResource(R.drawable.volet_close);
        }
    };
}