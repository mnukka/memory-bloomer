package com.github.mnukka.memory_bloomer.domain.structure.bloom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Bloom math test.
 *
 * COLLISION_PROBABILITY - Statistically significantly probability is considered at the p=0.05 level.
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BloomMathTest {
    private final double COLLISION_PROBABILITY = 0.004956043133057324;
    private final int NUM_OF_HASH_FUNCTIONS = 2;
    private final int NUM_OF_ITEMS_TO_STORE = 338882;

    @Test
    void findCollisionProbability_withPreCalculatedVariables_equals() {
        int expectedBitCount = 9284410;
        double probability = BloomMath.findCollisionProbability(NUM_OF_HASH_FUNCTIONS, NUM_OF_ITEMS_TO_STORE, expectedBitCount);
        assertEquals(COLLISION_PROBABILITY, probability);
    }

    @Test
    void optimalBits_withPreCalculatedVariables_equalsRounded() {
        double m = BloomMath.optimalBits(NUM_OF_HASH_FUNCTIONS, NUM_OF_ITEMS_TO_STORE, COLLISION_PROBABILITY);
        int expectedBitCount = 9284438;
        assertEquals(expectedBitCount, Math.floor(m));
    }
}