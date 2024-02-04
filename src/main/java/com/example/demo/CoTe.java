package com.example.demo;

import java.util.Scanner;

public class CoTe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();
        int e = scanner.nextInt();
        int f = scanner.nextInt();

        int x = (c*e - b*f) / (a*e - b*d);
        int y = (a*f - c*d) / (a*e - b*d);

        System.out.println(x + " " + y);
    }
}