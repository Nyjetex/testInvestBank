package com.alexsanderyurevich.converter;

import com.alexsanderyurevich.data.DeclineData;
import com.alexsanderyurevich.data.DeclineData.Cases;
import com.alexsanderyurevich.data.DeclineData.Gender;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Class converter
 */
public final class Converter {
    private static final long RANGE_VALUE = 999_999_999_999L;
    private Converter() {
    }

    private enum Category {
        BILLIONS,//1,000,000,000
        MILLIONS,//1,000,000
        THOUSANDS,//1,000
        HUNDREDS,//100
        DECADES,//10
        UNITS,//1
    }

    /**
     * Converts a number to a string representation
     *
     * @param number number in range [-999_999_999_999L, 999_999_999_999L]
     * @param cases  cases
     * @param gender gender
     * @return string representation
     */
    @Nonnull
    public static String convert(long number, @Nonnull Cases cases, @Nonnull Gender gender) {
        Objects.requireNonNull(cases, "cases");
        Objects.requireNonNull(gender, "gender");

        if (number > RANGE_VALUE || number < -RANGE_VALUE) {
            throw new IllegalArgumentException(MessageFormat.format("The value must be in range[{0};{1}]",
                    -RANGE_VALUE, RANGE_VALUE));
        }
        if (number == 0) {
            return "ноль";
        }
        String prefix = "";
        if (number < 0) {
            number *= -1;
            prefix = "минус ";
        }
        EnumMap<Category, Integer> categories = parseNumber(number);
        return prefix + getStringRepresentForNumber(cases, gender, categories).trim();
    }

    private static String getStringRepresentForNumber(@Nonnull Cases cases, @Nonnull Gender gender, EnumMap<Category, Integer> categories) {
        StringBuilder representNumber = new StringBuilder();
        for (Entry<Category, Integer> category : categories.entrySet()) {
            if (category.getValue() == 0) continue;
            representNumber.append(getStringRepresent(category, cases, gender));
            representNumber.append(" ");
        }
        return representNumber.toString();
    }

    private static String getStringRepresent(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases, @Nonnull Gender gender) {
        String represent = "";
        switch (category.getKey()) {
            case UNITS: {
                represent = getStringRepresentForUnits(category, cases, gender);
                break;
            }
            case DECADES: {
                represent = getStringRepresentForDecades(category, cases);
                break;
            }
            case HUNDREDS: {
                represent = getStringRepresentForHundreds(category, cases);
                break;
            }
            case THOUSANDS: {
                represent = getStringRepresentForThousand(category, cases);
                break;
            }
            case MILLIONS: {
                represent = getStringRepresentForMillions(category, cases);
                break;
            }
            case BILLIONS: {
                represent = getStringRepresentForBillions(category, cases);
                break;
            }
        }
        return represent;
    }

    private static String getStringRepresentForBillions(Entry<Category, Integer> category, Cases cases) {
        return getStringRepresentFor(category, cases, Gender.MALE, DeclineData.Word.BILLION);
    }

    private static String getStringRepresentForMillions(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases) {
        return getStringRepresentFor(category, cases, Gender.MALE, DeclineData.Word.MILLION);
    }

    private static String getStringRepresentForThousand(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases) {
        return getStringRepresentFor(category, cases, Gender.FEMALE, DeclineData.Word.THOUSAND);
    }

    private static String getStringRepresentFor(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases, @Nonnull Gender gender, @Nonnull DeclineData.Word wordType) {
        EnumMap<Category, Integer> categories = parseNumber(category.getValue());
        String represent = getStringRepresentForNumber(cases, gender, categories);

        int c = getUsedForms(categories);
        String word = DeclineData.getWord(wordType, cases, c);
        return represent + word;
    }

    private static int getUsedForms(EnumMap<Category, Integer> digitNumber) {
        int c = 0;
        int decades = digitNumber.get(Category.DECADES);
        int units = digitNumber.get(Category.UNITS);
        int hundreds = digitNumber.get(Category.HUNDREDS);
        //Определяем склонение разряда (тысяч, тысячи, миллион, миллионами и т.д)
        //100 тысяч, 200 тысяч и т.д, т.е только разряды сотен, десяток и единиц нет
        if (hundreds >= 1 && decades == 0 && units == 0) {
            c = 5;
        }
        //20 тысяч, 60 миллионов и т.д (т.е используем множественную форму)
        if (decades >= 2 && units == 0) {
            c = 5;
        }
        //Определяем склонение для 11...19 и 1...9
        //Если это просто цифра от 0 до 9 или число от 21 до 99
        //то для определения формы (ед или мн. число) берем последнюю цифру
        if ((decades == 0 || decades > 1) && units > 0) {
            c = units;
        } else if (decades > 10 && decades < 20) {
            //в разряде десяток может быть записано число от 11 до 19
            //тогда используем множественную форму
            c = 5;
        }
        return c;
    }

    private static String getStringRepresentForHundreds(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases) {
        return DeclineData.getStringHundred(category.getValue(), cases);
    }

    private static String getStringRepresentForDecades(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases) {
        return DeclineData.getStringDecade(category.getValue(), cases);
    }

    private static String getStringRepresentForUnits(@Nonnull Entry<Category, Integer> category, @Nonnull Cases cases, @Nonnull Gender gender) {
        return DeclineData.getStringUnit(category.getValue(), cases, gender);
    }

    @Nonnull
    private static EnumMap<Category, Integer> parseNumber(long number) {
        EnumMap<Category, Integer> categories = new EnumMap<>(Category.class);
        int billions = (int) (number / 1_000_000_000);
        long step = number - billions * 1_000_000_000L;
        int millions = (int) (step / 1_000_000);
        step = step - millions * 1_000_000;
        int thousand = (int) (step / 1_000);
        step = step - thousand * 1_000;
        int hundreds = (int) (step / 100);
        step = step - hundreds * 100;
        //Числа с 11..19 имеют свои названия, не составные как например 21 (двадцать + один)
        //поэтому если получили число от 11 до 19, то запишшем его полностью в разряд десяток, а в разряд единиц запишем 0
        int decades = (step > 10 && step < 20) ? (int) step : (int) step / 10;

        int units;
        if (decades < 10) {
            step = step - decades * 10;
            units = (int) (step);
        } else {
            units = 0;
        }

        categories.put(Category.BILLIONS, billions);
        categories.put(Category.MILLIONS, millions);
        categories.put(Category.THOUSANDS, thousand);
        categories.put(Category.HUNDREDS, hundreds);
        categories.put(Category.DECADES, decades);
        categories.put(Category.UNITS, units);
        return categories;
    }
}
