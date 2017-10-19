package com.example.andrew.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
    private static ArrayList<News> newsArrayList = new ArrayList<>();
    private static final String urlopen = " https://content.guardianapis.com/search?q=android&show-tags=contributor&order-by=relevance&api-key=test";
    private static final int READ_TIMEOUT = 10000;
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_type = "type";
    private static final String web = "webUrl";
    private static final String date = "webPublicationDate";
    private static final String sectionname = "sectionName";
    private static final String articles = "webTitle";
    private ListView listView;
    private NewsAdapter task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listviews);
        if (connection()) {
            task = new NewsAdapter(this, newsArrayList);
            listView.setAdapter(task);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    News news = task.getItem(i);
                    Uri titlenewsUri;
                    Uri section;
                    Uri typeUri;
                    Uri webnewsUri = Uri.parse(news.getWeburl());
                    titlenewsUri = webnewsUri;
                    section = webnewsUri;
                    typeUri = webnewsUri;
                    if (news.getWeburl() == null || TextUtils.isEmpty(news.getWeburl())) {
                        Toast.makeText(MainActivity.this, "No Found Link", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, webnewsUri);
                        startActivity(intent);
                        Intent title = new Intent(Intent.ACTION_VIEW, titlenewsUri);
                        startActivity(title);
                        Intent sections = new Intent(Intent.ACTION_VIEW, section);
                        startActivity(sections);
                        Intent types = new Intent(Intent.ACTION_VIEW, typeUri);
                        startActivity(types);
                    }
                }
            });
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, this).forceLoad();
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean connection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsAsync(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        if (data != null && !data.isEmpty()) {
            task.addAll(data);
        } else {
            Toast.makeText(getApplicationContext(), "No Found Data", Toast.LENGTH_SHORT).show();
            listView.setEmptyView(findViewById(R.id.textView));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        task.clear();
    }

    public static class NewsAsync extends AsyncTaskLoader<ArrayList<News>> {
        private final String LOG_TAG = NewsAsync.class.getName();

        public NewsAsync(Context context) {
            super(context);
        }

        @Override
        public ArrayList<News> loadInBackground() {
            URL url = createUrl(urlopen);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            ArrayList<News> test = Json(jsonResponse);
            return test;
        }

        public ArrayList<News> Json(String jsonResponse) {
            try {
                JSONObject JsonResponseurl = new JSONObject(jsonResponse);
                if (JsonResponseurl.has("response")) {
                    JSONObject jsonobject = JsonResponseurl.getJSONObject("response");
                    JSONArray jsonarray = jsonobject.getJSONArray("results");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject newsJsonObject = jsonarray.getJSONObject(i);
                        String webtitle = "", type = "", weburl = "", sectioname = "", dateformate = "", article = "";
                        if (newsJsonObject.has("webTitle")) {
                            webtitle = newsJsonObject.getString(KEY_TITLE);
                        } else {
                            webtitle = "No found Title";
                        }
                        if (newsJsonObject.has("type")) {
                            type = newsJsonObject.getString(KEY_type);
                        } else {
                            type = "Not found Type";
                        }
                        if (newsJsonObject.has("sectionName")) {
                            sectioname = newsJsonObject.getString(sectionname);
                        } else {
                            sectioname = "No found section name";
                        }
                        if (newsJsonObject.has("webUrl")) {
                            weburl = newsJsonObject.getString(web);
                        } else {
                            weburl = "Not found WebUrl";
                        }
                        if (newsJsonObject.has("webPublicationDate")) {
                            String DATES;
                            DATES = newsJsonObject.getString(date);
                            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dt.parse(DATES);
                            dateformate = dt.format(date);
                        } else {
                            dateformate = "Not found Date";
                        }
                        JSONArray jsonarraytags = newsJsonObject.getJSONArray("tags");
                        if (jsonarraytags.isNull(0)) {
                            article = "Not Found Tags";
                        }
                        for (int j = 0; j < jsonarraytags.length(); j++) {
                            JSONObject currentTag = jsonarraytags.getJSONObject(j);
                            String a = new String("");
                            if (currentTag.has("webTitle")) {
                                article = currentTag.getString(articles);
                            } else {
                                article = "Not found author";
                            }
                        }
                        News news = new News(webtitle, type, sectioname, weburl, dateformate, article);
                        newsArrayList.add(news);
                    }
                } else {
                    return null;
                }
                return newsArrayList;
            } catch (JSONException e) {
                Log.e("Error", "Problem in the book JSON", e);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error in URL ", e);
            }
            return url;
        }

        public String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(READ_TIMEOUT/* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP Request.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        public String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }
}
