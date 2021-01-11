package com.example.memorybootcamp.generators;

import java.util.List;

public interface IGenerator {
    /**
     * Generates a sequence of samples with given length
     */
    default List<?> generateSequence(int length) {
        return null;
    }
}
