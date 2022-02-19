package com.example.equipshare;

public class Reagent {
    String Name,Quantity,LabName,Department,ReagentID;

    public Reagent(String name, String quantity, String labName, String department, String reagentID) {
        Name = name;
        Quantity = quantity;
        LabName = labName;
        Department = department;
        ReagentID= reagentID;
    }

    public Reagent(){

    }

    public String getLabName() {
        return LabName;
    }

    public void setLabName(String labName) {
        LabName = labName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getReagentID() {
        return ReagentID;
    }

    public void setReagentID(String reagentID) {
        ReagentID = reagentID;
    }
}
