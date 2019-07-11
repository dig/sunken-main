package net.sunken.common.event.example;

import net.sunken.common.event.ListensToEvent;
import net.sunken.common.event.SunkenListener;

public class ExampleListener implements SunkenListener {

    @ListensToEvent
    public void onExample(ExampleEvent e) {
        System.out.println("hi");
    }
}
