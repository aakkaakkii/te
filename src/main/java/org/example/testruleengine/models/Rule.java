package org.example.testruleengine.models;


public class Rule {
    String ruleId;
    String condition;
    String action;
    Integer priority;
    String description;

    public Rule() {
    }

    public Rule(String ruleId, String condition, String action, Integer priority, String description) {
        this.ruleId = ruleId;
        this.condition = condition;
        this.action = action;
        this.priority = priority;
        this.description = description;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
