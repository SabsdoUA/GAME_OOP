package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Hammer extends BreakableTool {
    public Hammer() { this(1); }
    protected Hammer(int uses) {
        super(uses);
        setAnimation(new Animation("sprites/hammer.png"));
    }
    public int getUses() { return getRemainingUses(); }
}
