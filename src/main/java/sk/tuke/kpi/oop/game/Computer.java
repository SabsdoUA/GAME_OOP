package sk.tuke.kpi.oop.game;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor {
    private final Animation poweredAnimation;
    private final Animation unpoweredAnimation;
    private boolean powered;

    public Computer() {
        poweredAnimation = new Animation("sprites/computer.png", 80, 48, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        unpoweredAnimation = new Animation("sprites/computer.png", 80, 48, 0.2f);
        setPowered(false);
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        setAnimation(powered ? poweredAnimation : unpoweredAnimation);
    }

    public boolean isPowered() {
        return powered;
    }

    public int add(int a, int b) {
        return powered ? a + b : 0;
    }

    public float add(float a, float b) {
        return powered ? a + b : 0f;
    }

    public int sub(int a, int b) {
        return powered ? a - b : 0;
    }

    public float sub(float a, float b) {
        return powered ? a - b : 0f;
    }
}
