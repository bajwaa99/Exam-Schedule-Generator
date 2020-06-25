package com.mycompany.datesheet;

import java.util.ArrayList;


public class Student {

    String name;

    int rollNo;

    int clashCount=0;

    int counting=0;

    ArrayList<Course> enrolledCourses=new ArrayList<>();

    
    Student(String string,int roll) {
       
        name=string;
        
        rollNo=roll;
    }
    
    void enrollCourse(Course c){
   
        int flag=0;
        
        for(int i=0;i<enrolledCourses.size();i++){
        
            if(enrolledCourses.get(i).name.equals(c.name)){
                flag=1;
                break;
            }
        }
        
        if(flag==0)
            enrolledCourses.add(c);
    }
    
}
