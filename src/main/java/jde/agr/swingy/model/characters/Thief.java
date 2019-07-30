package jde.agr.swingy.model.characters;

public class Thief extends Hero {

    public Thief() {
        super();
    }

    public Thief(String name) {
        super(name);
        this.type = "Thief";
        this.attack += 7;
        this.defense += 2;
        this.hp += 50;
    }
}
