package com.example.weathertoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView result1;

    public void getWeather(View view){
        try {
            Weather weather1 = new Weather();
            String encoded = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            weather1.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encoded + "&appid=1bdc2da8d0178247dcf2196d5bf76ae9");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find",Toast.LENGTH_SHORT).show();
        }
    }

    public class Weather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
           String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data = reader.read();

                while (data!=-1){
                    char current = (char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                result1.setText("Could not find");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                String weatherinfo = object.getString("weather");
                Log.i("weather content",weatherinfo);
                JSONArray arr = new JSONArray(weatherinfo);

                String messege="";

                for (int i =0;i<arr.length();i++){
                    JSONObject part = arr.getJSONObject(i);
                    String main=part.getString("main");
                    String description=part.getString("description");

                    if (!main.equals("")&&!description.equals("")){
                        messege+=main+"\n"+description;
                    }
                }

                if (!messege.equals("")){
                    result1.setText(messege);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Could not find",Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Could not find",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        editText = findViewById(R.id.plaintext);
        result1=findViewById(R.id.textView);
    }
}