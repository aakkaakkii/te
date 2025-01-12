package org.example.testruleengine.ruleImpl;

import jakarta.annotation.PostConstruct;
import org.example.testruleengine.ruleBuilder.GeneralNodeResolver;
import org.example.testruleengine.ruleBuilder.NodeResolver;
import org.example.testruleengine.ruleEngine.InferenceEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoanInferenceEngine extends InferenceEngine<UserDetails, LoanDetails> {
    private final Map<String, RuleService> serviceMap;
    private List<NodeResolver> nodeResolvers;

    public LoanInferenceEngine(@Qualifier("RuleGroup1") Map <String, RuleService> serviceMap) {
        this.serviceMap = serviceMap;

        nodeResolvers = new ArrayList<>();
        nodeResolvers.add(new GeneralNodeResolver());

        for (RuleService service : serviceMap.values()) {
            if (service instanceof NodeResolver) {
                nodeResolvers.add((NodeResolver) service);
            }
        }

    }

    @Override
    protected LoanDetails initializeOutputResult() {
        return new LoanDetails();
    }

    @Override
    protected Map<String, RuleService> initServiceMap() {
        return serviceMap;
    }


    @Override
    protected List<NodeResolver> getRuleResolver() {
        return nodeResolvers;
    }
}
