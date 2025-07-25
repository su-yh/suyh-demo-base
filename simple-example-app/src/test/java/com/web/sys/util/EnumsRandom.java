package com.web.sys.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author suyh
 * @since 2025-06-10
 */
public class EnumsRandom<E extends Enum<E>> {
    private final Random random = new Random();
    private final E[] values;

    public EnumsRandom(E[] values) {
        this.values = values;
    }

    public List<E> obtainList() {
        int enable = random.nextInt(2);
        if (enable == 0) {
            return null;
        }

        List<E> enList = new ArrayList<>();
        int num = random.nextInt(7);
        for (int i = 0; i < num; i++) {
            E e = obtainEnum();
            enList.add(e);
        }

        return enList;
    }

    public E obtainEnum() {
        int index = random.nextInt(values.length);
        return values[index];
    }
}
