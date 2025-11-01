package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.framework.AbstractActor;

public abstract class BreakableTool extends AbstractActor {
    private int remainingUses;

    protected BreakableTool(int initialUses) { remainingUses = Math.max(0, initialUses); }
    public int getRemainingUses() { return remainingUses; }
    public void use() {
        if (remainingUses <= 0) return;
        if (--remainingUses == 0 && getScene() != null) getScene().removeActor(this);
    }
}
