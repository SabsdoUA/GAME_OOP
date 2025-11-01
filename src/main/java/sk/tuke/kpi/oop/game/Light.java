package sk.tuke.kpi.oop.game;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor {
    private boolean isOn;
    private boolean electricityFlow;

    public Light() { updateAnimation(); }

    public void toggle() {
        isOn = !isOn;
        updateAnimation();
    }

    public void setElectricityFlow(boolean flow) {
        electricityFlow = flow;
        updateAnimation();
    }

    private void updateAnimation() {
        setAnimation(new Animation(isOn && electricityFlow ? "sprites/light_on.png" : "sprites/light_off.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG));
    }
}
