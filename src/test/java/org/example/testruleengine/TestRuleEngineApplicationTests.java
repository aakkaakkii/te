package org.example.testruleengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.example.testruleengine.langParser.DSLPatternUtil;
import org.example.testruleengine.ruleEngine.RuleEngine;
import org.example.testruleengine.ruleImpl.LoanDetails;
import org.example.testruleengine.ruleImpl.LoanInferenceEngine;
import org.example.testruleengine.ruleImpl.UserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestRuleEngineApplicationTests {

    @Autowired
    private DSLPatternUtil dslPatternUtil;
    @Autowired
    private RuleEngine ruleEngine;
    @Autowired
    private LoanInferenceEngine loanInferenceEngine;

    @Test
    public void test11() {
        String str1 = "kitten";
        String str2 = "sitting";

        // Calculate Levenshtein Distance
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        int distance = levenshtein.apply(str1, str2);

        // Find the maximum length of the strings
        int maxLength = Math.max(str1.length(), str2.length());

        // Calculate percentage match
        double percentageMatch = (1 - ((double) distance / maxLength)) * 100;

        // Output the result
        System.out.printf("Percentage Match: %.2f%%\n", percentageMatch);
    }


    @Test
    public void test2() throws JsonProcessingException {

        String jsonQuery = """
            {
              "condition": "AND",
              "rules": [
                {
                   "condition": "AND",
                   "rules": [
                       {
                       "field": "accountNumber",
                       "operator": ">",
                       "value": 100
                       },
                       {
                       "field": "accountNumber2",
                       "operator": "equal",
                       "value": 100
                       }
                   ]
                },
                {
                  "field": "accountNumber",
                  "operator": "in",
                  "value": [101, 102, 105]
                },
                {
                  "condition": "OR",
                  "rules": [
                    {
                      "component": "RS1",
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

        String result = loanInferenceEngine.buildMvelExpression(queryNode);
        System.out.println(result);
    }

    @Test
    public void testRuleEngine() {
        UserDetails userDetails = new UserDetails();
        userDetails.setMonthlySalary(60000d);
        userDetails.setCibilScore(600);
        userDetails.setRequestedLoanAmount(10000d);
        userDetails.setAge(37);

        Map<String, Object> context = Map.of("testProp", "asdf");

        LoanDetails result = (LoanDetails) ruleEngine.run(loanInferenceEngine, userDetails, context);
        System.out.println(result);
    }




    final String expression1 = "input.salary >= 70000 && input.creditScore >= 900 && $(loan.interest) >= 8";
    final String expression2 = "$(input.salary) >= 70000 && input.creditScore >= 900 && $(loan.interest) >= 8";

    @Test
    public void verifyPatternInExpression(){
        String keyword = dslPatternUtil.getListOfDslKeywords(expression1).get(0);
        assertThat(keyword).isEqualTo("$(loan.interest)");
    }

    @Test
    public void verifyNumberOfPatternFoundInExpression(){
        int numberOfPatters = dslPatternUtil.getListOfDslKeywords(expression2).size();
        assertThat(numberOfPatters).isEqualTo(2);
    }

    @Test
    public void verifyExtractValue(){
        String keyword = dslPatternUtil.getListOfDslKeywords(expression1).get(0);
        assertThat(dslPatternUtil.extractKeyword(keyword)).isEqualTo("loan.interest");
    }

    @Test
    public void verifyKeywordResolverValue(){
        String keyword = dslPatternUtil.getListOfDslKeywords(expression1).get(0);
        assertThat(dslPatternUtil.getKeywordResolver(dslPatternUtil.extractKeyword(keyword))).isEqualTo("loan");
    }

    @Test
    public void verifyKeywordValue(){
        String keyword = dslPatternUtil.getListOfDslKeywords(expression1).get(0);
        assertThat(dslPatternUtil.getKeywordValue(dslPatternUtil.extractKeyword(keyword))).isEqualTo("interest");
    }

}
