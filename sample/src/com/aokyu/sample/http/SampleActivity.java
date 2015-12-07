/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.sample.http;

import com.aokyu.dev.http.HttpClient;
import com.aokyu.dev.http.HttpClient.Configuration;
import com.aokyu.dev.http.HttpClient.ConfigurationBuilder;
import com.aokyu.dev.http.HttpMethod;
import com.aokyu.dev.http.HttpRequest;
import com.aokyu.dev.http.HttpResponse;
import com.aokyu.dev.http.content.ParametersBody;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends AppCompatActivity {

    @Bind(R.id.get_button)
    Button mGetButton;

    @Bind(R.id.progress)
    ProgressBar mProgress;

    @Bind(R.id.result_text)
    TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_sample);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_button)
    void onGetButtonClick() {
        new Retriever().execute();
    }

    @MainThread
    private void onRequestStart() {
        mGetButton.setEnabled(false);
        mProgress.setVisibility(View.VISIBLE);
        mResultText.setVisibility(View.GONE);
    }

    @MainThread
    private void onRequestCompleted() {
        mGetButton.setEnabled(true);
        mProgress.setVisibility(View.GONE);
        mResultText.setVisibility(View.VISIBLE);
    }

    private final class Retriever extends AsyncTask<Void, Void, String> {

        private static final String TEST_ENDPOINT = "https://httpbin.org/get";

        @MainThread
        @Override
        protected void onPreExecute() {
            onRequestStart();
        }

        @WorkerThread
        @Override
        protected String doInBackground(Void... params) {
            HttpResponse response = null;
            try {
                HttpClient client = new HttpClient();
                Configuration config = new ConfigurationBuilder()
                        .setConnectTimeout(60000)
                        .setReadTimeout(60000)
                        .build();
                client.setConfiguration(config);
                // Add request parameters to URL.
                Map<String, String> map = new HashMap<String, String>();
                map.put("show_env", "1");
                ParametersBody body = new ParametersBody(map);
                URL url = new URL(TEST_ENDPOINT + "?" + body.toParameter());
                HttpRequest request = new HttpRequest(HttpMethod.GET, url);
                response = client.execute(request);
                int statusCode = response.getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    return response.getResponseAsString();
                }
            } catch (MalformedURLException e) {
                // Invalid URL.
            } catch (SocketTimeoutException e) {
                // Connection timeout.
            } catch (IOException ioe) {
                // Network I/O error or error response.
                if (response != null) {
                    try {
                        return response.getErrorAsString();
                    } catch (IOException e) {
                        // Network I/O error.
                    }
                }
            } finally {
                if (response != null) {
                    response.disconnect();
                }
            }
            return null;
        }

        @MainThread
        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    mResultText.setText(jsonObj.toString(4));
                } catch (JSONException e) {
                    // Invalid response.
                }
            }
            onRequestCompleted();
        }
    }
}
