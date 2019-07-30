package jde.agr.swingy.view;

import jde.agr.swingy.model.characters.Character;
import jde.agr.swingy.model.characters.Hero;
import lombok.Getter;
import java.util.Random;

import static jde.agr.swingy.Utility.Global.*;

@Getter
public class SquareMap {
    private int[][] map;
    public int mapSize;
    private Hero hero;
    private int[] oldPos = new int[] { -1, -1};;

    public SquareMap(int mapSize) {
        this.mapSize = mapSize;
        this.map = new int[mapSize][mapSize];
    }

    public void updateHeroPosition() {
        this.map[oldPos[0]][oldPos[1]] = 0;
        oldPos[0] = hero.getX();
        oldPos[1] = hero.getY();
        if (this.map[hero.getX()][hero.getY()] == 2) {
            this.map[hero.getX()][hero.getY()] = 8;
        } else {
            this.map[hero.getX()][hero.getY()] = 1;
        }
        if (!bIsGUI) {
            printMap();
        }
    }

    public void registerHero(Character hero) {
        this.hero = (Hero) hero;
        this.hero.register(this);
        this.hero.setX(mapSize / 2);
        this.hero.setY(mapSize / 2);
        oldPos[0] = this.hero.getX();
        oldPos[1] = this.hero.getY();
        this.map[mapSize / 2][mapSize / 2] = 1;
    }

    public void generateFoes() {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (map[i][j] != 1) {
                    int random = new Random().nextInt(3);
                    if (random == 0) {
                        map[i][j] = 2;
                    }
                }
            }
        }
        if (!bIsGUI) {
            printMap();
        }
    }

    private void printMap() {
        for (int[] line : map) {
            for (int col : line) {
                String box = col + " ";
                switch (col) {
                    case 1:
                        System.out.print(ANSI_GREEN + box + ANSI_RESET);
                        break;
                    case 2:
                        System.out.print(ANSI_RED + box + ANSI_RESET);
                        break;
                    case 8:
                        System.out.print(ANSI_YELLOW + box + ANSI_RESET);
                        break;
                    default:
                        System.out.print(box);
                        break;
                }
            }
            System.out.println();
        }
    }
}
