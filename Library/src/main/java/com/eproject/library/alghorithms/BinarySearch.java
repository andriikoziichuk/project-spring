package com.eproject.library.alghorithms;
import java.util.ArrayList;
import java.util.Comparator;

public class BinarySearch {
    public static <T extends Comparable<T>> Integer binarySearch(ArrayList<T>
                                                                         array, T target){
        return binarySearch(array, target, T::compareTo);
    }
    public static <T>Integer binarySearch(ArrayList<T> array, T target,
                                          Comparator<T> cmp){
        int low = 0, height = array.size() - 1;
        int middle;
        int index = -1;
        while(low <= height){
            middle = (height + low)/2;
            T el = array.get(middle);
            if (cmp.compare(target, el) == 0){
                index = middle;
                break;
            }
            else if (cmp.compare(target, el) > 0){ //target > el
                low = middle + 1;
            }
            else if (cmp.compare(target, el) < 0){
                height = middle - 1;
            }
        }
        return index;
    }
}