package com.zacguo.androidone;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONInputStream;
import org.quickconnectfamily.json.JSONOutputStream;
import org.quickconnectfamily.json.JSONUtilities;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Apparently, Android doesn't allow network access in the MainActivity, so I had to override it.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        try {
            getWebsiteInfo();
        } catch (Exception e){
        }

    }

    public void getWebsiteInfo() throws Exception {

        /*
        Follow codes are pretty much the same as the command line one
        It just the android http request will return mobile version of the websites
        and most of them somehow don't include meta.description
         */
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

        // initialize a json utility, it will parse the json file read from online and turn it into a hashmap
        JSONUtilities jsonUtil = new JSONUtilities();

        final HashMap parsedJSONMap = (HashMap) jsonUtil.parse(jsonText);

        //convert ArrayList to Array
        final String[] bookArray = Arrays.copyOf(parsedJSONMap.keySet().toArray(),parsedJSONMap.keySet().toArray().length,String[].class);

        //setup adapter for the listView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.text_view, bookArray);

        //set adapter to listView
        ListView mainList = (ListView) findViewById(R.id.mainList);
        mainList.setAdapter(adapter);

        //setup a on click handler to handle click events, and create new view intent
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,bookArray[position], Toast.LENGTH_SHORT).show();
                ListActivity aList = new ListActivity();

                Intent intent = new Intent(MainActivity.this, MyListActivity.class);
                //give the bookName that is clicked, so MyListActivity will use it to list the chapter names
                intent.putExtra("bookName", bookArray[position]);
                startActivity(intent);
            }
        });

    }
}
