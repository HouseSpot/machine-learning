package com.example.housespot;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Service
public class MachineLearningService {

    private final String tfServingBaseUrl = "https://machine-learning-tfserving-2na55gy4bq-uc.a.run.app/v1/models/my_model:predict";

    public MachineLearningService() {}

    public String predict(String requestPayload) throws Exception {
        String url = tfServingBaseUrl;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestPayload));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}

