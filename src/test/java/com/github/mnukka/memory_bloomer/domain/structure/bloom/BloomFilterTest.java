package com.github.mnukka.memory_bloomer.domain.structure.bloom;

import com.github.mnukka.memory_bloomer.domain.hash.IHashFunction;
import com.github.mnukka.memory_bloomer.domain.hash.MurmurHash;
import com.github.mnukka.memory_bloomer.domain.hash.SpookyHash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BloomFilterTest {

    @Test
    void createCache_WithNullArguments_ThrowsNPE() {
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        Assertions.assertThrows(NullPointerException.class, () -> bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), null));
        Assertions.assertThrows(NullPointerException.class, () -> bloomFilter32.createCache(null, Arrays.asList("one", "two")));
    }

    @Test
    void createCache_WithEmptyList_ThrowsIllegalArgumentException() {
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> bloomFilter32.createCache(Collections.emptyList(), Arrays.asList("one", "two")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), Collections.emptyList()));
    }

    @Test
    void createCache_WithValidArguments_returnsBloomFilterWithoutExceptions() {
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        Assertions.assertDoesNotThrow(() -> bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), Arrays.asList("one", "two")));
    }

    @Test
    void createCache_withOneHashAndOneItem_matchesOptimalBits() {
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), Collections.singletonList("one"));
        final BloomProperties bloomProperties = bloomFilter32.getProperties();
        double recommendedBits = Math.floor(BloomMath.optimalBits(1, 1, 0.005));
        Assertions.assertEquals(recommendedBits, bloomProperties.getBitmapSize());
    }

    @Test
    void createCache_withOneHashAndOneItem_matchesBloomProperties() {
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), Collections.singletonList("one"));
        final BloomProperties bloomProperties = bloomFilter32.getProperties();
        Assertions.assertEquals(0.005, bloomProperties.getCollisionProbability());
        Assertions.assertEquals(199, bloomProperties.getBitmapSize());
        Assertions.assertEquals(1, bloomProperties.getHashFunctionCount());
        Assertions.assertEquals(IHashFunction.getSeed(), bloomProperties.getSeed());
    }

    @Test
    void isKeyPresent_withItemsFromList_match() {
        BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        bloomFilter32.createCache(Collections.singletonList(new MurmurHash()), Collections.singletonList("one"));
        assert bloomFilter32.isKeyPresent("one");
    }

    @Test
    void isKeyPresent_keysFromWordList_allMatch() throws IOException, URISyntaxException {
        final List<String> wordList = loadWordList();
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        bloomFilter32.createCache(Arrays.asList(new MurmurHash(), new SpookyHash()), wordList);
        assert wordList.size() > 0;
        assert wordList.stream().allMatch(bloomFilter32::isKeyPresent);
    }

    @Test
    void isKeyPresent_randomStringsNotInWordList_lessThan1In200Match() throws IOException, URISyntaxException {
        List<Double> falsePositiveList = new ArrayList<>();
        final List<String> wordList = loadWordList();
        final BloomFilter32<String> bloomFilter32 = new BloomFilter32<>();
        bloomFilter32.createCache(Arrays.asList(new MurmurHash(), new SpookyHash()), wordList);
        for (int i = 0; i < 10; i++) {
            List<String> randomStringList = createRandomStringList(10000);
            List<String> matchedList = randomStringList
                    .stream()
                    .filter(bloomFilter32::isKeyPresent)
                    .collect(Collectors.toList());
            List<String> notMatched = randomStringList
                    .stream()
                    .filter(p -> !bloomFilter32.isKeyPresent(p))
                    .collect(Collectors.toList());
            matchedList.removeAll(wordList);

            final int total = notMatched.size() + matchedList.size();
            final double falsePositivePct = matchedList.size() / 100D / (total / 100D);
            falsePositiveList.add(falsePositivePct);
        }
        final double average = (falsePositiveList.stream().reduce(Double::sum).get() / falsePositiveList.size());
        Assertions.assertTrue(Math.round(average * 1000D) / 1000D <= 0.005D);
    }

    private List<String> loadWordList() throws IOException, URISyntaxException {
        List<String> wordList;
        var path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("wordlist.txt")).toURI());
        try (Stream<String> lines = Files.lines(path)) {
            wordList = lines.collect(Collectors.toList());
        }
        return wordList;
    }

    private List<String> createRandomStringList(final int wordListSize) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < wordListSize; i++) {
            list.add(generateRandomString());
        }
        return list;
    }

    private String generateRandomString() {
        return new Random().ints(97, 122 + 1)
                .limit(5)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}