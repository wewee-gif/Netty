package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author:lmw
 * @date:2024/3/24 21:06
 **/
class piont {
    static final int TARGET = 24;
    static final double EPSILON = 1e-6;
    static final int ADD = 0, MULTIPLY = 1, SUBTRACT = 2, DIVIDE = 3;

    public static boolean judgePoint24(int[] nums) {
        List<Double> list = new ArrayList<Double>();
        for (int num : nums) {
            list.add((double) num);
        }
        return solve(list);
    }

    public static boolean solve(List<Double> list) {
        if (list.size() == 0) {
            return false;
        }
        if (list.size() == 1) {
            return Math.abs(list.get(0) - TARGET) < EPSILON;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    List<Double> list2 = new ArrayList<Double>();
                    for (int k = 0; k < size; k++) {
                        if (k != i && k != j) {
                            list2.add(list.get(k));
                        }
                    }
                    for (int k = 0; k < 4; k++) {
                        if (k < 2 && i > j) {
                            continue;
                        }
                        if (k == ADD) {
                            list2.add(list.get(i) + list.get(j));
                        } else if (k == MULTIPLY) {
                            list2.add(list.get(i) * list.get(j));
                        } else if (k == SUBTRACT) {
                            list2.add(list.get(i) - list.get(j));
                        } else if (k == DIVIDE) {
                            if (Math.abs(list.get(j)) < EPSILON) {
                                continue;
                            } else {
                                list2.add(list.get(i) / list.get(j));
                            }
                        }
                        if (solve(list2)) {
                            char a='?';
                            switch (k){
                                case 0:
                                    a = '+';
                                    break;
                                case 1:
                                    a='*';
                                    break;
                                case 2:
                                    a='-';
                                    break;
                                case 3:
                                    a='/';
                                    break;
                            }
                            System.out.print(list.get(i));
                            System.out.print(a);
                            System.out.println(list.get(j));
                            return true;
                        }
                        list2.remove(list2.size() - 1);
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            String[] stringItems = msg.trim().split(",");
            int[] intItems = new int[stringItems.length];
            for (int i = 0; i < stringItems.length; i++) {
                intItems[i] = Integer.parseInt(stringItems[i]);
            }
            System.out.println(judgePoint24(intItems));;

        }
    }
}
