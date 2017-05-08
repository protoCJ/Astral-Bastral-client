package main;

import game.Game;


/**
 * Created by protoCJ on 23.04.2017.
 */
public class Main {
    public static void main(String[] args) {
        Game g;
        g = new Game("localhost", 9090);
        g.start();
    }
}
