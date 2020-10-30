package mua;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Reader reader = new Reader(in);
        reader.readAll();
    }
}