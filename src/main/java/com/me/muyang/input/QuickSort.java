package com.me.muyang.input;

import java.util.List;

/**
 * Created by yangxianda on 2017/4/1.
 */
public class QuickSort<T> implements SortFunction<T> {
    public void sort(List<T> list) {
    }

    public void quickSort(T[] array, int beg, int end){
        if(beg >= end || array == null) return;
        int p = partion(array, beg, end);
        quickSort(array, beg, p -1);
        quickSort(array, p+1, end);
    }

    public int middle(int [] list, int low, int high){
        int tmp = list[low];    //数组的第一个作为中轴
        while (low < high) {
            while (low < high && list[high] > tmp) {
                high--;
            }
            list[low] = list[high];   //比中轴小的记录移到低端
            while (low < high && list[low] <= tmp) {
                low++;
            }
            list[high] = list[low];   //比中轴大的记录移到高端
        }
        list[low] = tmp;              //中轴记录到尾
        return low;                   //返回中轴的位置
    }
    public int partion(T[] array, int beg, int end){
        T first = array[beg];
        int i = beg, j = end;
        while (i < j) {
            while (i < j && ((Comparable<Object>)array[j]).compareTo(first) > 0 ) { // 比目标值 大，一旦出现比目标值小的，把值给目标值
                j--;
            }
            array[i] = array[j];
            while (i < j && ((Comparable<Object>)array[i]).compareTo(first) < 0 ) { // 同理
                i++;
            }
            array[j] = array[i];

        }
        array[i] = first;
        return i;
    }

    public static void main(String [] args){
        Integer [] array = new Integer[]{4,5,3,8,10,0};
        QuickSort<Integer> sort = new QuickSort<Integer>();
        sort.quickSort(array,0, array.length-1);

    }
}
