package com.zacguo.androidone;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.quickconnectfamily.json.JSONUtilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Z on 6/10/2017.
 */

public class MyListActivity extends ListActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        String bookName = getIntent().getExtras().getString("bookName");

        try
        {
            URL webpagename = new URL("http://zacguo.com/mormon.json");

            // creates the connection to said web page
            HttpURLConnection testconnection = (HttpURLConnection) webpagename.openConnection();
            // establish connection buffer and prep it to display
            BufferedReader displaydata = new BufferedReader(new InputStreamReader(testconnection.getInputStream()));
            // here is the string object that the data will display in
            String hyperText;
            String jsonText = "";
            // loop while there is data left and display in the string
            while ((hyperText = displaydata.readLine()) != null) {
                jsonText += hyperText;
            }

            //similar to MainActivity, json will be convert to HashMap then get the array by bookName
            //Then, setup the view adapter to display the array of chapter names.
            JSONUtilities jsonUtil = new JSONUtilities();

            final HashMap parsedJSONMap = (HashMap) jsonUtil.parse(jsonText);

            System.out.println(parsedJSONMap);

            ArrayList<String> chapterList = (ArrayList<String>) parsedJSONMap.get(bookName);

            final String[] chapterArray = Arrays.copyOf(chapterList.toArray(),chapterList.toArray().length,String[].class);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.text_view, chapterArray);

            setListAdapter(adapter);

        } catch (Exception e){

        }
    }
}
