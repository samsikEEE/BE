package com.sprta.samsike.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/python")
public class PythonController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/view")
    public String showPythonResultPage() {
        return "result"; // result.html을 반환
    }

    @GetMapping("/python-result")
    public String getPythonResult() {
        String pythonApiUrl = "http://localhost:5000/run-python"; // Flask API URL
        return restTemplate.getForObject(pythonApiUrl, String.class);
    }
}
