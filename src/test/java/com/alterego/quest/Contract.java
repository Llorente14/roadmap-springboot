package com.alterego.quest;

import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.context.ApplicationContext;

/**
 * Helper kit. JANGAN DIUBAH.
 *
 * Test quest Spring mengakses class buatanmu lewat reflection, supaya semua test tetap bisa
 * dikompilasi walaupun class-nya belum kamu buat. Pesan gagalnya menyebut apa yang harus dibuat.
 */
public final class Contract {

    private Contract() {
    }

    public static Class<?> type(String fqcn) {
        try {
            return Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            int dot = fqcn.lastIndexOf('.');
            return fail("Class " + fqcn.substring(dot + 1) + " belum ada. Buat di package " + fqcn.substring(0, dot) + ".");
        }
    }

    public static Object bean(ApplicationContext ctx, String fqcn) {
        Class<?> type = type(fqcn);
        String[] names = ctx.getBeanNamesForType(type);
        if (names.length == 0) {
            return fail(type.getSimpleName() + " ada, tapi belum menjadi bean Spring. Annotation apa yang membuat Spring mengelolanya?");
        }
        return ctx.getBean(type);
    }

    /** Panggil method publik berdasarkan nama dan jumlah argumen. Exception asli dilempar ulang apa adanya. */
    public static Object call(Object target, String method, Object... args) {
        Method m = Arrays.stream(target.getClass().getMethods())
                .filter(x -> x.getName().equals(method) && x.getParameterCount() == args.length)
                .findFirst()
                .orElseGet(() -> fail(target.getClass().getSimpleName() + " belum punya method publik "
                        + method + " dengan " + args.length + " parameter."));
        try {
            m.setAccessible(true);
            return m.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw sneaky(e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Baca properti: mendukung gaya record (nickname()) dan gaya getter (getNickname()/isActive()). */
    public static Object prop(Object target, String name) {
        String cap = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (String candidate : new String[] {name, "get" + cap, "is" + cap}) {
            try {
                Method m = target.getClass().getMethod(candidate);
                m.setAccessible(true);
                return m.invoke(target);
            } catch (NoSuchMethodException ignored) {
                // coba nama berikutnya
            } catch (InvocationTargetException e) {
                throw sneaky(e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return fail(target.getClass().getSimpleName() + " tidak punya accessor untuk '" + name + "' (" + name + "() atau get" + cap + "()).");
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneaky(Throwable t) throws E {
        throw (E) t;
    }
}
