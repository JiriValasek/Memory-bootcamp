package com.example.memorybootcamp.generators;

import java.util.ArrayList;

public class BinaryNumberGenerator implements IGenerator{

    @Override
    public ArrayList<String> generateSequence(int length) {

        ArrayList<String> result = new ArrayList<>();
        int elem;
        for (int i = 0; i < length; i++) {
            elem = (int) Math.round(Math.random());
            result.add(String.valueOf(elem));
        }
        return result;
    }
}
