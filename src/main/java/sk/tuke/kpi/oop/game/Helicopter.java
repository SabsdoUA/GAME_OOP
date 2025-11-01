package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Helicopter extends AbstractActor {
    private static final String SPRITE_PATH = "sprites/heli.png";
    private static final int FRAME_WIDTH = 64;
    private static final int FRAME_HEIGHT = 64;
    private static final float FRAME_DURATION = 0.1f;
    private static final Animation.PlayMode PLAY_MODE = Animation.PlayMode.LOOP_PINGPONG;

    private Disposable pursuitAction;

    public Helicopter() {
        setAnimation(new Animation(SPRITE_PATH, FRAME_WIDTH, FRAME_HEIGHT, FRAME_DURATION, PLAY_MODE));
    }

    public void searchAndDestroy() {
        if (pursuitAction != null) {
            return;
        }

        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        Player player = scene.getFirstActorByType(Player.class);
        if (player == null) {
            return;
        }

        pursuitAction = new Loop<>(new Invoke<>(new Pursuit(player))).scheduleFor(this);
    }

    private void stopPursuit() {
        if (pursuitAction == null) {
            return;
        }
        pursuitAction.dispose();
        pursuitAction = null;
    }

    private final class Pursuit implements Runnable {
        private final Player target;

        private Pursuit(Player target) {
            this.target = target;
        }

        @Override
        public void run() {
            Scene scene = getScene();
            if (scene == null || target.getScene() != scene) {
                stopPursuit();
                return;
            }

            int nextX = nextCoordinate(getPosX(), getWidth(), target.getPosX(), target.getWidth());
            int nextY = nextCoordinate(getPosY(), getHeight(), target.getPosY(), target.getHeight());
            setPosition(nextX, nextY);

            if (intersects(target)) {
                int remainingEnergy = Math.max(0, target.getEnergy() - 1);
                target.setEnergy(remainingEnergy);
            }
        }

        private int nextCoordinate(int sourcePos, int sourceSize, int targetPos, int targetSize) {
            int sourceCenter = sourcePos + sourceSize / 2;
            int targetCenter = targetPos + targetSize / 2;

            if (sourceCenter > targetCenter) {
                return sourcePos - 1;
            }
            if (sourceCenter < targetCenter) {
                return sourcePos + 1;
            }
            return sourcePos;
        }
    }
}
