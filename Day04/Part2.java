package Day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Part2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        List<Card> cards = Card.ofLines(lines);
        List<Deck> decks = new ArrayList<>();
        for (Card card : cards) {
            decks.add(new Deck(card));
        }
        int count = 0;
        for (Deck deck : decks) {
            Card card = deck.getCard();
            for (int j = 0; j < deck.getCount(); j++) {
                for (int i = 0; i < deck.getMatchingNumbersCount(); i++) {
                    decks.get(card.cardId() + i).addCopy();
                }
            }
            count += deck.getCount();
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
}


/** A deck to store copies of the same card. */
final class Deck {
    /** The card that this deck holds copies of. */
    private Card card;

    /** The number of copies of the same card in this deck. */
    private int count = 1;

    /** A cache for the intersection count. Crucial for performance. */
    private final int matchingNumbersCount;

    public Deck(Card card) {
        this.card = card;
        matchingNumbersCount = card.intersection().size();
    }

    public Card getCard() {
        return card;
    }

    public int getCount() {
        return count;
    }

    public int getMatchingNumbersCount() {
        return matchingNumbersCount;
    }

    public void addCopy() {
        count++;
    }
}
