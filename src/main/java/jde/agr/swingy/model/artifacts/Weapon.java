package jde.agr.swingy.model.artifacts;

import jde.agr.swingy.Utility.EArtifact;
import lombok.Getter;

@Getter
public class Weapon extends Artifact {

    private int attack;

    public Weapon(String name, int attack) {
        super(name);
        this.type = EArtifact.WEAPON;
        this.attack = attack;
    }
}
