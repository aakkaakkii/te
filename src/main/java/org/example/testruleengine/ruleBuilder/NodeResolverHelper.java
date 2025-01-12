package org.example.testruleengine.ruleBuilder;

import org.example.testruleengine.ruleImpl.RuleService;

import java.util.Arrays;
import java.util.List;

public class NodeResolverHelper {

    public static List<NodeResolver> initDefaultResolver() {
        return Arrays.asList(new GeneralNodeResolver());
    }

    public static List<NodeResolver> initWithCustomResolvers(List<Object> objects) {
        List<NodeResolver> nodeResolvers = NodeResolverHelper.initDefaultResolver();
        addResolvers(objects, nodeResolvers);
        return nodeResolvers;
    }

    public static void addResolvers(List<Object> objects, List<NodeResolver> nodeResolvers) {
        for (Object o : objects) {
            if (o instanceof NodeResolver) {
                nodeResolvers.add((NodeResolver) o);
            }
        }
    }
}
