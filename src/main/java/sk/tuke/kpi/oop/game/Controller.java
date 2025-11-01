package sk.tuke.kpi.oop.game;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Controller extends AbstractActor {
    private final Reactor reactor;

    public Controller(Reactor reactor) {
        this.reactor = reactor;
        setAnimation(new Animation("sprites/switch.png"));
    }

    public void toggle() {
        if (reactor == null || reactor.getDamage() >= 100) return;
        if (reactor.isRunning()) reactor.turnOff();
        else reactor.turnOn();
    }

    public Reactor getReactor() { return reactor; }
}
