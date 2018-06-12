package galatos.notification.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import static org.junit.Assert.*;

public abstract class StaticClassTest<T> {

    private Class<T> clazz;

    public StaticClassTest(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Test
    public void whenInstantiateStaticClass_thenThrowException() throws IllegalAccessException, InstantiationException {
        final Class<?> cls = clazz;
        final Constructor<?> c = cls.getDeclaredConstructors()[0];
        c.setAccessible(true);

        Throwable targetException = null;
        try {
            c.newInstance((Object[])null);
        } catch (InvocationTargetException ite) {
            targetException = ite.getTargetException();
        }

        assertNotNull(targetException);
        assertEquals(targetException.getClass(), InstantiationException.class);
    }

}