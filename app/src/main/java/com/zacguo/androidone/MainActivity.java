package com.zacguo.androidone;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    }

    public void getWebsiteInfo(View view) throws Exception {

        /*
        titleText to show the title collected from <title> tag
        descText to show the description from meta.description
        urlField for the user to input a website url
         */
        TextView titleText = (TextView) findViewById(R.id.titleText);

        TextView descText = (TextView) findViewById(R.id.descText);

        EditText urlField = (EditText) findViewById(R.id.urlField);

        /*
        Follow codes are pretty much the same as the command line one
        It just the android http request will return mobile version of the websites
        and most of them somehow don't include meta.description
         */
        URL webpagename = new URL(urlField.getText().toString());

        // creates the connection to said web page
        HttpURLConnection testconnection = (HttpURLConnection) webpagename.openConnection();
        // establish connection buffer and prep it to display
        BufferedReader displaydata = new BufferedReader(new InputStreamReader(testconnection.getInputStream()));
        // here is the string object that the data will display in
        String hyperText;
        // loop while there is data left and display in the string
        while ((hyperText = displaydata.readLine()) != null) {

            //just to log out the whole html returned from the website.
            //That's how I know the website will omit the meta.description on the mobile websites
            Log.d("hyperText",hyperText);

			/*
			 * Three pattern created to read out the information returned from the website.
			 * Pattern 1 matches the title tag to output the website title.
			 * Pattern 2 and 3 matches the description meta tag to output the website description.
			 * It is possible that websites place the name perimeter before or after the content perimeter.
			 */
            Pattern pattern = Pattern.compile("<title>(.*?)</title>");
            Matcher matcher = pattern.matcher(hyperText);
            if (matcher.find()) {
                titleText.setText(matcher.group(1));
            }

            Pattern pattern2 = Pattern.compile("<meta content=\"(.*?)\" name=\"description\">");
            Matcher matcher2 = pattern2.matcher(hyperText);
            if (matcher2.find()) {
                descText.setText(matcher2.group(1));
            }

            Pattern pattern3 = Pattern.compile("<meta name=\"description\" content=\"(.*?)\">");
            Matcher matcher3 = pattern3.matcher(hyperText);
            if (matcher3.find()) {
                descText.setText(matcher3.group(1));
            }
        }

        /*
        I got this from https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        Just to hide the keyboard after the input of the website url.
         */
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
