package com.alexsanderyurevich.data;

import com.alexsanderyurevich.data.DeclineData.Cases;

import javax.annotation.Nonnull;
import java.util.EnumMap;

public final class Decline {
    private final EnumMap<Cases, String> declines = new EnumMap<>(Cases.class);

    void addDecline(@Nonnull Cases cases, @Nonnull String decline) {
        declines.put(cases,decline);
    }

    String getDecline(@Nonnull Cases cases) {
        return declines.get(cases);
    }

    @Override
    public String toString() {
        return "Decline{" +
                "declines=" + declines +
                '}';
    }
}
