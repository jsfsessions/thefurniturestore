package com.itr.parser.outlet.walmart.model;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public interface Selectable {

    default String getBaseSelector() {
        Selector selector = getClass().getAnnotation(Selector.class);

        if (selector == null) {
            throw new IllegalStateException("Base annotation is not set");
        }

        return selector.value();
    }

    default Map<String, Consumer<Elements>> getSelectors() {
        try {
            Map<String, Consumer<Elements>> selectable = new HashMap<>();
            Map<String, Method> setters = stream(getClass().getDeclaredMethods())
                    .filter(M -> M.getName().startsWith("set"))
                    .collect(
                            toMap(D -> D.getName().replace("set", "").toUpperCase(), D -> D)
                    );
            for (Field field : getClass().getDeclaredFields()) {
                Selector selector = field.getAnnotation(Selector.class);
                if (selector == null) {
                    continue;
                }
                Consumer<Elements> setter = V -> {
                    Method handler = setters.get(field.getName().toUpperCase());
                    try {
                        if (selector.isCollection()) {
                            handler.invoke(this, V.stream().map(Element::text).collect(toList()));  
                        } else { 																   //the setter, setting this collection to item Collection field (List<String> images)
                            handler.invoke(this, V.text());  
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("Error reading properties", e);
                    }
                };

                selectable.put(selector.value(), setter);
            }
            return selectable;
        } catch (Exception e) {
            throw new IllegalStateException("Error reading properties", e);
        }
    }
}
