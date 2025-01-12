package org.example.testruleengine.langParser;

import org.example.testruleengine.dlsResolver.DSLKeywordResolver;
import org.example.testruleengine.dlsResolver.DSLResolver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DSLParser {
    private final DSLKeywordResolver keywordResolver;
    private final DSLPatternUtil dslPatternUtil;

    public DSLParser(DSLKeywordResolver keywordResolver, DSLPatternUtil dslPatternUtil) {
        this.keywordResolver = keywordResolver;
        this.dslPatternUtil = dslPatternUtil;
    }

    public String resolveDomainSpecificKeywords(String expression, Map<String, Object> context) {
        Map<String, Object> dslKeywordToResolverValueMap = executeDSLResolver(expression, context);
        return replaceKeywordsWithValue(expression, dslKeywordToResolverValueMap);
    }

    private Map<String, Object> executeDSLResolver(String expression, Map<String, Object> context) {
        List<String> listOfDslKeyword = dslPatternUtil.getListOfDslKeywords(expression);
        Map<String, Object> dslKeywordToResolverValueMap = new HashMap<>();


        listOfDslKeyword.stream()
                .forEach(
                        dslKeyword -> {
                            String extractedDslKeyword = dslPatternUtil.extractKeyword(dslKeyword);
                            String keyResolver = dslPatternUtil.getKeywordResolver(extractedDslKeyword);
                            String keywordValue = dslPatternUtil.getKeywordValue(extractedDslKeyword);
                            DSLResolver resolver = keywordResolver.getResolver(keyResolver).get();
                            Object resolveValue = resolver.resolveValue(keywordValue, context);
                            dslKeywordToResolverValueMap.put(dslKeyword, resolveValue);
                        }
                );

        return dslKeywordToResolverValueMap;

    }

    private String replaceKeywordsWithValue(String expression, Map<String, Object> dslKeywordToResolverValueMap) {
        List<String> keyList = dslKeywordToResolverValueMap.keySet().stream().toList();
        for (String key : keyList) {
            expression = expression.replace(key, dslKeywordToResolverValueMap.get(key).toString());
        }
        return expression;
    }
}
