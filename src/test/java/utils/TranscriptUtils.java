package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class TranscriptUtils {

    private TranscriptUtils() {
    }

    public static double calculateWordSimilarity(String expectedText, String actualText) {
        String normalizedExpected = normalize(expectedText);
        String normalizedActual = normalize(actualText);

        if (normalizedExpected.isEmpty() || normalizedActual.isEmpty()) {
            return 0.0;
        }

        Set<String> expectedWords = new HashSet<>(
                Arrays.asList(normalizedExpected.split("\\s+"))
        );

        Set<String> actualWords = new HashSet<>(
                Arrays.asList(normalizedActual.split("\\s+"))
        );

        Set<String> commonWords = new HashSet<>(expectedWords);
        commonWords.retainAll(actualWords);

        return (double) commonWords.size() / expectedWords.size();
    }

    private static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}