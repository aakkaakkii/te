package org.example.testruleengine.langParser;

import org.mvel2.MVEL;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MVELParser {

    public boolean parseMvelExpression( String expression, Map<String, Object> inputObjects){
        try {
            return MVEL.evalToBoolean(expression,inputObjects);
        }catch (Exception e){
//            log.error("Can not parse Mvel Expression : {} Error: {}", expression, e.getMessage());
        }
        return false;
    }
}
