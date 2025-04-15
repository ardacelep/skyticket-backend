package com.skylab.skyticket.core.utilities;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void copyNonNullProperties(Object source, Object target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            boolean accessibleBefore = sourceField.canAccess(source); // Önceki erişim durumunu sakla
            sourceField.setAccessible(true); // Erişimi aç

            try {
                Object value = sourceField.get(source); // Kaynaktaki değeri al
                if (value != null) { // Eğer null değilse hedef nesneye kopyala
                    Field targetField = getField(targetClass, sourceField.getName());
                    if (targetField != null) {
                        boolean targetAccessibleBefore = targetField.canAccess(target);
                        targetField.setAccessible(true);

                        targetField.set(target, value);

                        targetField.setAccessible(targetAccessibleBefore); // Eski erişim durumuna döndür
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                sourceField.setAccessible(accessibleBefore); // Kaynak alanın erişim durumunu eski haline getir
            }
        }
    }

    // Hedef sınıfta alanın olup olmadığını kontrol eden yardımcı metod
    private static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Eğer alan bulunmazsa üst sınıfta aramaya devam et
            }
        }
        return null;
    }

}
