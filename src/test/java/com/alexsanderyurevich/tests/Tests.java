package com.alexsanderyurevich.tests;

import com.alexsanderyurevich.loader.DeclineXmlLoader;
import org.junit.Test;

import static com.alexsanderyurevich.converter.Converter.convert;
import static com.alexsanderyurevich.data.DeclineData.Cases.*;
import static com.alexsanderyurevich.data.DeclineData.Gender.*;
import static org.junit.Assert.assertEquals;

public final class Tests {

    static {
        DeclineXmlLoader.loadDeclineData();
    }

    @Test
    public void testConvert() {
        assertEquals("ноль", convert(0, NOMINATIVE, MALE));
        assertEquals("ноль", convert(-0, NOMINATIVE, MALE));
        assertEquals("минус один", convert(-1, NOMINATIVE, MALE));

        assertEquals("один", convert(1, NOMINATIVE, MALE));
        assertEquals("одного", convert(1, GENITIVE, MALE));
        assertEquals("одному", convert(1, DATIVE, MALE));
        assertEquals("один", convert(1, ACCUSATIVE, MALE));
        assertEquals("одним", convert(1, INSTRUMENTAL, MALE));
        assertEquals("одном", convert(1, PREPOSITIONAL, MALE));

        assertEquals("одна", convert(1, NOMINATIVE, FEMALE));
        assertEquals("одной", convert(1, GENITIVE, FEMALE));
        assertEquals("одной", convert(1, DATIVE, FEMALE));
        assertEquals("одну", convert(1, ACCUSATIVE, FEMALE));
        assertEquals("одной", convert(1, INSTRUMENTAL, FEMALE));
        assertEquals("одной", convert(1, PREPOSITIONAL, FEMALE));

        assertEquals("одно", convert(1, NOMINATIVE, NEUTER));
        assertEquals("одного", convert(1, GENITIVE, NEUTER));
        assertEquals("одному", convert(1, DATIVE, NEUTER));
        assertEquals("одно", convert(1, ACCUSATIVE, NEUTER));
        assertEquals("одним", convert(1, INSTRUMENTAL, NEUTER));
        assertEquals("одном", convert(1, PREPOSITIONAL, NEUTER));

        assertEquals("два", convert(2, NOMINATIVE, MALE));
        assertEquals("двух", convert(2, GENITIVE, MALE));
        assertEquals("двум", convert(2, DATIVE, MALE));
        assertEquals("два", convert(2, ACCUSATIVE, MALE));
        assertEquals("двумя", convert(2, INSTRUMENTAL, MALE));
        assertEquals("двух", convert(2, PREPOSITIONAL, MALE));

        assertEquals("три", convert(3, NOMINATIVE, MALE));
        assertEquals("трех", convert(3, GENITIVE, MALE));
        assertEquals("трем", convert(3, DATIVE, MALE));
        assertEquals("три", convert(3, ACCUSATIVE, MALE));
        assertEquals("тремя", convert(3, INSTRUMENTAL, MALE));
        assertEquals("трех", convert(3, PREPOSITIONAL, MALE));

        assertEquals("одиннадцать", convert(11, NOMINATIVE, MALE));
        assertEquals("девятнадцать", convert(19, NOMINATIVE, MALE));

        assertEquals("двадцать один", convert(21, NOMINATIVE, MALE));

        assertEquals("двадцать", convert(20, NOMINATIVE, MALE));
        assertEquals("тридцать", convert(30, NOMINATIVE, MALE));
        assertEquals("сорок", convert(40, NOMINATIVE, MALE));
        assertEquals("пятьдесят", convert(50, NOMINATIVE, MALE));
        assertEquals("шестьдесят", convert(60, NOMINATIVE, MALE));
        assertEquals("семьдесят", convert(70, NOMINATIVE, MALE));
        assertEquals("восемьдесят", convert(80, NOMINATIVE, MALE));
        assertEquals("девяносто", convert(90, NOMINATIVE, MALE));

        assertEquals("сто", convert(100, NOMINATIVE, MALE));
        assertEquals("двести", convert(200, NOMINATIVE, MALE));
        assertEquals("триста", convert(300, NOMINATIVE, MALE));
        assertEquals("четыреста", convert(400, NOMINATIVE, MALE));
        assertEquals("пятьсот", convert(500, NOMINATIVE, MALE));
        assertEquals("шестьсот", convert(600, NOMINATIVE, MALE));
        assertEquals("семьсот", convert(700, NOMINATIVE, MALE));
        assertEquals("восемьсот", convert(800, NOMINATIVE, MALE));
        assertEquals("девятьсот", convert(900, NOMINATIVE, MALE));

        assertEquals("сто одиннадцать", convert(111, NOMINATIVE, MALE));
        assertEquals("сто двадцать", convert(120, NOMINATIVE, MALE));
        assertEquals("сто двадцать один", convert(121, NOMINATIVE, MALE));
        assertEquals("сто один", convert(101, NOMINATIVE, MALE));

        assertEquals("одна тысяча", convert(1_000, NOMINATIVE, MALE));
        assertEquals("сто тысяч", convert(100_000, NOMINATIVE, MALE));
        assertEquals("один миллион", convert(1_000_000, NOMINATIVE, MALE));
        assertEquals("сто миллионов", convert(100_000_000, NOMINATIVE, MALE));
        assertEquals("один миллиард", convert(1_000_000_000, NOMINATIVE, MALE));
        assertEquals("сто миллиардов", convert(100_000_000_000L, NOMINATIVE, MALE));
    }
}
