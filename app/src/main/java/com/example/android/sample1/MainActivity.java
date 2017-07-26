package com.example.android.sample1;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String AMAZON_REQUEST_URL = "http://webservices.amazon.in/onca/xml?Service=AWSECommerceService&Operation=ItemSearch&SubscriptionId=AKIAIXO2UWOGD63UM5DA&AssociateTag=444830100395 &SearchIndex=Beauty&ResponseGroup=Images,ItemAttributes,Offers&ItemPage=2&Title=lipstick";

    private WordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new WordAdapter(this, new ArrayList<AmazonXmlParser.Entry>());
        listView.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }
    private void loadPage() {
        new DownloadXmlTask().execute(AMAZON_REQUEST_URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List<AmazonXmlParser.Entry>> {

        @Override
        protected List<AmazonXmlParser.Entry> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<AmazonXmlParser.Entry> result;
            try{
            result = loadXmlFromNetwork(urls[0]);
        }
            catch (IOException | XmlPullParserException e){
                result =null;
            }
            return result;
        }

    @Override
        protected void onPostExecute(List<AmazonXmlParser.Entry> data) {

            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }


    private List<AmazonXmlParser.Entry> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
       AmazonXmlParser feedXmlParser = new AmazonXmlParser();
        List<AmazonXmlParser.Entry> entries = null;
        String url = null;
        StringBuilder htmlString = new StringBuilder();
        try {
            stream = downloadUrl(urlString);
            entries = feedXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }

    private InputStream downloadUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.connect();
        if (conn.getResponseCode() == 200) {
            Log.e(LOG_TAG,"Successfully got connected");
        } else {
            Log.e(LOG_TAG, "Error response code");
        }
        InputStream stream = conn.getInputStream();
        return stream;
    }

}