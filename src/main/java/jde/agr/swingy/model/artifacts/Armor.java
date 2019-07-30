package jde.agr.swingy.model.artifacts;

import jde.agr.swingy.Utility.EArtifact;
import lombok.Getter;

@Getter
public class Armor extends Artifact {

    private int defense;

    public Armor(String name, int defense) {
        super(name);
        this.type = EArtifact.ARMOR;
        this.defense = defense;
    }
}
