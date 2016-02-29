package com.joris.projetinfo915;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by jobos on 05/02/2016.
 */
public class PriseFragment extends Fragment {

    private boolean priseEtat = false;
    private ImageView powerButton;

    private String API_PRISE_TRUE = "http://192.168.1.133:8080/prise/bcdbd910189245228ffbc2d99e4a7b1d/incendium";
    private String API_PRISE_FALSE = "http://192.168.1.133:8080/prise/bcdbd910189245228ffbc2d99e4a7b1d/exstinctio";

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
                new DownloadMachin().execute(API_PRISE_FALSE);
                powerButton.setBackgroundResource(R.drawable.power_red);
            } else {
                priseEtat = true;
                new DownloadMachin().execute(API_PRISE_TRUE);
                powerButton.setBackgroundResource(R.drawable.power_green);
            }
        }
    };

    private class DownloadMachin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpRequest request = HttpRequest.get(params[0]);
                String result = null;
                if (request.ok()) {
                    result = request.body();
                }
                return result;
            } catch (HttpRequest.HttpRequestException exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //JsonElement jsonResult = new JsonParser().parse(result);
                Gson gson = new Gson();
            } else {
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}