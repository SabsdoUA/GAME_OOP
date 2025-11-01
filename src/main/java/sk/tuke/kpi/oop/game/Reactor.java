package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.tools.FireExtinguisher;
import sk.tuke.kpi.oop.game.tools.Hammer;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

public class Reactor extends AbstractActor {
    private int temperature;
    private int damage;
    private Light light;
    private boolean isRunning;
    private static final String SPRITE_NORMAL = "sprites/reactor_on.png";
    private static final String SPRITE_HOT = "sprites/reactor_hot.png";
    private static final String SPRITE_BROKEN = "sprites/reactor_broken.png";
    private static final String SPRITE_OFF = "sprites/reactor.png";
    private static final float BASE_DURATION_NORMAL = 0.1f;
    private static final float BASE_DURATION_HOT = 0.05f;
    private static final float BASE_DURATION_BROKEN = 0.1f;

    public Reactor() {
        this.isRunning = true;
        updateAnimation();
    }

    public int getDamage() { return damage; }
    public int getTemperature() { return temperature; }
    public boolean isRunning() { return isRunning; }

    public void increaseTemperature(int increment) {
        if (damage >= 100 || increment <= 0 || !isRunning) return;
        temperature += (int) Math.ceil(increment * (damage <= 33 ? 1.0 : damage <= 66 ? 1.5 : 2.0));
        if (temperature < 0) temperature = 0;
        if (temperature > 2000) damage = Math.min(100, Math.max(damage, (temperature - 2000) / 40));
        if (temperature >= 6000) damage = 100;
        updateAnimation();
        updateElectricityFlow();
    }

    public void decreaseTemperature(int decrement) {
        if (damage >= 100 || decrement <= 0 || !isRunning) return;
        temperature = Math.max(0, temperature - (damage >= 50 ? decrement / 2 : decrement));
        updateAnimation();
        updateElectricityFlow();
    }

    public void turnOn() {
        if (damage < 100) isRunning = true;
        updateAnimation();
        updateElectricityFlow();
    }

    public void turnOff() {
        isRunning = false;
        updateAnimation();
        updateElectricityFlow();
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new PerpetualReactorHeating(1).scheduleFor(this);
    }

    private void updateAnimation() {
        final String sprite = damage >= 100 ? SPRITE_BROKEN : temperature > 4000 && isRunning ? SPRITE_HOT : isRunning ? SPRITE_NORMAL : SPRITE_OFF;
        final float duration = damage >= 100 ? BASE_DURATION_BROKEN : temperature > 4000 && isRunning || isRunning ? Math.max(0.02f, (temperature > 4000 ? BASE_DURATION_HOT : BASE_DURATION_NORMAL) * (1.0f - damage / 100.0f)) : 0.1f;
        if (damage >= 100) isRunning = false;
        setAnimation(new Animation(sprite, 80, 80, duration, Animation.PlayMode.LOOP_PINGPONG));
    }

    private void updateElectricityFlow() {
        if (light != null) light.setElectricityFlow(isRunning && damage < 100);
    }

    public void addLight(Light light) {
        if (this.light != null) return;
        this.light = light;
        if (light != null) light.setElectricityFlow(isRunning && damage < 100);
    }

    public void removeLight(Light light) {
        if (this.light == light) {
            if (light != null) light.setElectricityFlow(false);
            this.light = null;
        }
    }

    public void repairWith(Hammer hammer) {
        if (hammer == null || damage <= 0 || damage >= 100) return;
        final int oldDamage = damage, candidateTemp = (damage - 50) * 40 + 2000;
        damage = Math.max(0, damage - 50);
        if (damage < oldDamage) {
            if (candidateTemp < temperature) temperature = Math.max(0, candidateTemp);
            hammer.use();
        }
        updateAnimation();
        updateElectricityFlow();
    }

    public void extinguishWith(FireExtinguisher extinguisher) {
        if (extinguisher != null && extinguisher.getRemainingUses() > 0) {
            extinguisher.use();
            temperature -= 4000;
            setAnimation(new Animation("sprites/reactor_extinguished.png", 80, 80, 0.1f));
            updateElectricityFlow();
        }
    }
}
