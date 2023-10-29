package com.example.demo;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String a = sc.nextLine();
        int b = 0;

        for(int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);

            if (c == 'A' || c == 'B' || c == 'C') {
                b += 3;
            } else if (c == 'F' || c == 'E' || c == 'D') {
                b += 4;
            } else if (c == 'G' || c == 'H' || c == 'I') {
                b += 5;
            } else if (c == 'J' || c == 'K' || c == 'L') {
                b += 6;
            } else if (c == 'M' || c == 'N' || c == 'O') {
                b += 7;
            } else if (c == 'P' || c == 'Q' || c == 'R' || c == 'S') {
                b += 8;
            } else if (c == 'T' || c == 'U' || c == 'V') {
                b += 9;
            } else {
                b += 10;
            }
        }

        System.out.println(a);
        System.out.println(b);
    }
}
