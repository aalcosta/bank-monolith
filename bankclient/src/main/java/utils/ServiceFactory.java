package utils;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {

    private final static ServiceFactory instance = new ServiceFactory();

    private static Map<Class, Object> services = new HashMap<>();

    private static ServiceFactory getInstance() {
        return instance;
    }

    public static <T> T get(Class<T> clazz) throws RuntimeException{
        if (!services.containsKey(clazz)) {
            services.put(clazz, instantiateService(clazz));
        }
        return clazz.cast(services.get(clazz));
     }

    private static <T> T instantiateService(Class<T> clazz) throws RuntimeException {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
