package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable {
    private Reactor reactor;
    private boolean isOn;
    private static final String FAN_SPRITE = "sprites/fan.png";
    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final float FRAME_DURATION = 0.1f;
    private static final float COOLING_INTERVAL = 1.0f;
    private static final int COOLING_AMOUNT = 1;

    public Cooler() {
        this(null);
    }

    public Cooler(Reactor reactor) {
        this.reactor = reactor;
        final Animation fanAnimation = new Animation(FAN_SPRITE, FRAME_WIDTH, FRAME_HEIGHT, FRAME_DURATION, Animation.PlayMode.LOOP_PINGPONG);
        fanAnimation.stop();
        setAnimation(fanAnimation);
    }

    public Reactor getReactor() {
        return reactor;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
        if (isOn) {
            coolReactor();
        }
    }

    @Override
    public void turnOn() {
        if (isOn) return;
        isOn = true;
        if (getAnimation() != null) {
            getAnimation().play();
        }
        coolReactor();
    }

    @Override
    public void turnOff() {
        if (!isOn) return;
        isOn = false;
        if (getAnimation() != null) {
            getAnimation().stop();
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new ActionSequence<>(new Wait<>(COOLING_INTERVAL), new Invoke<>(this::coolReactor))).scheduleFor(this);
    }

    private void coolReactor() {
        if (!isOn || reactor == null) return;
        reactor.decreaseTemperature(COOLING_AMOUNT);
    }
}
