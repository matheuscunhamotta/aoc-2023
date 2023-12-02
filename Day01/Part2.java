package Day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        List<Integer> calibrationValues = CalibrationValue.ofLines(lines);
        System.out.println(calibrationValues.stream().reduce(0, (a, b) -> a + b));
    }
}


final class CalibrationValue {
    /**
     * This `reverse` solution is not elegant but nice and easy. It avoids having to backtrack
     * because of substrings like `eightwo` and similars at the end of a string, as using the `find`
     * method of `Matcher` would match `eight` and miss the `two`.
     */
    public static List<Integer> ofLines(List<String> lines) {
        List<Integer> calibrationValues = new ArrayList<>(lines.size());

        // Pattern will be like: "(one|two|<...>|nine|\\d{1})".
        String normalExpression = CalibrationValue.getExpression(DigitMap.re());
        Pattern normalPattern = Pattern.compile(normalExpression);

        // Reversed pattern.
        String reverseExpression =
                CalibrationValue.getExpression(CalibrationValue.reverse(DigitMap.re()));
        Pattern reversePattern = Pattern.compile(reverseExpression);

        lines.forEach(line -> {
            Matcher normalMatcher = normalPattern.matcher(line);
            normalMatcher.find();
            String firstDigit = DigitMap.fromNameOrDigit(normalMatcher.group());

            Matcher reverseMatcher = reversePattern.matcher(CalibrationValue.reverse(line));
            reverseMatcher.find();
            String lastDigit =
                    DigitMap.fromNameOrDigit(CalibrationValue.reverse(reverseMatcher.group()));

            calibrationValues.add(Integer.parseInt(firstDigit + lastDigit));
        });
        return calibrationValues;
    }

    private static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    private static String getExpression(String input) {
        return "(" + input + "|\\d{1}" + ")";
    }
}


enum DigitMap {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);

    private final int digit;

    DigitMap(int digit) {
        this.digit = digit;
    }

    private static DigitMap fromName(String input) {
        for (DigitMap digitName : DigitMap.values()) {
            if (digitName.name().equalsIgnoreCase(input)) {
                return digitName;
            }
        }
        throw new IllegalArgumentException("Invalid argument: " + input);
    }

    public static String fromNameOrDigit(String nameOrDigit) {
        if (nameOrDigit.length() == 1) {
            return nameOrDigit;
        }
        return Integer.toString(fromName(nameOrDigit).digit);
    }

    /** Returns the string `one|two|<...>|nine`. */
    public static String re() {
        DigitMap[] digitNames = DigitMap.values();
        StringBuilder stringBuilder = new StringBuilder(digitNames.length);
        for (int i = 0; i < digitNames.length; i++) {
            stringBuilder.append(digitNames[i].toString().toLowerCase());
            if (i < digitNames.length - 1) {
                stringBuilder.append("|");
            }
        }
        return stringBuilder.toString();
    }
}
