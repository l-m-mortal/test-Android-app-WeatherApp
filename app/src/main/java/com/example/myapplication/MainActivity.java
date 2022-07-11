package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private Button mainBtn;
    private TextView resultInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userField = findViewById(R.id.userField);
        mainBtn = findViewById(R.id.mainBtn);
        resultInfo = findViewById(R.id.resultInfo);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userField.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.noUserInput, Toast.LENGTH_LONG).show();
                    else{
                        String city = userField.getText().toString();
                        String key = "dbcabfd626b6cbb6e6e594cb74653095";
                        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid="+ key +"&units=metric";

                        new getUrlData().execute(url);
                }
            }
        });

    }
    private class getUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            resultInfo.setText("wait...");

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) !=null);
                    buffer.append(line).append("\n");

                    return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection !=null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                    }catch (IOException e){
                    e.printStackTrace();
                }
            } return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                resultInfo.setText("Temperature:" + jsonObject.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}