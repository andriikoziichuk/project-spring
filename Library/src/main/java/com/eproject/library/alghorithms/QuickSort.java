package com.eproject.library.alghorithms;

import java.util.*;

public class QuickSort {

    private static final Random rnd = new Random();
    public static <T extends Comparable<T>> List<T> qSort(List<T> l){
        return qSort(l, T::compareTo);
    }
    public static <T>List<T> qSort(List<T> l, Comparator<T> cmp){
        if (l.size() < 2){
            return l;
        }
        List<T> smaller = new ArrayList<>(l.size()/2);
        List<T> bigger = new ArrayList<>(l.size()/2);
        T pivot = l.remove(rnd.nextInt(l.size()));
        for (T el : l) {
            if (cmp.compare(el, pivot) > 0) { //el > pivot
                bigger.add(el);
            } else {
                smaller.add(el);
            }
        }
        smaller = qSort(smaller, cmp);
        bigger = qSort(bigger, cmp);
        smaller.add(pivot);
        smaller.addAll(bigger);
        return smaller;
    }
}
