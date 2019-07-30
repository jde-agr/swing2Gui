package jde.agr.swingy.model.artifacts;

import jde.agr.swingy.Utility.EArtifact;
import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class Artifact implements Serializable {

    String name;
    EArtifact type;

    Artifact(String name) {
        this.name = name;
    }
}
