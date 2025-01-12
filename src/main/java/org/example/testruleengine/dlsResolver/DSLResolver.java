package org.example.testruleengine.dlsResolver;

import java.util.Map;

public interface DSLResolver {
    String getResolverKeyword();
    Object resolveValue(String keyword, Map<String, Object> context);
}
