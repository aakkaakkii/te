package org.example.testruleengine.ruleBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public interface NodeResolver {

    boolean resolve(String key);
    String parse(JsonNode node);
}
