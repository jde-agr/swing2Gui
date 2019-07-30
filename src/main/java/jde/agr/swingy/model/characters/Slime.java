package jde.agr.swingy.model.characters;

public class Slime extends Foe {

    public Slime(int level) {
        super(level);
        this.name = "Slime Slob";
        this.type = "Slime";
        this.attack = 1 + this.level;
        this.defense = 1 + this.level;
        this.hp = 2 + this.level;
    }
}
