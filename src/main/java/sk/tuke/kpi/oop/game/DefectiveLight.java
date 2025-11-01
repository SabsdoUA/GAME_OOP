package sk.tuke.kpi.oop.game;

import java.util.Random;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;

public class DefectiveLight extends Light {
    private static final float MIN_ON_DELAY = 0.3f;
    private static final float MAX_ON_DELAY = 1.2f;
    private static final float MIN_OFF_DELAY = 0.2f;
    private static final float MAX_OFF_DELAY = 0.8f;
    private static final Random RANDOM = new Random();

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new FlickerAction().scheduleFor(this);
    }

    private static final class FlickerAction extends AbstractAction<DefectiveLight> {
        private boolean initialized;
        private boolean isLightOn;
        private float remaining;

        @Override
        public void execute(float deltaTime) {
            DefectiveLight light = getActor();
            if (light == null || light.getScene() == null) {
                setDone(true);
                return;
            }

            if (!initialized) {
                remaining = randomDelay(false);
                initialized = true;
            }

            remaining -= deltaTime;
            while (remaining <= 0f) {
                light.toggle();
                isLightOn = !isLightOn;
                remaining += randomDelay(isLightOn);
            }
        }

        private float randomDelay(boolean currentlyOn) {
            float min = currentlyOn ? MIN_OFF_DELAY : MIN_ON_DELAY;
            float max = currentlyOn ? MAX_OFF_DELAY : MAX_ON_DELAY;
            float delay = min + RANDOM.nextFloat() * (max - min);
            if (delay <= 0f) {
                return 0.1f;
            }
            return delay;
        }
    }
}
