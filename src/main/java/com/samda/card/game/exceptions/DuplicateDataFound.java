package com.samda.card.game.exceptions;

public class DuplicateDataFound extends IllegalArgumentException{
    private String resourcesName;
    private String fieldName;
    private long fieldValue;

    public DuplicateDataFound(String resourcesName, String fieldName, long fieldValue){
        super(String.format("Duplicate %s found with %s : %s", resourcesName, fieldName, fieldValue));
        this.resourcesName = resourcesName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
