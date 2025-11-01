package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Reactor;

public class PerpetualReactorHeating extends AbstractAction<Reactor> {
    private final int increaseAmount;
    private float elapsedTime;

    public PerpetualReactorHeating(int increaseAmount) { this.increaseAmount = increaseAmount; }

    @Override
    public void execute(float deltaTime) {
        Reactor reactor = getActor();
        if (reactor == null || reactor.getScene() == null) {
            setDone(true);
            return;
        }
        elapsedTime += deltaTime;
        while (elapsedTime >= 1f) {
            reactor.increaseTemperature(increaseAmount);
            elapsedTime -= 1f;
        }
    }
}
