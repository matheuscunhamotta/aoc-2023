package Day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a very loose solution, adequate for a very easy problem. But it still triggers a desire
 * to improve it, for instance to stop hard-coding every color and use loops instead, with a 3-uple
 * or something similar to store the colors, and use a better data structure, etc. Not much to learn
 * doing it though.
 */
public class Part2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        List<Game> games = Game.ofLines(lines);
        int count = 0;
        for (Game game : games) {
            count += game.power();
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

    public int power() {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Sample sample : gameSample) {
            if (red < sample.r())
                red = sample.r();
            if (green < sample.g())
                green = sample.g();
            if (blue < sample.b())
                blue = sample.b();
        }
        return red * green * blue;
    }
}


/**
 * The `sampleId` is just to avoid hash collisions when there are multiple samples in a game with
 * the same outcome, if any.
 */
record Sample(int sampleId, int r, int g, int b) {
}
