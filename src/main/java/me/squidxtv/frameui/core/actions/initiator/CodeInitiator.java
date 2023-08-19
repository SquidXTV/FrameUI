package me.squidxtv.frameui.core.actions.initiator;

import org.jetbrains.annotations.NotNull;

public record CodeInitiator(Class<?> clazz) implements ActionInitiator<Class<?>> {

    @Override
    public @NotNull Class<?> getInitiator() {
        return clazz;
    }
}
