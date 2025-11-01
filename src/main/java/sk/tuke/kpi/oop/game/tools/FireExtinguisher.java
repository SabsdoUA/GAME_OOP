package sk.tuke.kpi.oop.game.tools;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class FireExtinguisher extends BreakableTool {
    public FireExtinguisher() {
        super(1);
        setAnimation(new Animation("sprites/extinguisher.png", 16, 16));
    }
}
