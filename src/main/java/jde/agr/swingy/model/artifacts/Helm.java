package jde.agr.swingy.model.artifacts;


import jde.agr.swingy.Utility.EArtifact;
import lombok.Getter;

@Getter
public class Helm extends Artifact {
    private int hp;

    public Helm(String name, int hp) {
        super(name);
        this.type = EArtifact.HELM;
        this.hp = hp;
    }
}
