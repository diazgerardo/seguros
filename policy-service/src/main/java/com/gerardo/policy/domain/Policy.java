package com.gerardo.policy.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "policy")
public class Policy {


    @Version
    private Long version; // control de concurrencia

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @Column(precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Column(length = 50)
    private String tariffVersion;

    private Instant issuedAt;

    protected Policy() {
        // for JPA
    }

    public Policy(String policyNumber) {
        this.policyNumber = policyNumber;
        this.status = PolicyStatus.DRAFT;
    }

    public void issue(BigDecimal premiumAmount, String tariffVersion) {
        if (this.status != PolicyStatus.DRAFT) {
            throw new IllegalStateException("policy is not in DRAFT state");
        }
        this.premiumAmount = premiumAmount;
        this.tariffVersion = tariffVersion;
        this.status = PolicyStatus.ISSUED;
        this.issuedAt = Instant.now();
    }

    // getters only (immutability outside the aggregate)

    public Long getId() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public String getTariffVersion() {
        return tariffVersion;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }
}
