package org.example.testruleengine;

import java.util.ArrayList;
import java.util.List;

public class MyService implements MyInterface{
    public boolean isAccountNumberValid(ArrayList<Integer> list , int accountNumber) {
        return list.contains(accountNumber);
    }
}