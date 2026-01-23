package tech.pmman.ecs.compoent;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class FlyComponent implements Component<EntityStore> {
    public static ComponentType<EntityStore, FlyComponent> COMPONENT_TYPE;

    public FlyComponent() {
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new FlyComponent();
    }

    public static ComponentType<EntityStore, FlyComponent> getComponentType() {
        return COMPONENT_TYPE;
    }
}
