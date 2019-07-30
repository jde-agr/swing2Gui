package jde.agr.swingy.model.characters;

public class Rat extends Foe {

    public Rat(int level) {
        super(level);
        this.name = "Rodent Rat";
        this.type = "Rat";
        this.attack = 7 + this.level;
        this.defense = 2 + this.level;
        this.hp = 20 + this.level;
    }
}
