package com.springbootcamp.ecommerceapp.utils;

import java.util.HashSet;
import java.util.Set;

public class StringToMapParser {

    public static Set<String> toSetOfValues(String value){
        Set<String> values = new HashSet<>();
        String[] splitValues = value.split(",");

        for(String splitValue : splitValues){
            values.add(splitValue);
        }
        return values;
    }

    public static String toCommaSeparatedString(Set<String> valueSet){
        String values = "";
        if(valueSet==null || valueSet.isEmpty())
            return values;

        values = String.join(",", valueSet);
        return values;
    }

}
