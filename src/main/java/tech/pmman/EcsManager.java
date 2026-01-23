package tech.pmman;

import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.ecs.compoent.FlyComponent;
import tech.pmman.ecs.system.FlyNoFallDamageSystem;

public class EcsManager {
    public static void setup(ComponentRegistryProxy<EntityStore> registry) {
        registerComponent(registry);
        registerSystem(registry);
    }

    private static void registerComponent(ComponentRegistryProxy<EntityStore> registry) {
        FlyComponent.COMPONENT_TYPE = registry.registerComponent(FlyComponent.class, FlyComponent::new);
    }

    private static void registerSystem(ComponentRegistryProxy<EntityStore> registry) {
        registry.registerSystem(new FlyNoFallDamageSystem());
    }
}
