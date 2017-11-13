package com.example.vinittiwari.androidapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ListActivity {

        // 1. define list of item
        String[] col2Value = null;

        //String listItemArray[] = {"A","B","C"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            String listItemArray[] = requestSheet("1ZUPow_rFLCEifZh2oN3WW_oG0B4TWawh9xL7X50ulyQ");
            // 2. create array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1, // standard row layout provided by android
                    listItemArray){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.e("--->","--->"+position);
                            changeActivity(Option1.class,col2Value[position]);
                        }
                    });
                    return view;
                }

            };

            // 3. Call setListAdapter
            setListAdapter(adapter);



            System.out.print("----->");

        }

        void changeActivity(Class ChangeToActivityContext,String position){
            Intent intent = new Intent(this, ChangeToActivityContext);
            intent.putExtra("clickedItem", String.valueOf(position));
            startActivity(intent);
        }


    public String[] requestSheet(String excelId){
        String[] array = null;

        try {
            ArrayList<String> ar = new ArrayList<String>();
            ArrayList<String> col2 = new ArrayList<String>();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://docs.google.com/spreadsheets/d/"+excelId+"/gviz/tq?tq="+"select%20*");
            //String postBody = "";
            //StringEntity se = new StringEntity( postBody);
            //httppost.setEntity(se);
            //getRequest.setHeader("Content-type", "text/xml");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(getRequest);
            String responseString = new BasicResponseHandler().handleResponse(response);
            responseString = responseString.replace("/*O_o*/"," ");
            responseString = responseString.replace("google.visualization.Query.setResponse("," ");
            responseString = responseString.substring(0, responseString.length() - 2);
            Log.i("Response------>",responseString);
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(responseString);
            JsonObject obj = element.getAsJsonObject();
            String rows = obj.get("table").getAsJsonObject().get("rows").toString();
            Log.i("rows------>",rows);

            for(int i=0;i<=obj.get("table").getAsJsonObject().get("rows").getAsJsonArray().size();i++) {
                try{
                    String value = obj.get("table").getAsJsonObject().get("rows").getAsJsonArray().get(i).getAsJsonObject().get("c").getAsJsonArray().get(0).getAsJsonObject().get("v").toString();
                    String value2 = obj.get("table").getAsJsonObject().get("rows").getAsJsonArray().get(i).getAsJsonObject().get("c").getAsJsonArray().get(1).getAsJsonObject().get("v").toString();
                    Log.i("value------>",value);
                    ar.add(value);
                    col2.add(value2);
                }catch (Exception e){

                }
            }
            array = ar.toArray(new String[ar.size()]);
            col2Value= col2.toArray(new String[col2.size()]);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }
    }

