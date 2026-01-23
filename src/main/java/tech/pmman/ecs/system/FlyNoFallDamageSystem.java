package tech.pmman.ecs.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.ecs.compoent.FlyComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlyNoFallDamageSystem extends DamageEventSystem {
    @Override
    public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull Damage damage) {
        DamageCause damageCause = DamageCause.getAssetMap()
                                             .getAsset(damage.getDamageCauseIndex());
        if (damageCause != null && "Fall".equals(damageCause.getId())) {
            damage.setCancelled(true);
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return FlyComponent.getComponentType();
    }

    @Nullable
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get()
                           .getFilterDamageGroup();
    }
}
