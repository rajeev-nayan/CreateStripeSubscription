package org.example.evaluations.evaluation.controllers;

import org.example.evaluations.evaluation.dtos.SubscriptionRequestDto;
import org.example.evaluations.evaluation.services.ISubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Subscription;
import com.stripe.param.PlanCreateParams;

@RestController
public class SubscriptionController {

    @Autowired
    private ISubscriptionService subscriptionService;

    @PostMapping("/subscription")
    private String subscription(@RequestBody SubscriptionRequestDto dto) {
        PlanCreateParams.Interval interval = getInterval(dto.getBillingFrequency().name());
        return subscriptionService.createSubscriptionForProduct(dto.getCustomerName(), dto.getCustomerEmail(),
                dto.getProductAmount(), dto.getProductName(), interval);
    }

    private PlanCreateParams.Interval getInterval(String frequency) {
        return switch (frequency.toLowerCase()) {
            case "day" -> PlanCreateParams.Interval.DAY;
            case "week" -> PlanCreateParams.Interval.WEEK;
            case "month" -> PlanCreateParams.Interval.MONTH;
            case "year" -> PlanCreateParams.Interval.YEAR;
            default -> throw new IllegalArgumentException(
                    "Unsupported frequency: " + frequency);
        };
    }
}
