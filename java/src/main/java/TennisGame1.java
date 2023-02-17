import java.util.Arrays;

public class TennisGame1 implements TennisGame {

    class Player {
        int points = 0;
        final String name;

        Player(String name) {
            this.name = name;
        }

        public void gainPoint() {
            points++;
        }

        public boolean hasScoreBiggerThan(Player other) {
            return points > other.points;
        }

        public boolean hasScoreBiggerThan(Score score) {
            return points > score.value;
        }

        public String name() {
            return name;
        }

        public int pointsDifference(Player other) {
            return points - other.points;
        }

        public String score() {
            return Score.from(points).label;
        }

        public boolean is(String name) {
            return this.name.equals(name);
        }
    }

    private final Player server;
    private final Player receiver;

    public TennisGame1(String server, String receiver) {
        this.server = new Player(server);
        this.receiver = new Player(receiver);
    }

    public void wonPoint(String playerName) {
        if(server.is(playerName)) {
            server.gainPoint();
        } else {
            receiver.gainPoint();
        }
    }

    public String getScore() {
        if (gameContinues()) {
            return getGameScore();
        }
        return "Win for " + getLeadingPlayer().name();
    }

    private String getGameScore() {
        if (isScoreEqual()) {
            return getEqualScore();
        }
        if (isAdvantage()) {
            return "Advantage " + getLeadingPlayer().name();
        }
        return getSimpleScore();
    }

    private boolean isScoreEqual() {
        return server.pointsDifference(receiver) == 0;
    }

    private boolean isAdvantage() {
        return getLeadingPlayer().hasScoreBiggerThan(Score.FORTY) && !isScoreEqual();
    }

    private String getSimpleScore() {
        return String.format("%s-%s", server.score(), receiver.score());
    }

    enum Score {
        LOVE(0, "Love"), FIFTEEN(1, "Fifteen"), THIRTY(2, "Thirty"), FORTY(3, "Forty");

        private final int value;
        private final String label;

        Score(int value, String label) {
            this.value = value;
            this.label = label;
        }

        static Score from(int value) {
            return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no such element: " + value));
        }

    }

    private Player getLeadingPlayer() {
        if (server.hasScoreBiggerThan(receiver)) {
            return server;
        }
        return receiver;
    }

    private boolean isGameFinished() {
        return getLeadingPlayer().hasScoreBiggerThan(Score.FORTY)
            && Math.abs(server.pointsDifference(receiver)) >= 2;
    }

    private boolean gameContinues() {
        return !isGameFinished();
    }

    private String getEqualScore() {
        if (server.hasScoreBiggerThan(Score.THIRTY)) {
            return "Deuce";
        }
        return String.format("%s-All", server.score());
    }

}
