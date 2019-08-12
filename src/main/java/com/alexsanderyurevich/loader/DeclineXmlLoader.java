package com.alexsanderyurevich.loader;

import com.alexsanderyurevich.data.DeclineData;
import com.alexsanderyurevich.data.DeclineData.Cases;
import com.alexsanderyurevich.data.DeclineData.Gender;
import com.alexsanderyurevich.data.DeclineData.Word;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DeclineXmlLoader {
    private static final String DECLINE_DATA_FILE_NAME = "decline.xml";

    private DeclineXmlLoader() { }

    public static void loadDeclineData() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream resource = Objects.requireNonNull(DeclineXmlLoader.class.getClassLoader().getResourceAsStream(DECLINE_DATA_FILE_NAME), "Not found resource");

            Document document = builder.parse(resource);
            document.getDocumentElement().normalize();

            NodeList types = document.getElementsByTagName("type");
            for (int i = 0; i < types.getLength(); i++) {
                Node node = types.item(i);
                Types type = Enum.valueOf(Types.class, ((Element) node).getAttribute("value"));
                switch (type) {
                    case DIGITS: {
                        parseDigits(node);
                        break;
                    }
                    case NUMBERS: {
                        parseNumbers(node);
                        break;
                    }
                    case DECADES: {
                        parseDecades(node);
                        break;
                    }
                    case HUNDREDS: {
                        parseHundreds(node);
                        break;
                    }
                    case WORDS: {
                        parseWords(node);
                        break;
                    }
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseWords(@Nonnull Node node) {
        Element element = ((Element) node);
        NodeList words = element.getElementsByTagName("word");
        for (int i = 0; i < words.getLength(); i++) {
            element = (Element) words.item(i);
            Word wordType = Enum.valueOf(Word.class, element.getAttribute("value"));
            String word = element.getAttribute("word");
            NodeList numbers = element.getElementsByTagName("number");

            for (int j = 0; j < numbers.getLength(); j++) {
                element = (Element) numbers.item(j);
                String values = element.getAttribute("value");
                String[] digits = values.split(",");
                EnumMap<Cases, String> cases = parseCases(element);
                for (String digit : digits) {
                    addedWordsToContainer(wordType, word, Integer.valueOf(digit), cases);
                }
            }
        }
    }

    private static void addedWordsToContainer(@Nonnull Word wordType, @Nonnull String word, int number, @Nonnull EnumMap<Cases, String> cases) {
        for (Cases caseType : cases.keySet()) {
            DeclineData.addWord(wordType, word, number, caseType, cases.get(caseType));
        }
    }

    private static void parseHundreds(@Nonnull Node node) {
        Element element = ((Element) node);
        NodeList hundreds = element.getElementsByTagName("hundred");
        Map<Integer, EnumMap<Cases, String>> numberCases = getCases(hundreds);
        for (Map.Entry<Integer, EnumMap<Cases, String>> entry : numberCases.entrySet()) {
            addedHundredToContainer(entry.getKey(), entry.getValue());
        }
    }

    private static void addedHundredToContainer(int hundred, @Nonnull EnumMap<Cases, String> cases) {
        for (Cases caseType : cases.keySet()) {
            String decline = cases.get(caseType);
            DeclineData.addHundred(hundred, caseType, decline);
        }
    }

    private static void parseDecades(@Nonnull Node node) {
        Element element = ((Element) node);
        NodeList decades = element.getElementsByTagName("decade");
        Map<Integer, EnumMap<Cases, String>> numberCases = getCases(decades);
        for (Map.Entry<Integer, EnumMap<Cases, String>> entry : numberCases.entrySet()) {
            addedDecadeToContainer(entry.getKey(), entry.getValue());
        }
    }

    private static void addedDecadeToContainer(int decade, @Nonnull EnumMap<Cases, String> cases) {
        for (Cases caseType : cases.keySet()) {
            String decline = cases.get(caseType);
            DeclineData.addDecade(decade, caseType, decline);
        }
    }

    private static void parseNumbers(@Nonnull Node node) {
        Element element = ((Element) node);
        NodeList numbers = element.getElementsByTagName("number");
        Map<Integer, EnumMap<Cases, String>> numberCases = getCases(numbers);
        for (Map.Entry<Integer, EnumMap<Cases, String>> entry : numberCases.entrySet()) {
            addedNumberToContainer(entry.getKey(), entry.getValue());
        }
    }

    private static Map<Integer, EnumMap<Cases, String>> getCases(NodeList numbers) {
        Map<Integer, EnumMap<Cases, String>> digitCases = new HashMap<>();
        Element element;
        for (int i = 0; i < numbers.getLength(); i++) {
            element = (Element) numbers.item(i);
            int number = Integer.valueOf(element.getAttribute("value"));
            EnumMap<Cases, String> cases = parseCases(element);
            digitCases.put(number, cases);
        }
        return digitCases;
    }

    private static void addedNumberToContainer(int number, @Nonnull EnumMap<Cases, String> cases) {
        for (Cases caseType : cases.keySet()) {
            String decline = cases.get(caseType);
            DeclineData.addNumber(number, caseType, decline);
        }
    }

    private static void parseDigits(@Nonnull Node node) {
        Element element = ((Element) node);
        NodeList digits = element.getElementsByTagName("digit");
        for (int i = 0; i < digits.getLength(); i++) {
            element = (Element) digits.item(i);
            int number = Integer.valueOf(element.getAttribute("value"));
            NodeList gender = element.getElementsByTagName("gender");
            if (gender.getLength() > 0) {
                EnumMap<Gender, EnumMap<Cases, String>> declineGender = parseGender(gender.item(0));
                addedDigitWithGenderToContainer(number, declineGender);
            } else {
                EnumMap<Cases, String> cases = parseCases(element);
                addedDigitToContainer(cases, number);
            }
        }
    }

    private static void addedDigitToContainer(@Nonnull EnumMap<Cases, String> cases, int number) {
        for (Cases caseType : cases.keySet()) {
            String decline = cases.get(caseType);
            DeclineData.addDigits(number, caseType, decline);
        }
    }

    private static void addedDigitWithGenderToContainer(int number, @Nonnull EnumMap<Gender, EnumMap<Cases, String>> declineGenders) {
        for (Gender gender : declineGenders.keySet()) {
            EnumMap<Cases, String> declines = declineGenders.get(gender);
            for (Cases caseType : declines.keySet()) {
                String decline = declines.get(caseType);
                DeclineData.addDigitWithGender(number, gender, caseType, decline);
            }
        }

    }

    private static EnumMap<Gender, EnumMap<Cases, String>> parseGender(@Nonnull Node node) {
        EnumMap<Gender, EnumMap<Cases, String>> declineGender = new EnumMap<>(Gender.class);
        Element element = (Element) node;
        Node male = element.getElementsByTagName("male").item(0);
        Node female = element.getElementsByTagName("female").item(0);
        Node neuter = element.getElementsByTagName("neuter").item(0);

        EnumMap<Cases, String> maleCases = parseCases((Element) male);
        EnumMap<Cases, String> femaleCases = parseCases((Element) female);
        EnumMap<Cases, String> neuterCases = parseCases((Element) neuter);

        declineGender.put(Gender.MALE, maleCases);
        declineGender.put(Gender.FEMALE, femaleCases);
        declineGender.put(Gender.NEUTER, neuterCases);
        return declineGender;
    }

    private static EnumMap<Cases, String> parseCases(@Nonnull Element node) {
        EnumMap<Cases, String> cases = new EnumMap<>(Cases.class);
        Node casesNode = node.getElementsByTagName("cases").item(0);
        for (Cases enumCase : Cases.values()) {
            NodeList caseElement = ((Element) casesNode).getElementsByTagName(enumCase.name());
            String caseWord = ((Element) caseElement.item(0)).getAttribute("value");
            cases.put(enumCase, caseWord);
        }
        return cases;
    }

    private enum Types {
        DIGITS,
        NUMBERS,
        DECADES,
        HUNDREDS,
        WORDS
    }
}
