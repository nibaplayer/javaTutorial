package chapter5;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        int[] arr = new int[3];
        arr = new int[] {1,2,0};
        Arrays.sort(arr);
        int[] arr2 = arr;
        System.out.println(arr[2]);
    }
}
