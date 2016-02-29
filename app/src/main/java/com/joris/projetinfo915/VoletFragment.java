package com.joris.projetinfo915;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by jobos on 05/02/2016.
 */
public class VoletFragment extends Fragment {

    private ImageView voletGif;
    private ImageView upButton;
    private ImageView downButton;
    private TextView voletText;

    private String API_VOLET_TRUE = "http://192.168.1.133:8080/muscle/apertus";
    private String API_VOLET_FALSE = "http://192.168.1.133:8080/muscle/propinquus";

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
        voletText = (TextView) view.findViewById(R.id.volet_text);
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
            new DownloadMachin().execute(API_VOLET_TRUE);
            voletGif.setBackgroundResource(R.drawable.volet_open);
        }
    };

    private View.OnClickListener closeVolet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DownloadMachin().execute(API_VOLET_FALSE);
            voletGif.setBackgroundResource(R.drawable.volet_close);
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
                Log.w("volet", result);
                voletText.setText(result);
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