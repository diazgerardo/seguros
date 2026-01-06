package com.gerardo.rating.api;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @GetMapping
    public Map<String, Object> rate(@RequestParam("policyNumber") String policyNumber) {

        // prima determinística, simple
        BigDecimal premium =
                BigDecimal.valueOf(1000 + policyNumber.length() * 100);

        return Map.of(
            "premium", premium,
            "tariffVersion", "V1"
        );
    }
}
