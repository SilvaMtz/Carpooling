package itesm.mx.carpoolingtec.model;

import java.util.ArrayList;

public class Ride {
    private String Driver_IdStudent;
    private ArrayList<String> Passangers_IdStudent;
    private Boolean Smoking;
    private int Genders;
    private String Description;

    public Ride() {
    }

    public Ride(String driver_IdStudent, ArrayList<String> passangers_IdStudent, Boolean smoking, int genders, String description) {
        Driver_IdStudent = driver_IdStudent;
        Passangers_IdStudent = passangers_IdStudent;
        Smoking = smoking;
        Genders = genders;
        Description = description;
    }

    public String getDriver_IdStudent() {
        return Driver_IdStudent;
    }

    public void setDriver_IdStudent(String driver_IdStudent) {
        Driver_IdStudent = driver_IdStudent;
    }

    public ArrayList<String> getPassangers_List() {
        return Passangers_IdStudent;
    }

    public String getPassangers_IdStudent(int index) {
        return Passangers_IdStudent.get(index);
    }

    public void setPassangers_IdStudent(ArrayList<String> passangers_IdStudent) {
        Passangers_IdStudent = passangers_IdStudent;
    }

    public Boolean getSmoking() {
        return Smoking;
    }

    public void setSmoking(Boolean smoking) {
        Smoking = smoking;
    }

    public int getidGenders() {
        return Genders;
    }

    public String getGenders() {
        switch (Genders){
            case 1:
                return "Male";
            case 2:
                return "Female";
            case 3:
                return "Both";
        }
        return null;
    }

    public void setGenders(int genders) {
        Genders = genders;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
