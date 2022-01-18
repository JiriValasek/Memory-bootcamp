package com.example.memorybootcamp.generators;

import java.util.ArrayList;

/** Generator for decimal number sequences. */
public class NumberGenerator implements IGenerator{

    /** Method generating a sequence of decimal numbers to remember. */
    @Override
    public ArrayList<String> generateSequence(int length) {
        final ArrayList<String> result = new ArrayList<>();
        int elem;
        for (int i = 0; i < length; i++) {
            elem = (int) Math.floor(10 * Math.random());
            result.add(String.valueOf(elem));
        }
        return result;
    }
}
