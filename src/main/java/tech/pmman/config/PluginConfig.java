package tech.pmman.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Data;

@Data
public class PluginConfig {
    private String pluginPrefix = "[SimpleEss]";
    private String welcomeText = "";
    private String joinBroadcast = "";
    private int teleportCooldown = 120;
    private String[] publicCommand = new String[]{
            "back",
            "backdeath",
            "home",
            "sethome",
            "tpa",
            "tpatab",
            "msg"
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
                                new KeyedCodec<>("PublicCommand", BuilderCodec.STRING_ARRAY),
                                (o, d) -> o.publicCommand = d,
                                o -> o.publicCommand
                        )
                        .add()
                        .build();
}
