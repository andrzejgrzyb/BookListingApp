package pl.com.andrzejgrzyb.booklistingapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andrzej on 19.07.2017.
 */

public class BookSearchAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {

    private static final String ITEMS = "items";
    private static final String VOLUME_INFO = "volumeInfo";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";

    public AsyncResponse delegate = null;

    public BookSearchAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> bookArrayList) {
        delegate.processFinish(bookArrayList);
    }

    @Override
    protected ArrayList<Book> doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String bookJsonStr = null;

        try {
            // Construct the URL for the Google Books API query
            URL url = new URL(strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                bookJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                bookJsonStr = null;
            }
            bookJsonStr = buffer.toString();
        } catch (IOException e) {
            bookJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonConverter(bookJsonStr);
    }

    private ArrayList<Book> jsonConverter(String bookJsonStr) {

        ArrayList<Book> bookArrayList = new ArrayList<>();

        try {
            JSONObject bookJsonObject = new JSONObject(bookJsonStr);
            if (!bookJsonObject.isNull(ITEMS)) {

                JSONArray bookJsonArray = bookJsonObject.getJSONArray(ITEMS);

                for (int i = 0; i < bookJsonArray.length(); i++) {

                    JSONObject itemJsonObject = bookJsonArray.getJSONObject(i);
                    JSONObject volumeInfoObject = itemJsonObject.getJSONObject(VOLUME_INFO);

                    String title = volumeInfoObject.getString(TITLE);

                    String[] authors = new String[]{"No Authors"};

                    if (!volumeInfoObject.isNull(AUTHORS)) {
                        JSONArray authorsArray = volumeInfoObject.getJSONArray(AUTHORS);

                        authors = new String[authorsArray.length()];
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authors[j] = authorsArray.getString(j);
                        }
                    }

                    JSONObject imageLinks = volumeInfoObject.getJSONObject("imageLinks");
                    String thumbnail = imageLinks.getString("thumbnail");

                    bookArrayList.add(new Book(title, authors));
                }
            } else {
                bookArrayList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookArrayList;
    }
}