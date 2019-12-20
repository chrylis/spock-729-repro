package io.github.chrylis.repro.spock729repro;

import java.util.UUID;

public class RealImplementation implements MockMe {

    @Override
    public boolean equalsOne(UUID uuid) {
        return ONE.equals(uuid);
    }
}
