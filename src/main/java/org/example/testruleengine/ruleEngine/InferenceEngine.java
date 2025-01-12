package org.example.testruleengine.ruleEngine;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.example.testruleengine.langParser.RuleParser;
import org.example.testruleengine.models.Rule;
import org.example.testruleengine.ruleBuilder.GeneralNodeResolver;
import org.example.testruleengine.ruleBuilder.NodeResolver;
import org.example.testruleengine.ruleBuilder.NodeResolverHelper;
import org.example.testruleengine.ruleImpl.FailureDetails;
import org.example.testruleengine.ruleImpl.RuleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public abstract class InferenceEngine<INPUT_DATA, OUTPUT_RESULT> {
    @Autowired
    protected RuleParser<INPUT_DATA, OUTPUT_RESULT> ruleParser;


    public OUTPUT_RESULT run(List<Rule> listOfRules, INPUT_DATA inputData, FailureDetails failureDetails, Map<String, Object> context) {
        if (null == listOfRules || listOfRules.isEmpty()) {
            return null;
        }

        //STEP 1 (MATCH) : Match the facts and data against the set of rules.
        List<Rule> conflictSet = match(listOfRules, inputData, context, failureDetails);

        //STEP 2 (RESOLVE) : Resolve the conflict and give the selected one rule.
        Rule resolvedRule = resolve(conflictSet);
        if (null == resolvedRule) {
            return null;
        }

        //STEP 3 (EXECUTE) : Run the action of the selected rule on given data and return the output.
        OUTPUT_RESULT outputResult = executeRule(resolvedRule, inputData);

        return outputResult;
    }

    public ValidationResult validateAgainstRules(List<Rule> listOfRules, INPUT_DATA inputData, Map<String, Object> context) {
        ValidationResult validationResult = new ValidationResult();
        for (Rule rule : listOfRules) {
            String condition = rule.getCondition();
            boolean b = ruleParser.parseCondition(condition, inputData, context, initServiceMap());
            if (b) {
                validationResult.addSuccessful(rule.getRuleId());
            } else {
                validationResult.addFailure(rule.getRuleId());
            }
        }

        return validationResult;
    }

    protected List<Rule> match(List<Rule> listOfRules, INPUT_DATA inputData, Map<String, Object> context, FailureDetails failureDetails) {
        return listOfRules.stream()
                .filter(
                        rule -> {
                            String condition = rule.getCondition();
                            boolean b = ruleParser.parseCondition(condition, inputData, context, initServiceMap());
                            if (!b) {
                                failureDetails.addFailure(rule.getRuleId());
                            }
                            return b;
                        }
                )
                .collect(Collectors.toList());
    }

    protected Rule resolve(List<Rule> conflictSet) {
        Optional<Rule> rule = conflictSet.stream()
                .min(Comparator.comparing(Rule::getPriority));
        return rule.orElse(null);
    }


    protected OUTPUT_RESULT executeRule(Rule rule, INPUT_DATA inputData) {
        OUTPUT_RESULT outputResult = initializeOutputResult();
        return ruleParser.parseAction(rule.getAction(), inputData, outputResult);
    }

    protected Map<String, RuleService> initServiceMap() {
        return new HashMap<>();
    }

    protected List<NodeResolver> getRuleResolver() {
        return NodeResolverHelper.initDefaultResolver();
    }

    public String buildMvelExpression(JsonNode node) {
        if (node.has("rules")) {
            String condition = node.get("condition").asText();
            String logicalOperator = "AND".equalsIgnoreCase(condition) ? "&&" : "||";

            StringBuilder expression = new StringBuilder();
            for (JsonNode rule : node.get("rules")) {
                if (expression.length() > 0) {
                    expression.append(" ").append(logicalOperator).append(" ");
                }
                expression.append(buildMvelExpression(rule));
            }
            return "(" + expression + ")";
        } else {
            JsonNode component = node.get("component");
            return getRuleResolver().stream()
                    .filter(e -> e.resolve(component == null ? null : component.asText()))
                    .findFirst()
                    .get()
                    .parse(node);
        }
    }

    protected abstract OUTPUT_RESULT initializeOutputResult();

    public static class  ValidationResult {
        private List<String> failureList = new ArrayList<>();
        private List<String> successfulList = new ArrayList<>();

        public void addFailure(String ruleName) {
            failureList.add(ruleName);
        }

        public List<String> getFailureList() {
            return failureList;
        }

        public void addSuccessful(String ruleName) {
            successfulList.add(ruleName);
        }

        public List<String> getSuccessfulList() {
            return successfulList;
        }
    }

}
