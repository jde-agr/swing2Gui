package jde.agr.swingy.controller;

import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.model.characters.Character;
import jde.agr.swingy.view.SquareMap;

public class MapFactory {

    public static SquareMap generateMap(Character hero) {
        int mapSize = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);

        if (mapSize > 19) {
            mapSize = 19;
        }
        SquareMap squareMap = new SquareMap(mapSize);
        squareMap.registerHero(hero);
        Logger.print(hero.getName() + " arrived in a new hostile environment");
        squareMap.generateFoes();
        return (squareMap);
    }
}
