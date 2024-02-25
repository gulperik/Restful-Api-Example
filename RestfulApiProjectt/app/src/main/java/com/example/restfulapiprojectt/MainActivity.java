package com.example.restfulapiprojectt;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText phoneName;
    TextView price;

    Button buton;

    String phoneNamee;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneName = findViewById(R.id.editTextText);
        price = findViewById(R.id.textView);
        buton = findViewById(R.id.button);


        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNamee = phoneName.getText().toString();
                try{
                    getData();
                }
                catch (MalformedURLException e){
                    throw new RuntimeException(e);
                }
            }
        });



    }

    public void getData() throws MalformedURLException{
        Uri uri = Uri.parse("https://api.restful-api.dev/objects")
                .buildUpon().build();

        URL url = new URL(uri.toString());
        new DOTask().execute(url);

    }
    class DOTask extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls){
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return  data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        public void parseJson(String data) throws JSONException {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.getString("name");
                JSONObject itemData = item.getJSONObject("data");

                if (name.equals(phoneNamee)) {
                    double priceValue = itemData.getDouble("price");
                    price.setText(String.valueOf(priceValue));
                    return;
                }
            }
            price.setText("Not found");
        }
    }

}