package com.example.demo;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        for (int i = 1; i < N; i++) {
            int decompositionSum = i;
            int number = i;

            while (number != 0) {
                decompositionSum += number % 10;
                number /= 10;
            }

            if (decompositionSum == N) {
                System.out.println(i);
                return;
            }
        }

        System.out.println(0);
    }
}
