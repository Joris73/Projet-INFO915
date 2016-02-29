package com.joris.projetinfo915;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

/**
 * Created by jobos on 01/02/2016.
 */
public class PhilipsFragment extends Fragment {

    private String API_PHILIPS_TRUE = "http://192.168.1.133:8080/lampe/True";
    private String API_PHILIPS_FALSE = "http://192.168.1.133:8080/lampe/False";

    public PhilipsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_philips, container, false);

        LobsterPicker lobsterPicker = (LobsterPicker) view.findViewById(R.id.lobsterpicker);
        final LobsterShadeSlider shadeSlider = (LobsterShadeSlider) view.findViewById(R.id.shadeslider);

        lobsterPicker.addDecorator(shadeSlider);

        Button onButton = (Button) view.findViewById(R.id.btn_on_philips);
        Button offButton = (Button) view.findViewById(R.id.btn_off_philips);

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float[] hsv = new float[3];
                Color.colorToHSV(shadeSlider.getColor(), hsv);
                int couleurPourTim = (int) ((hsv[0] / 360) * 65025);
                Log.wtf("qdg", API_PHILIPS_TRUE + "/color/" + couleurPourTim);
                new DownloadMachin().execute(API_PHILIPS_TRUE + "/color/" + couleurPourTim);
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadMachin().execute(API_PHILIPS_FALSE + "/color/66666");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


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