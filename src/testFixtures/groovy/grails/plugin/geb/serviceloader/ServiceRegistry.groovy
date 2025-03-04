package grails.plugin.geb.serviceloader
import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap

/**
 * A service registry that loads and caches different service types using the Java
 * {@link java.util.ServiceLoader}, while allowing overriding which instance to return.
 * <p>
 * This class provides thread-safe service loading and caching, supporting parallel test execution.
 * It provides both automatic service discovery through {@link java.util.ServiceLoader}
 * and explicit replacement of the returned service instance for customization.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * MyService service = ServiceRegistry.getInstance(MyService, DefaultMyService)
 * </pre>
 * </p>
 *
 * @since 4.2
 * @author Mattias Reichel
 */
@CompileStatic
class ServiceRegistry {

    private static final ThreadLocal<ConcurrentHashMap<Class<?>, Object>> INSTANCES = ThreadLocal.withInitial {
        new ConcurrentHashMap<Class<?>, Object>()
    }

    /**
     * Returns the service instance of the given service type, loading it using
     * {@link java.util.ServiceLoader} if not already loaded or an instance
     * of the default implementation type if no service implementation is found
     * by the {@link java.util.ServiceLoader}.
     *
     * If an instance has been set using {@link #setInstance(Class, Object)} or
     * {@link #setInstance(Class, Class)}, that instance will be returned instead.
     *
     * @param serviceType The service type
     * @param defaultType The service implementation type to use if no service
     * implementation is found (Must have a zero-argument constructor)
     * @return An instance of the service type
     */
    static <T> T getInstance(Class<T> serviceType, Class<? extends T> defaultType) {
        (T) INSTANCES.get().computeIfAbsent(serviceType) {
            ServiceLoader.load(serviceType)
                    .findFirst()
                    .orElseGet { defaultType.getDeclaredConstructor().newInstance() }
        }
    }

    /**
     * Sets the instance to be returned for the given service type, bypassing instance
     * loading from {@link java.util.ServiceLoader}.
     * <p>
     * Setting the instance to {@code null} will revert to loading the the service instance
     * via the {@link java.util.ServiceLoader}.
     * </p>
     * @param serviceType The service type for which the instance should be set
     * @param instance The instance to return for the given service type, or
     * {@code null} for default service loading
     */
    static <T> void setInstance(Class<T> serviceType, T instance) {
        INSTANCES.get().put(serviceType, instance)
    }

    /**
     * Sets the implementation type to return for the given service type, bypassing instance loading
     * from {@link java.util.ServiceLoader}.
     *
     * @param serviceType The service type for which the instance should be set
     * @param instanceType The type of the instance to return for the given service type.
     * (Must have a zero-argument constructor).
     */
    static <T> void setInstance(Class<T> serviceType, Class<? extends T> instanceType) {
        setInstance(serviceType, instanceType.getDeclaredConstructor().newInstance())
    }
}
