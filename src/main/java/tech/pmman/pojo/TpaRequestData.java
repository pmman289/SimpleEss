package tech.pmman.pojo;

import lombok.Data;

import java.util.UUID;

@Data
public class TpaRequestData {
    private UUID requestPlayer;
    private Long timestamp;

    public TpaRequestData(UUID requestPlayer) {
        this.requestPlayer = requestPlayer;
        timestamp = System.currentTimeMillis();
    }

    public TpaRequestData(UUID requestPlayer, Long timestamp) {
        this.requestPlayer = requestPlayer;
        this.timestamp = timestamp;
    }

    public TpaRequestData() {
    }
}
