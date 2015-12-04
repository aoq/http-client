/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.sample.http;

import com.aokyu.dev.http.HttpClient;
import com.aokyu.dev.http.HttpHeaders;
import com.aokyu.dev.http.HttpMethod;
import com.aokyu.dev.http.HttpRequest;
import com.aokyu.dev.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        retrieveResponse();
    }

    private void retrieveResponse() {
        mProgress.setVisibility(View.VISIBLE);
        mResultText.setVisibility(View.GONE);
        new Retriever().execute();
    }

    private final class Retriever extends AsyncTask<Void, Void, String> {

        private static final String TEST_ENDPOINT = "https://httpbin.org/get";

        @Override
        protected String doInBackground(Void... params) {
            HttpResponse response = null;
            try {
                HttpClient client = new HttpClient();
                URL url = new URL(TEST_ENDPOINT);
                HttpHeaders headers = new HttpHeaders();
                HttpRequest request = new HttpRequest(HttpMethod.GET, url, headers);
                response = client.execute(request);
                int statusCode = response.getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    return response.getResponseAsString();
                } else {
                    return response.getErrorAsString();
                }
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } finally {
                if (response != null) {
                    response.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                return;
            }

            try {
                JSONObject jsonObj = new JSONObject(result);
                mResultText.setText(jsonObj.toString(4));
            } catch (JSONException e) {
            }

            mProgress.setVisibility(View.GONE);
            mResultText.setVisibility(View.VISIBLE);
        }
    }
}
