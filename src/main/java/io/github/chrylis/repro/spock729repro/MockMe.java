package io.github.chrylis.repro.spock729repro;

import java.util.UUID;

public interface MockMe {

    static final UUID ONE = new UUID(0, 1);

    boolean equalsOne(UUID uuid);

    default boolean equalsOneFromString(String uuid) {
        return equalsOne(UUID.fromString(uuid));
    }
}
