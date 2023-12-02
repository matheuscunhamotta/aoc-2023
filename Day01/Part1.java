package Day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        List<Integer> calibrationValues = CalibrationValue.ofLines(lines);
        System.out.println(calibrationValues.stream().reduce(0, (a, b) -> a + b));
    }
}


final class CalibrationValue {
    public static List<Integer> ofLines(List<String> lines) {
        List<Integer> calibrationValues = new ArrayList<>(lines.size());
        Pattern pattern = Pattern.compile("\\d{1}");
        lines.forEach(line -> {
            StringBuilder lineBuilder = new StringBuilder(line);
            Matcher matcher = pattern.matcher(lineBuilder);

            // Get the first digit, reverse the sb then get the last digit.
            matcher.find();
            String firstDigit = matcher.group();
            lineBuilder.reverse();
            matcher.reset();
            matcher.find();
            String lastDigit = matcher.group();
            calibrationValues.add(Integer.parseInt(firstDigit + lastDigit));
        });
        return calibrationValues;
    }
}
