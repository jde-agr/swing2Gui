package jde.agr.swingy.model.characters;

import jde.agr.swingy.Utility.Logger;

public abstract class Foe extends Character {

    Foe(int level) {
        this.level = level;
    }

    public void attack(Character character) {
        Logger.print(this.getName() + " is attacking");
        character.defend(this, this.attack);
        if (character.getHp() <= 0) {
            switch (character.getType()) {
                case "Warrior":
                    Logger.print(this.name + " says: You, a warrior ? you must be joking !");
                    break;
                case "Thief":
                    Logger.print(this.name + " says: Noob !");
                    break;
                case "Wizard":
                    Logger.print(this.name + " says: You are too weak !");
                    break;
            }
        }
    }

    public void defend(Character character, int damage) {
        int realDamage = damage - this.defense;

        if (realDamage <= 0) {
            realDamage = 1;
        }
        this.hp -= realDamage;
        Logger.print(character.getName() + " dealt " + realDamage + " damage to " + this.name);
        if (hp <= 0) {
            Logger.print(this.name + " died");
        }
    }
}
