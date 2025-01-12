package org.example.testruleengine.langParser;

import org.example.testruleengine.ruleImpl.RuleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RuleParser<INPUT_DATA, OUTPUT_RESULT> {
    private final DSLParser dslParser;
    private final MVELParser mvelParser;

    private final String INPUT_KEYWORD = "input";
    private final String OUTPUT_KEYWORD = "output";
    private final String SERVICES_KEYWORD = "services";

    public RuleParser(DSLParser dslParser,
                      MVELParser mvelParser) {
        this.dslParser = dslParser;
        this.mvelParser = mvelParser;
    }

    public boolean parseCondition(String expression, INPUT_DATA inputData, Map<String, Object> context, Map<String, RuleService> services) {
        String resolvedDslExpression = dslParser.resolveDomainSpecificKeywords(expression, context);
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEYWORD, inputData);
        input.put(SERVICES_KEYWORD, services);
        boolean match = mvelParser.parseMvelExpression(resolvedDslExpression, input);
        return match;
    }

    public OUTPUT_RESULT parseAction(String expression, INPUT_DATA inputData, OUTPUT_RESULT outputResult) {
        String resolvedDslExpression = dslParser.resolveDomainSpecificKeywords(expression, null);
        Map<String, Object> input = new HashMap<>();
        input.put(INPUT_KEYWORD, inputData);
        input.put(OUTPUT_KEYWORD, outputResult);
        mvelParser.parseMvelExpression(resolvedDslExpression, input);
        return outputResult;
    }
}
