package com.example.memorybootcamp.generators;

import java.util.ArrayList;

public class NumberGenerator implements IGenerator{

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
