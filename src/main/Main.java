package main;

import game.Game;

import java.util.Scanner;


/**
 * Created by protoCJ on 23.04.2017.
 */
public class Main {
    public static void main(String[] args) {
        Game g;
        System.out.println("Give server address:");
        String host = new Scanner(System.in).nextLine();
        g = new Game(host, 9090);
        g.start();
    }
}
