package com.kdp.fretquiz.game;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

//@Value.Immutable
//public interface Game {
//    String id();
//    Instant createdAt();
//    Settings settings();
//    List<Round> rounds();
//    GameState state();
//    String hostId();
//    List<Player> players();
//
//    static Game createWithHost(Player host) {
//        return ImmutableGame
//                .builder()
//                .id(UUID.randomUUID().toString())
//                .createdAt(Instant.now())
//                .settings(Settings.createDefault())
//                .state(GameState.INIT)
//                .hostId(host.id())
//                .addPlayers(host)
//                .build();
//    }
//}
