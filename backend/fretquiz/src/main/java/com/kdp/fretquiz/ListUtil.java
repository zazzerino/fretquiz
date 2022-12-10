package com.kdp.fretquiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ListUtil {

    public static <T> List<T> addItem(List<T> list, T item) {
        var arrayList = new ArrayList<>(list);
        arrayList.add(item);
        return List.copyOf(arrayList);
    }

    public static <T> List<T> removeItem(List<T> list, T item) {
        var arrayList = new ArrayList<>(list);
        arrayList.remove(item);
        return List.copyOf(arrayList);
    }

    public static <T> List<T> removeIf(List<T> list, Predicate<T> filter) {
        var arrayList = new ArrayList<>(list);
        arrayList.removeIf(filter);
        return List.copyOf(arrayList);
    }

    public static <T> List<T> toggleItem(List<T> list, T item) {
        if (list.contains(item)) {
            return removeItem(list, item);
        } else {
            return addItem(list, item);
        }
    }

    public static <T> int nextIndex(List<T> list, T item) {
        var index = list.indexOf(item);
        return (index + 1) % list.size();
    }

    public static <T> T nextItem(List<T> list, T item) {
        return list.get(nextIndex(list, item));
    }

    public static <T> T randomItem(List<T> list) {
        var index = new Random().nextInt(list.size());
        return list.get(index);
    }
}
