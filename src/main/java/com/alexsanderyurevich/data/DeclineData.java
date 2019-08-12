package com.alexsanderyurevich.data;

import com.alexsanderyurevich.utils.Pair;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class DeclineData {
    private static final EnumMap<Word, Pair<String, Map<Integer, Decline>>> declineWords = new EnumMap<>(Word.class);
    private static final Map<Integer, EnumMap<Gender, Decline>> digitWithGender = new HashMap<>();
    private static final Map<Integer, Decline> digits = new HashMap<>();
    private static final Map<Integer, Decline> numbers = new HashMap<>();
    private static final Map<Integer, Decline> decades = new HashMap<>();
    private static final Map<Integer, Decline> hundreds = new HashMap<>();

    private DeclineData() {
    }

    /**
     * Added word to container
     *
     * @param wordType wordType
     * @param caseType caseType
     * @param word     word
     * @param wordEnd  word end
     */
    public static void addWord(@Nonnull Word wordType, @Nonnull String word, int number, @Nonnull Cases caseType, @Nonnull String wordEnd) {
        requireNonNull(wordType, "wordType");
        requireNonNull(word, "word");
        requireNonNull(caseType, "caseType");
        requireNonNull(wordEnd, "wordEnd");
        Pair<String, Map<Integer, Decline>> declinesPair = declineWords.getOrDefault(wordType, new Pair<>(word, new HashMap<>()));
        Decline decline = declinesPair.getRight().getOrDefault(number, new Decline());
        decline.addDecline(caseType, wordEnd);
        declinesPair.getRight().put(number, decline);
        declineWords.put(wordType, declinesPair);
    }

    /**
     * Added decade to container
     *
     * @param decade      decades [1,2...,9]
     * @param caseType    caseType
     * @param declineWord word
     */
    public static void addDecade(int decade, @Nonnull Cases caseType, @Nonnull String declineWord) {
        requireNonNull(declineWord, "declineWord");
        requireNonNull(caseType, "caseType");
        if (decade < 1 || decade > 9) {
            throw new IllegalArgumentException();
        }
        Decline decline = decades.getOrDefault(decade, new Decline());
        decline.addDecline(caseType, declineWord);
        decades.put(decade, decline);
    }

    /**
     * Added hundred to container
     *
     * @param hundred     hundred [1,2...,9]
     * @param caseType    caseType
     * @param declineWord word
     */
    public static void addHundred(int hundred, @Nonnull Cases caseType, @Nonnull String declineWord) {
        requireNonNull(caseType, "caseType");
        requireNonNull(declineWord, "declineWord");
        if (hundred < 1 || hundred > 9) {
            throw new IllegalArgumentException();
        }
        Decline decline = hundreds.getOrDefault(hundred, new Decline());
        decline.addDecline(caseType, declineWord);
        hundreds.put(hundred, decline);
    }

    /**
     * Adds numbers with different cases in different faces
     *
     * @param gender       gender
     * @param caseType     caseType
     * @param declineDigit decline digit
     */
    public static void addDigitWithGender(int digit, @Nonnull Gender gender, @Nonnull Cases caseType, @Nonnull String declineDigit) {
        requireNonNull(gender, "gender");
        requireNonNull(caseType, "caseType");
        requireNonNull(declineDigit, "declineDigit");
        EnumMap<Gender, Decline> declineMap = digitWithGender.getOrDefault(digit, new EnumMap<>(Gender.class));
        Decline decline = declineMap.getOrDefault(gender, new Decline());
        decline.addDecline(caseType, declineDigit);
        declineMap.put(gender, decline);
        digitWithGender.put(digit, declineMap);
    }

    /**
     * Added digit to container
     *
     * @param digit        digit [1...9]
     * @param caseType     caseType
     * @param declineDigit decline digit
     */
    public static void addDigits(int digit, @Nonnull Cases caseType, @Nonnull String declineDigit) {
        requireNonNull(caseType, "caseType");
        requireNonNull(declineDigit, "declineDigit");
        if (digit < 1 || digit > 9) {
            throw new IllegalArgumentException();
        }
        Decline decline = digits.getOrDefault(digit, new Decline());
        decline.addDecline(caseType, declineDigit);
        digits.put(digit, decline);
    }

    /**
     * Added number to container
     *
     * @param number       number [11...19]
     * @param caseType     caseType
     * @param declineDigit decline number
     */
    public static void addNumber(int number, @Nonnull Cases caseType, @Nonnull String declineDigit) {
        requireNonNull(caseType, "caseType");
        requireNonNull(declineDigit, "declineDigit");
        if (number < 11 || number > 19) {
            throw new IllegalArgumentException();
        }
        Decline decline = numbers.getOrDefault(number, new Decline());
        decline.addDecline(caseType, declineDigit);
        numbers.put(number, decline);
    }

    /**
     * Returns a string representation for units
     *
     * @param unit     unit
     * @param caseType case type
     * @param gender   gender
     * @return a string representation for unit
     */
    public static String getStringUnit(int unit, @Nonnull Cases caseType, @Nonnull Gender gender) {
        if (unit < 1 || unit > 9) {
            throw new IllegalArgumentException();
        }
        requireNonNull(caseType, "caseType");
        requireNonNull(gender, "gender");
        EnumMap<Gender, Decline> map = digitWithGender.get(unit);
        if (map != null) {
            Decline decline = map.get(gender);
            return decline.getDecline(caseType);
        }
        Decline decline = digits.get(unit);
        return decline.getDecline(caseType);
    }

    /**
     * Returns a string representation for decades
     *
     * @param decade   decade
     * @param caseType case type
     * @return a string representation for decades
     */
    public static String getStringDecade(int decade, @Nonnull Cases caseType) {
        requireNonNull(caseType, "caseType");
        if (decade > 10 && decade < 20) {
            return numbers.get(decade).getDecline(caseType);
        }
        Decline decline = decades.get(decade);
        return decline.getDecline(caseType);
    }

    /**
     * Returns a string representation for hundreds
     *
     * @param hundred  hundred
     * @param caseType case type
     * @return a string representation for hundreds
     */
    public static String getStringHundred(int hundred, @Nonnull Cases caseType) {
        if (hundred < 1 || hundred > 9) {
            throw new IllegalArgumentException();
        }
        requireNonNull(caseType, "caseType");
        Decline decline = hundreds.get(hundred);
        return decline.getDecline(caseType);
    }

    /**
     * Returns the word in the desired declension
     *
     * @param word     word
     * @param caseType case type
     * @param forms    forms
     * @return the word in the desired declension
     */
    public static String getWord(@Nonnull Word word, @Nonnull Cases caseType, int forms) {
        if (forms < 0) {
            throw new IllegalArgumentException();
        }
        requireNonNull(word, "word");
        requireNonNull(caseType, "caseType");
        if (forms == 0) {
            forms = 1;
        }
        String wordEnd = declineWords.get(word).getRight().get(forms).getDecline(caseType);
        if (wordEnd.isEmpty()) {
            return declineWords.get(word).getLeft();
        }
        return declineWords.get(word).getLeft() + wordEnd;
    }

    /**
     * Word types
     */
    public enum Word {
        THOUSAND,
        MILLION,
        BILLION,
    }

    /**
     * Gender
     */
    public enum Gender {
        MALE,
        FEMALE,
        NEUTER
    }

    /**
     * Cases types
     */
    public enum Cases {
        NOMINATIVE,
        GENITIVE,
        ACCUSATIVE,
        DATIVE,
        INSTRUMENTAL,
        PREPOSITIONAL
    }
}
