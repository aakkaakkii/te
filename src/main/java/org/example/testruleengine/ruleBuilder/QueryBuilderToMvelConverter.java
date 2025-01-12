package org.example.testruleengine.ruleBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilderToMvelConverter {

    public static void main(String[] args) throws Exception {
        String jsonQuery = """
            {
              "condition": "AND",
              "rules": [
                {
                  "field": "accountNumber",
                  "operator": "in",
                  "value": [101, 102, 105]
                },
                {
                  "condition": "OR",
                  "rules": [
                    {
                      "field": "balance",
                      "operator": ">",
                      "value": 1000
                    },
                    {
                      "field": "status",
                      "operator": "equal",
                      "value": "active"
                    }
                  ]
                }
              ]
            }
        """;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode queryNode = mapper.readTree(jsonQuery);

        String mvelExpression = buildMvelExpression(queryNode);
        System.out.println("MVEL Expression: " + mvelExpression);

        Map<String, Object> input = new HashMap<>();
        input.put("accountNumber", 101);
        input.put("balance", 101);
        input.put("status", "deactive");



        boolean result = (Boolean) MVEL.eval(mvelExpression, Map.of(
                "input", input
        ));
        System.out.println(result);
    }

    private static String buildMvelExpression(JsonNode node) {
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
            String field = node.get("field").asText();
            String operator = mapOperator(node.get("operator").asText());
            JsonNode valueNode = node.get("value");
            JsonNode component = node.get("component");


            String value = valueNode.isArray() ?
                    valueNode.toString() : valueNode.isTextual() ?
                    "\"" + valueNode.asText() + "\"" : valueNode.asText();

            if ("contains".equals(operator) || "!contains".equals(operator)) {
                return String.format("%s %s input.%s", value, operator, field);
            } else {
                return String.format("input.%s %s %s", field, operator, value);
            }
        }
    }

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
}