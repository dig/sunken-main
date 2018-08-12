package me.minigames.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDefinition {
    String name();
    String usage();
    int minArgs() default 0;
    int maxArgs() default -1;
}
