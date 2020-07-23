package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.Intersection.getIntersectionInterval;


public class Main {

    private static final Map<String, Boolean> testBrackets = new HashMap<>();
    private static final List<Interval> intervals = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        // задание 4.1
        fillValidatorTestData();
        BracketsValidator validator = new BracketsValidator();
        testValidator(validator);

        System.out.println();

        // задание 4.2
        fillIntervalsTestData();
        showIntersections();
    }

    private static void fillIntervalsTestData() {
        intervals.add(new Interval("23.10.2017 10:00 Z", "23.10.2017 11:00 Z"));
        intervals.add(new Interval("", "23.10.2017 11:00 Z"));
        intervals.add(new Interval("23.10.2017 10:00 Z", null));
        intervals.add(new Interval("21.10.2017 10:30 Z", "25.10.2017 11:00 Z"));
        intervals.add(new Interval("23.10.2017 10:20 Z", "23.10.2017 11:00 Z"));
        intervals.add(new Interval("23.10.2019 10:20 Z", "23.10.2020 11:00 Z"));
        intervals.add(new Interval("23.10.2015 10:20 Z", "01.05.2018 15:00 Z"));
    }

    private static void fillValidatorTestData() {
        testBrackets.put("3", true);
        testBrackets.put("one", true);
        testBrackets.put("([])", true);
        testBrackets.put(" [({ })] ", true);
        testBrackets.put("([()[]()])()", true);
        testBrackets.put("a + (35 - b) * [test()] / {(3 + c)}", true);
        testBrackets.put("(", false);
        testBrackets.put(")(", false);
        testBrackets.put("[([)", false);
        testBrackets.put("([([))", false);
        testBrackets.put("(([()])))", false);
        testBrackets.put("(a+[b*c) - 17]", false);
    }

    static void testValidator(BracketsValidator validator) throws Exception {
        for (Map.Entry<String, Boolean> entry : testBrackets.entrySet()) {
            String elem = entry.getKey();
            Boolean expectedResult = entry.getValue();
            boolean correct = validator.validate(elem);
            System.out.println("Expression " + elem + " , expected result - " + expectedResult + ", validator " + correct);
            if (correct != expectedResult) {
                throw new Exception("Validator not working properly or you specified an incorrect expected result!");
            }
            validator.clearStack();
        }
        System.out.println("BracketsValidator tests passed!");
    }

    private static void showIntersections() {
        // все пары пересечений
        List<Intersection> intersections = Intersection.getIntersections(intervals);

        if (!intersections.isEmpty()) {
            intersections.forEach(inter ->
                    System.out.println(inter.toString() + ", intersection " + getIntersectionInterval(inter))
            );
        } else {
            System.out.println("No intersection!");
        }
    }
}
