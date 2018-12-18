package admin.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Reflector {

    public static <T> List<String> getPropertiesNamesOf(Class<T> c) {
        return Arrays.stream(c.getDeclaredFields())
                     .map(Field::getName)
                     .collect(Collectors.toList());
    }

    public static Map<String, Object> getPropertiesWithValues(Object bean) {
        try {
            //can't use LinkedHashMap or smth like that to get properties in declaration order
            //because Introspector itself doesn't provide an order
            var map = new HashMap<String, Object>();
            for (var desc : Introspector.getBeanInfo(bean.getClass(), Object.class)
                                        .getPropertyDescriptors()) {
                var method = desc.getReadMethod();
                if (Objects.nonNull(method)) {
                    var value = method.invoke(bean);
                    if (value != null) map.put(desc.getName(), value);
                }
            }
            return map;
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            return Collections.emptyMap();
        }
    }
}
