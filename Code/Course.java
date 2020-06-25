package com.mycompany.datesheet;

import java.util.ArrayList;



public class Course {

    int index;

    String name;

    Day day;

    Slot slot;

    ArrayList<Student> enrolledStudents=new ArrayList<>();


    Course(String string, int index) {
      
        name=string;
       
        this.index=index;
       
    }
    
    Course(Course c){
    
        name=c.name;
       
        enrolledStudents=c.enrolledStudents;
        
        day=c.day;
        
        slot=c.slot;
        
        index=c.index;
    
    }
    
    //This function enrolls a student in a specific Course
    void enrollStudent(Student s){
      
        int flag=0;
        
        for(int i=0;i<enrolledStudents.size();i++){
        
            if(enrolledStudents.get(i).rollNo==s.rollNo){
                flag=1;
                break;
            }
        }
        
        if(flag==0)
            enrolledStudents.add(s);
        
    }
    
}
