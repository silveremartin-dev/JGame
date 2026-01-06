package org.jgame.parts.players;

import org.jetbrains.annotations.NotNull;
import org.jgame.model.GameUser;

public class GamePlayer extends AbstractPlayer {

    private final GameUser user;

    public GamePlayer(@NotNull GameUser user) {
        this.user = user;
        setId(user.getLogin());
    }

    public GameUser getUser() {
        return user;
    }

    @Override
    public java.util.List<org.jgame.logic.ActionInterface> computeNextActions(org.jgame.logic.Gameplay gameplay) {
        // As a generic player, this logic depends on the specific game implementation
        // For now, return empty list or use AI logic if Type is ARTIFICIAL
        return new java.util.ArrayList<>();
    }
}
