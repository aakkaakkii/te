package org.example.testruleengine;

import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {
    public static void main(String[] args) {
        // Java object with the function
        MyInterface service = new MyService();
        MyInterface service2 = new MyService2();

        // Input data
        Map<String, Object> input = Map.of(
                "account", 101
        );


        Map<String, MyInterface> services = new HashMap<>();
        services.put("service", service);
        services.put("service2", service2);

        // MVEL Expression
        String expression = """
            services.service.isAccountNumberValid([101, 102, "104"], input.account) && services.service2.isGreaterThan("123")
            """;

        String expression1 = """
            input.account in [101, 102, 104]
            """;

        // Evaluate the expression
        boolean result = (Boolean) MVEL.eval(expression1, Map.of(
                "input", input,
                "services", services // Pass the Java object to the MVEL context
        ));

        System.out.println("Result: " + result); // Output: Result: true
    }
}

