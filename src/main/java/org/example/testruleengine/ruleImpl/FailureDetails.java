package org.example.testruleengine.ruleImpl;

import java.util.ArrayList;
import java.util.List;

public class FailureDetails {

    private List<String> failureList = new ArrayList<>();

    public void addFailure(String ruleName) {
        failureList.add(ruleName);
    }

    public List<String> getFailureList() {
        return failureList;
    }
}
