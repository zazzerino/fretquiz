package kdp.fretquiz.game.model;

import kdp.fretquiz.theory.FretCoord;
import org.springframework.data.relational.core.mapping.Embedded;

public record Guess(Long playerId,
                    @Embedded.Nullable(prefix = "clicked_") FretCoord clickedCoord,
                    @Embedded.Nullable(prefix = "correct_") FretCoord correctCoord,
                    boolean isCorrect) {
}
