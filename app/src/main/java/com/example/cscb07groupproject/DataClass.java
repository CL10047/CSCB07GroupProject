package com.example.cscb07groupproject;

public class DataClass {

    private String Title;
    private String Date;
    private String Description;

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }
    public DataClass(String Title, String Date, String Description) {
        this.Title = Title;
        this.Date = Date;
        this.Description = Description;
    }

    public DataClass(){
    }


}
