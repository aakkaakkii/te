package org.example.testruleengine;

public class MyService2 implements MyInterface{

    public boolean isGreaterThan(String str) {
        return str.length() > 5;
    }
}
