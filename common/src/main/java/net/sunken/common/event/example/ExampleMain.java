package net.sunken.common.event.example;

import net.sunken.common.event.EventManager;

public class ExampleMain {

    public static void main(String[] args) {
        EventManager.register(new ExampleListener());

        EventManager.callEvent(new ExampleEvent());
    }
}
