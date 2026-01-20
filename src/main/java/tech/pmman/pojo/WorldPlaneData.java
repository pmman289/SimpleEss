package tech.pmman.pojo;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@Data
public class WorldPlaneData {
    private WorldPlanePoint start;
    private WorldPlanePoint end;

    public WorldPlaneData(WorldPlanePoint start, WorldPlanePoint end) {
        this.start = start;
        this.end = end;
    }

    public WorldPlaneData() {
    }

    public WorldPlanePoint getRandomPointInSpace(){
        if (start == null || end == null) {
            return new WorldPlanePoint(0, 0);
        }

        // 确定 X 的范围
        int minX = Math.min(start.getX(), end.getX());
        int maxX = Math.max(start.getX(), end.getX());

        // 确定 Z 的范围
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxZ = Math.max(start.getZ(), end.getZ());

        ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();
        int randomX = random.nextInt(minX, maxX + 1);
        int randomZ = random.nextInt(minZ, maxZ + 1);
        return new WorldPlanePoint(randomX, randomZ);
    }

    public static final BuilderCodec<WorldPlaneData> CODEC =
            BuilderCodec.builder(WorldPlaneData.class, WorldPlaneData::new)
                        .append(
                                new KeyedCodec<>("Start", WorldPlanePoint.CODEC),
                                (o, d) -> o.start = d,
                                o -> o.start
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("End", WorldPlanePoint.CODEC),
                                (o, d) -> o.end = d,
                                o -> o.end
                        )
                        .add()
                        .build();

    @Data
    public static class WorldPlanePoint {
        private int x;
        private int z;

        public WorldPlanePoint(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public WorldPlanePoint() {
        }

        public static final BuilderCodec<WorldPlanePoint> CODEC =
                BuilderCodec.builder(WorldPlanePoint.class, WorldPlanePoint::new)
                            .append(
                                    new KeyedCodec<>("X", BuilderCodec.INTEGER),
                                    (o, d) -> o.x = d,
                                    o -> o.x
                            )
                            .add()
                            .append(
                                    new KeyedCodec<>("Y", BuilderCodec.INTEGER),
                                    (o, d) -> o.z = d,
                                    o -> o.z
                            )
                            .add()
                            .build();
    }
}
