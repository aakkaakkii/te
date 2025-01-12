package org.example.testruleengine.ruleBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

public class GeneralNodeResolver implements NodeResolver {

    private static final String GENERAL = "GENERAL";

    private static String mapOperator(String operator) {
        return switch (operator) {
            case "equal" -> "==";
            case "not_equal" -> "!=";
            case "in" -> "contains";
            case "not_in" -> "!contains";
            case ">" -> ">";
            case "<" -> "<";
            case ">=" -> ">=";
            case "<=" -> "<=";
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    @Override
    public boolean resolve(String key) {
        return key == null || key.equalsIgnoreCase(GENERAL);
    }

    @Override
    public String parse(JsonNode node) {
        String field = node.get("field").asText();
        String operator = mapOperator(node.get("operator").asText());
        JsonNode valueNode = node.get("value");

        String value = valueNode.isArray() ?
                valueNode.toString() : valueNode.isTextual() ?
                "\"" + valueNode.asText() + "\"" : valueNode.asText();

        if ("contains".equals(operator) || "!contains".equals(operator)) {
            return String.format("%s %s input.%s", value, operator, field);
        } else {
            return String.format("input.%s %s %s", field, operator, value);
        }    }
}
