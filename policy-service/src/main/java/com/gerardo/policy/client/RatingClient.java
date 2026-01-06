package com.gerardo.policy.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RatingClient {

    final RestTemplate restTemplate;

    public RatingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RatingResponse rate(String policyNumber) {
        return restTemplate.getForObject(
            "http://localhost:8082/rating?policyNumber={policyNumber}",
            RatingResponse.class,
            policyNumber
        );
    }
}
