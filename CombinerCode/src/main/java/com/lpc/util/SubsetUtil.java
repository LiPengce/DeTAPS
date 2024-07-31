package com.lpc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-01
 */
public class SubsetUtil {
    public static List<List<Integer>> getSubsets(int total, int t) {
        List<List<Integer>> subsets = new ArrayList<>();
        backtrack(subsets, new ArrayList<>(), total, t, 1);
        return subsets;
    }

    private static void backtrack(List<List<Integer>> subsets, List<Integer> tempList, int total, int t, int start) {
        if (tempList.size() == t) {
            subsets.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = start; i <= total; i++) {
            tempList.add(i);
            backtrack(subsets, tempList, total, t, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    public static void main(String[] args) {
        int total = 20;
        int t = 5;
        List<List<Integer>> subsets = getSubsets(total, t);
        for (List<Integer> subset : subsets) {
            System.out.println(subset);
        }

        System.out.println(subsets.size());
    }
}
