package org.example.testruleengine.langParser;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DSLPatternUtil {
    private static final Pattern DSL_PATTERN = Pattern.compile("\\$\\((\\w+)(\\.\\w+)\\)"); //$(rulenamespace.keyword)
    private static final String DOT = ".";


    public List<String>  getListOfDslKeywords(String expression) {
        Matcher matcher = DSL_PATTERN.matcher(expression);
        List<String> listOfDslKeyword = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            listOfDslKeyword.add(group);
        }
        return listOfDslKeyword;
    }

    public String extractKeyword(String keyword) {
        return keyword.substring(keyword.indexOf('(') + 1,
                keyword.indexOf(')'));
    }

    public String getKeywordResolver(String dslKeyword){
        return dslKeyword.split("\\"+DOT)[0];
    }

    public String getKeywordValue(String dslKeyword){
        return dslKeyword.split("\\"+DOT)[1];
    }
}
