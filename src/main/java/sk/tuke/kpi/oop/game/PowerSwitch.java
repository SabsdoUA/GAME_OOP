package sk.tuke.kpi.oop.game;

import java.awt.Color;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class PowerSwitch extends AbstractActor {
    private final Switchable device;

    public PowerSwitch(Switchable device) {
        this.device = device;
        setAnimation(new Animation("sprites/switch.png"));
        refreshTint();
    }

    public Switchable getDevice() {
        return device;
    }

    public void switchOn() {
        if (device != null) {
            device.turnOn();
        }
        refreshTint();
    }

    public void switchOff() {
        if (device != null) {
            device.turnOff();
        }
        refreshTint();
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new ActionSequence<>(new Wait<>(0.2f), new Invoke<>(this::refreshTint))).scheduleFor(this);
    }

    private void refreshTint() {
        Animation animation = getAnimation();
        if (animation != null) {
            animation.setTint(device != null && device.isOn() ? Color.WHITE : Color.GRAY);
        }
    }
}
