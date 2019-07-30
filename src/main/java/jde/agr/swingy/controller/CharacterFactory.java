package jde.agr.swingy.controller;

import jde.agr.swingy.Utility.EType;
import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.model.characters.*;
import jde.agr.swingy.model.characters.Character;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static jde.agr.swingy.Utility.Global.factory;

public abstract class CharacterFactory {

    private static Character character;

    public static Character newHero(String name, EType type) {
        switch (type) {
            case WARRIOR:
                character = new Warrior(name);
                break;
            case THIEF:
                character = new Thief(name);
                break;
            case WIZARD:
                character = new Wizard(name);
                break;
        }
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Character>> constraintViolations = validator.validate(character);
        if (constraintViolations.size() > 0 ) {
            for (ConstraintViolation<Character> constraints : constraintViolations) {
                Logger.print(constraints.getRootBeanClass().getSimpleName()+
                        "." + constraints.getPropertyPath() + " " + constraints.getMessage());
            }
            return (null);
        }
        else {
            return (character);
        }
    }

    static Character newFoe(EType type, Character hero) {
        switch (type) {
            case RAT:
                character = new Rat(hero.getLevel());
                break;
            case BAT:
                character = new Bat(hero.getLevel());
                break;
            case SLIME:
                character = new Slime(hero.getLevel());
                break;
        }
        return (character);
    }
}
