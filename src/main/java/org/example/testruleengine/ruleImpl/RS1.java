package org.example.testruleengine.ruleImpl;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.testruleengine.ruleBuilder.NodeResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("RuleGroup1")
public class RS1 implements RuleService, NodeResolver {
    public boolean execute() {
        System.out.println("execute is executed");
        return false;
    }

    @Override
    public boolean resolve(String key) {
        return key != null && key.equalsIgnoreCase("RS1");
    }

    @Override
    public String parse(JsonNode node) {
        return "true == true";
    }
}
