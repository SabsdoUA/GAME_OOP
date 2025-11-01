package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable {
    private boolean isOn;
    private boolean electricityFlow;

    public Light() {
        updateAnimation();
    }

    public void toggle() {
        if (isOn) {
            turnOff();
        } else {
            turnOn();
        }
    }

    @Override
    public void turnOn() {
        if (isOn) {
            return;
        }
        isOn = true;
        updateAnimation();
    }

    @Override
    public void turnOff() {
        if (!isOn) {
            return;
        }
        isOn = false;
        updateAnimation();
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    public void setPowered(boolean powered) {
        if (powered) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public void setElectricityFlow(boolean flow) {
        electricityFlow = flow;
        updateAnimation();
    }

    private void updateAnimation() {
        setAnimation(new Animation(isOn && electricityFlow ? "sprites/light_on.png" : "sprites/light_off.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG));
    }
}
