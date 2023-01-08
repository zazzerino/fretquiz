package kdp.fretquiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtil {

//    public static <T> List<T> addItem(List<T> list, T item) {
//        var arrayList = new ArrayList<>(list);
//        arrayList.add(item);
//        return List.copyOf(arrayList);
//    }

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

//    public static <T> List<T> toggleItem(List<T> list, T item) {
//        return list.contains(item)
//                       ? removeItem(list, item)
//                       : addItem(list, item);
//    }

//    public static <T> int nextIndex(List<T> list, T item) {
//        var index = list.indexOf(item);
//        return (index + 1) % list.size();
//    }

    public static <T> Optional<Integer> indexOfMatchingItem(List<T> list, Predicate<T> predicate) {
        if (list.isEmpty()) {
            return Optional.empty();
        }

        for (var index = 0; index < list.size(); index++) {
            var item = list.get(index);

            if (predicate.test(item)) {
                return Optional.of(index);
            }
        }

        return Optional.empty();
    }

    public static <T> T randomItem(List<T> list) {
        var index = new Random().nextInt(list.size());
        return list.get(index);
    }

    public static <T> List<T> replaceItem(List<T> list, int index, T newItem) {
        var arrayList = new ArrayList<>(list);
        arrayList.set(index, newItem);
        return List.copyOf(arrayList);
    }

    public static <T> List<T> updateWhere(List<T> list,
                                          Predicate<T> predicate,
                                          Function<T, T> update) {
        return list
                .stream()
                .map(t -> predicate.test(t) ? update.apply(t) : t)
                .toList();
    }
}
