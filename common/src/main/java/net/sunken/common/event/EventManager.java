package net.sunken.common.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

public class EventManager {

    private static final Set<SunkenListener> allListeners;

    public static <T extends SunkenListener> void register(T listener) {
        allListeners.add(listener);
    }

    public static <T extends SunkenEvent> void callEvent(T event) {
        for (SunkenListener listener : allListeners) {
            Class<? extends SunkenListener> clazz = listener.getClass();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(ListensToEvent.class)) {
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length == 1) {
                        for (Parameter parameter : parameters) {
                            if (parameter.getType().getCanonicalName().equals(event.getClass().getCanonicalName())) {
                                try {
                                    method.invoke(listener, event);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        allListeners = new HashSet<>();
    }
}
