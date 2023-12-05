package Day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Part1 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        List<Card> cards = Card.ofLines(lines);
        int count = 0;
        for (Card card : cards) {
            count += card.points();
        }
        System.out.println(count);
    }
}


record Card(int cardId, Set<String> winningNumbers, Set<String> drawnNumbers) {
    public static List<Card> ofLines(List<String> lines) {
        List<Card> cards = new ArrayList<>();
        lines.forEach(line -> {
            String[] splitIdAndNumbers = line.split(":\\s+");
            int cardId = Integer.parseInt(splitIdAndNumbers[0].split("Card\\s+")[1]);
            String[] splitNumbers = splitIdAndNumbers[1].split("\\s+\\|\\s+");
            Set<String> winners = new HashSet<>(Arrays.asList(splitNumbers[0].split("\\s+")));
            Set<String> sample = new HashSet<>(Arrays.asList(splitNumbers[1].split("\\s+")));
            cards.add(new Card(cardId, winners, sample));
        });
        return cards;
    }

    public Set<String> intersection() {
        Set<String> intersection = new HashSet<>();
        for (String s : drawnNumbers) {
            if (winningNumbers.contains(s)) {
                intersection.add(s);
            }
        }
        return intersection;
    }

    public int points() {
        int intersectionSize = intersection().size();
        return intersectionSize < 2 ? intersectionSize : (int) Math.pow(2, intersectionSize - 1);
    }
}
