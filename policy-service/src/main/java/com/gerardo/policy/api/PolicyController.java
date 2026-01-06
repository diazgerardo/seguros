package com.gerardo.policy.api;

import com.gerardo.policy.domain.Policy;
import com.gerardo.policy.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public Policy createDraft(@RequestParam String policyNumber) {
        return policyService.createDraft(policyNumber);
    }

    @PostMapping("/{id}/issue")
    public Policy issuePolicy(@PathVariable Long id) {
        return policyService.issuePolicy(id);
    }
}
