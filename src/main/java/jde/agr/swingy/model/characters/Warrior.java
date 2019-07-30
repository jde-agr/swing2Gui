package jde.agr.swingy.model.characters;

public class Warrior extends Hero {

    public Warrior() {
        super();
    }

    public Warrior(String name) {
        super(name);
        this.type = "Warrior";
        this.attack += 5;
        this.defense += 3;
        this.hp += 75;
    }
}
