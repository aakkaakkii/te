package org.example.testruleengine.ruleEngine;

import org.example.testruleengine.models.Rule;
import org.example.testruleengine.ruleImpl.FailureDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RuleEngine {
    final String expression1 = "input.monthlySalary >= 50000.0 && input.cibilScore >= 500 && input.requestedLoanAmount<1500000";
    final String expression2 = "(input.monthlySalary < 50000.0 && input.cibilScore <= 300 && input.requestedLoanAmount >= 1000000) || $(bank.target_done) == true";
    final String expression3 = "[31, 32, 33].contains(input.age)";
    final String expression4 = "services.RS1.execute() == false";

    private List<Rule> allRulesByNamespace;

    public RuleEngine() {
        this.allRulesByNamespace = Arrays.asList(
                new Rule("1", expression1, "output.setInterestRate(10);output.addData(\"asdf\")", 2, "test"),
//                new Rule("1", expression1, "output.setInterestRate(10);output.addData(\"asdf\")", 1, "test"),
                new Rule("3", expression4, "output.setSanctionedPercentage(11111)", 1, "test"),
                new Rule("2", expression1, "output.setSanctionedPercentage(11)", 3, "test")
        );
    }

    public Object run(InferenceEngine inferenceEngine, Object inputData, Map<String, Object> context) {
        //        String ruleNamespace = inferenceEngine.getRuleNamespace().toString();

        FailureDetails failureDetails = new FailureDetails();

        Object result = inferenceEngine.run(allRulesByNamespace, inputData, failureDetails, context);
        return result;
    }

    public Object run(InferenceEngine inferenceEngine, Object inputData) {
        return run(inferenceEngine, inputData, null);
    }


    public void setAllRulesByNamespace(List<Rule> allRulesByNamespace) {
        this.allRulesByNamespace = allRulesByNamespace;
    }
}
