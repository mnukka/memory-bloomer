package examples;

import com.github.mnukka.memory_bloomer.api.IMemoryCache;
import com.github.mnukka.memory_bloomer.api.MemoryCacheFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Spellchecker {

    @Test
    public void createImmutableCache_withBloomFilter_canActAsSpellcheckDictionary() throws URISyntaxException, IOException {
        List<String> wordList;
        var path = Paths.get(Spellchecker.class.getClassLoader().getResource("wordlist.txt").toURI().getPath());
        try (Stream<String> lines = Files.lines(path)) {
            wordList = lines.collect(Collectors.toList());
        }

        IMemoryCache dictionaryDatabase = MemoryCacheFactory.createImmutableCache(wordList);

        List<String> cleanedWordsTypedByUser = Arrays.asList("I", "love", "how", "we", "have", "a", "new", "programming", "language", "every", "year");
        boolean result = dictionaryDatabase.isKeyPresent("two");
        assert result;
    }

}
