package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.tools.Hammer;
import sk.tuke.kpi.oop.game.tools.FireExtinguisher;

public class Gameplay extends Scenario {
    @Override
    public void setupPlay(Scene scene) {
        runMaintenanceShowcase(scene);
    }

    private void runMaintenanceShowcase(Scene scene) {
        Reactor reactor = placeReactor(scene, 64, 64);
        DefectiveLight light = installWarningLight(scene, reactor, 64, 96);
        SmartCooler smartCooler = installSmartCooler(scene, reactor, 96, 64);
        PowerSwitch reactorSwitch = installPowerSwitch(scene, reactor, 32, 64);
        Cooler auxiliaryCooler = installCooler(scene, reactor, 160, 64);
        PowerSwitch coolerSwitch = installPowerSwitch(scene, auxiliaryCooler, 160, 32);
        Light ceilingLight = installLight(scene, 32, 192);
        ceilingLight.setElectricityFlow(true);
        PowerSwitch lightSwitch = installPowerSwitch(scene, ceilingLight, 32, 224);
        Computer computer = installComputer(scene, 64, 128);
        Hammer hammer = placeHammer(scene, 160, 64);
        FireExtinguisher extinguisher = placeExtinguisher(scene, 192, 64);

        scheduleTemperatureEvents(reactor);
        scheduleElectricNetwork(reactor, light, computer);
        scheduleMaintenanceActions(reactor, smartCooler, reactorSwitch, hammer, extinguisher, computer);
        scheduleSwitchCycle(coolerSwitch, 2f, 3f, 2f);
        scheduleSwitchCycle(lightSwitch, 1f, 1.5f, 1.5f);
    }

    private Reactor placeReactor(Scene scene, int x, int y) {
        Reactor reactor = new Reactor();
        reactor.turnOn();
        scene.addActor(reactor, x, y);
        return reactor;
    }

    private DefectiveLight installWarningLight(Scene scene, Reactor reactor, int x, int y) {
        DefectiveLight light = new DefectiveLight();
        scene.addActor(light, x, y);
        reactor.addLight(light);
        return light;
    }

    private SmartCooler installSmartCooler(Scene scene, Reactor reactor, int x, int y) {
        SmartCooler smartCooler = new SmartCooler(reactor);
        scene.addActor(smartCooler, x, y);
        return smartCooler;
    }

    private PowerSwitch installPowerSwitch(Scene scene, Switchable device, int x, int y) {
        PowerSwitch powerSwitch = new PowerSwitch(device);
        scene.addActor(powerSwitch, x, y);
        return powerSwitch;
    }

    private Cooler installCooler(Scene scene, Reactor reactor, int x, int y) {
        Cooler cooler = new Cooler(reactor);
        scene.addActor(cooler, x, y);
        return cooler;
    }

    private Light installLight(Scene scene, int x, int y) {
        Light light = new Light();
        scene.addActor(light, x, y);
        return light;
    }

    private Computer installComputer(Scene scene, int x, int y) {
        Computer computer = new Computer();
        scene.addActor(computer, x, y);
        return computer;
    }

    private Hammer placeHammer(Scene scene, int x, int y) {
        Hammer hammer = new Hammer();
        scene.addActor(hammer, x, y);
        return hammer;
    }

    private FireExtinguisher placeExtinguisher(Scene scene, int x, int y) {
        FireExtinguisher extinguisher = new FireExtinguisher();
        scene.addActor(extinguisher, x, y);
        return extinguisher;
    }

    private void scheduleTemperatureEvents(Reactor reactor) {
        new ActionSequence<>(
            new Wait<>(1f),
            new Invoke<>(() -> reactor.increaseTemperature(2500)),
            new Wait<>(2f),
            new Invoke<>(() -> reactor.increaseTemperature(3000))
        ).scheduleFor(reactor);
    }

    private void scheduleElectricNetwork(Reactor reactor, DefectiveLight light, Computer computer) {
        if (computer == null) {
            return;
        }
        if (light != null) {
            new When<>(
                () -> reactor.getDamage() >= 100,
                new Invoke<>(() -> reactor.removeLight(light))
            ).scheduleFor(reactor);
        }
        new Loop<>(new ActionSequence<>(
            new Invoke<>(() -> computer.setPowered(reactor.isRunning() && reactor.getDamage() < 100)),
            new Wait<>(0.2f)
        )).scheduleFor(computer);
    }

    private void scheduleMaintenanceActions(
        Reactor reactor,
        SmartCooler smartCooler,
        PowerSwitch reactorSwitch,
        Hammer hammer,
        FireExtinguisher extinguisher,
        Computer computer
    ) {
        if (extinguisher != null) {
            new When<>(
                () -> reactor.getTemperature() >= 4500 && extinguisher.getRemainingUses() > 0,
                new Invoke<>(() -> reactor.extinguishWith(extinguisher))
            ).scheduleFor(reactor);
        }

        if (hammer != null) {
            new When<>(
                () -> reactor.getDamage() >= 50 && hammer.getUses() > 0,
                new Invoke<>(() -> reactor.repairWith(hammer))
            ).scheduleFor(reactor);
        }

        if (reactorSwitch != null) {
            new ActionSequence<>(
                new Wait<>(8f),
                new Invoke<>(reactorSwitch::switchOff),
                new Wait<>(5f),
                new Invoke<>(reactorSwitch::switchOn)
            ).scheduleFor(reactorSwitch);
        }

        if (smartCooler != null) {
            new Loop<>(new ActionSequence<>(
                new Invoke<>(() -> {
                    if (reactor.getDamage() >= 100) {
                        smartCooler.turnOff();
                    }
                }),
                new Wait<>(0.5f)
            )).scheduleFor(smartCooler);
        }

        if (computer != null) {
            new When<>(
                () -> reactor.getDamage() >= 90,
                new Invoke<>(() -> computer.setPowered(false))
            ).scheduleFor(reactor);
        }
    }

    private void scheduleSwitchCycle(PowerSwitch powerSwitch, float initialDelay, float onDuration, float offDuration) {
        if (powerSwitch == null) {
            return;
        }
        new ActionSequence<>(
            new Wait<>(initialDelay),
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(powerSwitch::switchOn),
                    new Wait<>(onDuration),
                    new Invoke<>(powerSwitch::switchOff),
                    new Wait<>(offDuration)
                )
            )
        ).scheduleFor(powerSwitch);
    }
}
