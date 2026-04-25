package bankapp.domain;

import java.util.List;

public class TypeMovement {

    int typeId;
    String nameType;
    String descriptionType;

    //constructores
    public TypeMovement(int typeId, String nameType, String descriptionType) {
        this.typeId = typeId;
        this.nameType = nameType;
        this.descriptionType = descriptionType;
    }

    // get y set
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }

    //metodos

    public TypeMovement createTypeMovement(TypeMovement type){
        return type;
    }

    public TypeMovement updateTypeMovement(TypeMovement type){
        return type;
    }

    public List<TypeMovement> getAllTypeMovements(){
        return null;
    }

    public TypeMovement getTypeMovementById(int id, TypeMovement type){
        return null;
    }

    public void deleteTypeMovement(int id){

    }
}