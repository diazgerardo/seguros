package com.gerardo.policy.service;

import com.gerardo.policy.client.RatingClient;
import com.gerardo.policy.client.RatingResponse;
import com.gerardo.policy.domain.Policy;
import com.gerardo.policy.repo.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    @Autowired
    RatingClient ratingClient;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Transactional
    public Policy createDraft(String policyNumber) {

        policyRepository.findByPolicyNumber(policyNumber)
                .ifPresent(p -> {
                    throw new IllegalArgumentException(
                            "policy with number " + policyNumber + " already exists");
                });

        Policy policy = new Policy(policyNumber);
        return policyRepository.save(policy);
    }

    @Transactional
    public Policy issuePolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("policy not found: " + policyId));
        RatingResponse rating = ratingClient.rate(policy.getPolicyNumber());
        policy.issue(rating.premium(), rating.tariffVersion());
        return policyRepository.save(policy);
    }
}
