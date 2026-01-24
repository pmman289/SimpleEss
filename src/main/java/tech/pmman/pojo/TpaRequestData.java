package tech.pmman.pojo;

import lombok.Data;

import java.util.UUID;

@Data
public class TpaRequestData {
    private UUID requestPlayer;
    private Long timestamp;
    private boolean isTpa = true;

    public TpaRequestData(UUID requestPlayer) {
        this.requestPlayer = requestPlayer;
        timestamp = System.currentTimeMillis();
    }

    public TpaRequestData(UUID requestPlayer, boolean isTpa) {
        this.requestPlayer = requestPlayer;
        this.isTpa = isTpa;
        timestamp = System.currentTimeMillis();
    }

    public TpaRequestData() {
    }
}
