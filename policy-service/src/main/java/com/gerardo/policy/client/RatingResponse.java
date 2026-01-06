package com.gerardo.policy.client;

import java.math.BigDecimal;

public record RatingResponse(
        BigDecimal premium,
        String tariffVersion
) {}
