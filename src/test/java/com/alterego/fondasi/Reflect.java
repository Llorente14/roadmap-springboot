package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.alterego.quest.Contract;

/**
 * Helper kit. JANGAN DIUBAH.
 *
 * Beberapa latihan Fondasi meminta kamu MEMBUAT method atau class sendiri. Test mengaksesnya lewat
 * reflection supaya tetap bisa dikompilasi sebelum kamu membuatnya.
 */
final class Reflect {

    private static final String PKG = "com.alterego.fondasi.";

    private Reflect() {
    }

    static Class<?> type(String simpleName) {
        return Contract.type(PKG + simpleName);
    }

    /** Panggil method public static berdasarkan nama dan jumlah parameter. */
    static Object callStatic(String simpleName, String method, Object... args) {
        Class<?> type = type(simpleName);
        Method m = Arrays.stream(type.getDeclaredMethods())
                .filter(x -> x.getName().equals(method) && x.getParameterCount() == args.length)
                .findFirst()
                .orElseGet(() -> fail(simpleName + " belum punya method " + method + " dengan " + args.length + " parameter."));
        if (!Modifier.isStatic(m.getModifiers()) || !Modifier.isPublic(m.getModifiers())) {
            return fail(simpleName + "." + method + " harus public static.");
        }
        try {
            return m.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw sneaky(e.getCause());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            return fail(simpleName + "." + method + " tidak bisa dipanggil dengan " + Arrays.toString(args) + ": " + e.getMessage());
        }
    }

    /** Buat object lewat constructor public dengan jumlah parameter yang sama. */
    static Object create(String simpleName, Object... args) {
        Class<?> type = type(simpleName);
        Constructor<?> c = Arrays.stream(type.getConstructors())
                .filter(x -> x.getParameterCount() == args.length)
                .findFirst()
                .orElseGet(() -> fail(simpleName + " belum punya constructor public dengan " + args.length + " parameter."));
        try {
            return c.newInstance(args);
        } catch (InvocationTargetException e) {
            throw sneaky(e.getCause());
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            return fail(simpleName + " tidak bisa dibuat dengan " + Arrays.toString(args) + ": " + e.getMessage());
        }
    }

    static Object call(Object target, String method, Object... args) {
        return Contract.call(target, method, args);
    }

    static Object prop(Object target, String name) {
        return Contract.prop(target, name);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneaky(Throwable t) throws E {
        throw (E) t;
    }
}
