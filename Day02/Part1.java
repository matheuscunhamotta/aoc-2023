package Day02;

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
        List<Game> games = Game.ofLines(lines);
        int count = 0;
        for (Game game : games) {
            if (game.isPossible(12, 13, 14)) {
                count += game.gameId();
            }
        }
        System.out.println(count);
    }
}


record Game(int gameId, List<Sample> gameSample) {
    public static List<Game> ofLines(List<String> lines) {
        List<Game> games = new ArrayList<>();
        // Compile a regex pattern for later.
        Pattern pattern = Pattern.compile("(\\d+) (red|green|blue)");
        lines.forEach(line -> {
            // Split each line into the game id part and the game samples part.
            String[] idAndGames = line.split(": ");
            // Get the game id.
            int gameId = Integer.parseInt(idAndGames[0].split("Game ")[1]);

            // Parse the game samples part.
            List<Sample> samples = new ArrayList<>();
            String[] stringSamples = idAndGames[1].split("; ");
            for (int sampleId = 0; sampleId < stringSamples.length; sampleId++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                Matcher matcher = pattern.matcher(stringSamples[sampleId]);
                while (matcher.find()) {
                    switch (matcher.group(2)) {
                        case "red":
                            red = Integer.parseInt(matcher.group(1));
                            break;
                        case "green":
                            green = Integer.parseInt(matcher.group(1));
                            break;
                        case "blue":
                            blue = Integer.parseInt(matcher.group(1));
                            break;
                        default:
                            throw new Error("Unknown color");
                    }
                }
                samples.add(new Sample(sampleId, red, green, blue));
            }
            games.add(new Game(gameId, samples));
        });
        return games;
    }

    public boolean isPossible(int r, int g, int b) {
        for (Sample sample : gameSample) {
            if (!sample.isPossible(r, g, b)) {
                return false;
            }
        }
        return true;
    }
}


/**
 * The `sampleId` is just to avoid hash collisions when there are multiple samples in a game with
 * the same outcome.
 */
record Sample(int sampleId, int r, int g, int b) {
    public boolean isPossible(int r, int g, int b) {
        if (r < this.r)
            return false;
        if (g < this.g)
            return false;
        if (b < this.b)
            return false;
        return true;
    }
}
