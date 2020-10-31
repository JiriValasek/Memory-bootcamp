package com.example.memorybootcamp.generators;

import org.json.JSONObject;

public interface IGenerator {
    /** Generates a sequence of samples with given length*/
    JSONObject generateSequence(int length);
}
