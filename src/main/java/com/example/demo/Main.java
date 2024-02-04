package com.example.demo;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        int M = scanner.nextInt();

        int[] cards = new int[N];
        for (int i = 0; i < N; i++) {
            cards[i] = scanner.nextInt();
        }

        //오름차순 정렬
        Arrays.sort(cards);

        //결과값 넣는 곳.
        int closestSum = 0;
        int result;

        //로직 시작
        //cards i는 1번째, left는 2, right는 3번 째 카드임.
        for (int i = 0; i < N - 2; i++) {
            int left = i + 1;
            int right = N - 1;

            while (left < right) {
                result = cards[i] + cards[left] + cards[right]; //3개 합.

                //21에 근사할 경우 RESULT에 값 저장
                if (Math.abs(M - result) < Math.abs(M - closestSum)) {
                    closestSum = result;
                }

                //result가 21보다 적다면, 두 번째 카드는 오른쪽으로 이동(숫자 증가)
                if (result < M) {
                    left++;
                } else {
                    //21보다 크다면 3번째 카드는 오른 쪽부터 (숫자 감소)
                    right--;
                }
            }

        }
        //결과 출력
        System.out.println(closestSum);
    }
}

