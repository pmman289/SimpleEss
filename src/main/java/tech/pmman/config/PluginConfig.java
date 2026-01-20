package tech.pmman.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Data;
import tech.pmman.pojo.WorldPlaneData;

@Data
public class PluginConfig {
    private String pluginPrefix = "[SimpleEss]";
    private String welcomeText = "";
    private String joinBroadcast = "";

    private int teleportCooldown = 120;

    private int maxHome = 3;

    private boolean autoAddNewPlayerToDefault = true;
    private boolean enablePublicCommandControl = false;
    private String[] publicCommand = new String[]{
            "back",
            "backdeath",
            "home",
            "sethome",
            "tpa",
            "tpatab",
            "msg"
    };

    private int rtpCooldown = 60;
    private boolean enableRtpLimitSpace = true;
    private WorldPlaneData rtpLimitSpace = new WorldPlaneData(
            new WorldPlaneData.WorldPlanePoint(500, 500),
            new WorldPlaneData.WorldPlanePoint(-500, -500)
    );
    private String[] rtpAllowWorld = new String[]{
            "default"
    };

    public static final BuilderCodec<PluginConfig> CODEC =
            BuilderCodec.builder(PluginConfig.class, PluginConfig::new)
                        .append(
                                new KeyedCodec<>("PluginPrefix", BuilderCodec.STRING),
                                (o, d) -> o.pluginPrefix = d,
                                o -> o.pluginPrefix
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("WelcomeText", BuilderCodec.STRING),
                                (o, d) -> o.welcomeText = d,
                                o -> o.welcomeText
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("JoinBroadcast", BuilderCodec.STRING),
                                (o, d) -> o.joinBroadcast = d,
                                o -> o.joinBroadcast
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("TeleportCooldown", BuilderCodec.INTEGER),
                                (o, d) -> o.teleportCooldown = d,
                                o -> o.teleportCooldown
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("MaxHome", BuilderCodec.INTEGER),
                                (o, d) -> o.maxHome = d,
                                o -> o.maxHome
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("AutoAddPlayerToDefault", BuilderCodec.BOOLEAN),
                                (o, d) -> o.autoAddNewPlayerToDefault = d,
                                o -> o.autoAddNewPlayerToDefault
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("EnablePublicCommandControl", BuilderCodec.BOOLEAN),
                                (o, d) -> o.enablePublicCommandControl = d,
                                o -> o.enablePublicCommandControl
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("PublicCommand", BuilderCodec.STRING_ARRAY),
                                (o, d) -> o.publicCommand = d,
                                o -> o.publicCommand
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("RtpCooldown", BuilderCodec.INTEGER),
                                (o, d) -> o.rtpCooldown = d,
                                o -> o.rtpCooldown
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("EnableRtpLimitSpace", BuilderCodec.BOOLEAN),
                                (o, d) -> o.enableRtpLimitSpace = d,
                                o -> o.enableRtpLimitSpace
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("RtpLimitSpace", WorldPlaneData.CODEC),
                                (o, d) -> o.rtpLimitSpace = d,
                                o -> o.rtpLimitSpace
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("RtpAllowWorld", BuilderCodec.STRING_ARRAY),
                                (o, d) -> o.rtpAllowWorld = d,
                                o -> o.rtpAllowWorld
                        )
                        .add()
                        .build();
}
