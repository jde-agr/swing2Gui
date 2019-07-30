package jde.agr.swingy.model.characters;

public class Bat extends Foe {

    public Bat(int level) {
        super(level);
        this.name = "Bat Brand";
        this.type = "Bat";
        this.attack = 5 + this.level;
        this.defense = 1 + this.level;
        this.hp = 10 + this.level;
    }
}
