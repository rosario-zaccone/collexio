package org.collexio;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static double mean(final List<Integer> nums) {
        int sum = 0;
        for (Integer elem: nums)
            sum += elem;
        return (double) sum / nums.size();
    }
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(10); l.add(20); l.add(30); l.add(40);
        System.out.println(mean(l));
    }
}
