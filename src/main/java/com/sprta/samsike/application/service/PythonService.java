package com.sprta.samsike.application.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PythonService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String getPythonExecutionResult() {
        String pythonApiUrl = "http://ec2-3-34-94-55.ap-northeast-2.compute.amazonaws.com/run-python";
        ResponseEntity<String> response = restTemplate.getForEntity(pythonApiUrl, String.class);
        return response.getBody();
    }
}
