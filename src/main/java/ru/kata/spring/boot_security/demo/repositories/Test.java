package ru.kata.spring.boot_security.demo.repositories;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        System.out.println(calc(5, 3));
    }
    public static int calc(int a, int b) {
        int sum = a + b;
        return sum;
    }
}
