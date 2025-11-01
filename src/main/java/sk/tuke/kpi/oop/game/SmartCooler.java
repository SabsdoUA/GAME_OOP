package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler {
    private static final int MIN_OPERATION_TEMPERATURE = 1500;
    private static final int MAX_OPERATION_TEMPERATURE = 2500;
    private static final float CHECK_INTERVAL = 0.1f;

    public SmartCooler() {
        super();
    }

    public SmartCooler(Reactor reactor) {
        this();
        setReactor(reactor);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new ActionSequence<>(new Invoke<>(this::updateCoolingState), new Wait<>(CHECK_INTERVAL))).scheduleFor(this);
        synchronizeWithReactor();
    }

    @Override
    public void setReactor(Reactor reactor) {
        if (reactor == getReactor()) {
            return;
        }
        super.setReactor(reactor);
        synchronizeWithReactor();
    }

    private void synchronizeWithReactor() {
        Reactor reactor = getReactor();
        if (reactor == null) {
            if (isOn()) {
                turnOff();
            }
            return;
        }
        updateCoolingState();
    }

    private void updateCoolingState() {
        Reactor reactor = getReactor();
        if (reactor == null) {
            if (isOn()) {
                turnOff();
            }
            return;
        }

        int temperature = reactor.getTemperature();
        if (temperature > MAX_OPERATION_TEMPERATURE && !isOn()) {
            turnOn();
        } else if (temperature < MIN_OPERATION_TEMPERATURE && isOn()) {
            turnOff();
        }
    }
}
