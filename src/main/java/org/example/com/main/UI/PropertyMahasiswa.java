package org.example.com.main.UI;

import javafx.beans.property.SimpleStringProperty;
import org.example.com.main.data.Mahasiswa;
import java.util.ArrayList;

public class PropertyMahasiswa {
    private final SimpleStringProperty name;
    private final SimpleStringProperty nim;
    private final SimpleStringProperty faculty;
    private final SimpleStringProperty programStudi;
    private final SimpleStringProperty email;

    public PropertyMahasiswa(String name, String nim, String faculty, String programStudi, String email) {
        this.name = new SimpleStringProperty(name);
        this.nim = new SimpleStringProperty(nim);
        this.faculty = new SimpleStringProperty(faculty);
        this.programStudi = new SimpleStringProperty(programStudi);
        this.email = new SimpleStringProperty(email);
    }

    public static ArrayList<PropertyMahasiswa> mahasiswaToProperty(ArrayList<Mahasiswa> arr) {
        ArrayList<PropertyMahasiswa> temp = new ArrayList<>();
        for (Mahasiswa mahasiswa : arr) {
            PropertyMahasiswa obj = new PropertyMahasiswa(
                mahasiswa.getName(), 
                mahasiswa.getNIM(), 
                mahasiswa.getFaculty(), 
                mahasiswa.getProgramStudi(), 
                mahasiswa.getEmail()
            );
            temp.add(obj);
        }
        return temp;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNim() {
        return nim.get();
    }

    public SimpleStringProperty nimProperty() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim.set(nim);
    }

    public String getFaculty() {
        return faculty.get();
    }

    public SimpleStringProperty facultyProperty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty.set(faculty);
    }

    public String getProgramStudi() {
        return programStudi.get();
    }

    public SimpleStringProperty programStudiProperty() {
        return programStudi;
    }

    public void setProgramStudi(String programStudi) {
        this.programStudi.set(programStudi);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
