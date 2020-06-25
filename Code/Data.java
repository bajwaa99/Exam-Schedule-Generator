package com.mycompany.datesheet;

import java.util.ArrayList;


//Data class represents a single index of the complete table of exam schedule
public class Data {

    ArrayList<Course> courses=new ArrayList<>();

    //This array contains all the allotted rooms and students in that room for an exam
    ArrayList<Student_Room> student_room=new ArrayList<>();

    ArrayList<Student> students=new ArrayList<>();

    //Adds a new entry in the Data
    public void addData(Course course) {
        
        int flag=0;
        
        for(int i=0;i<courses.size();i++){
        
            if(courses.get(i).name.equals(course.name)){
                flag=1;
                break;
            }
        }
        
        if(flag==0)
            courses.add(course);
        
    }
    
}
