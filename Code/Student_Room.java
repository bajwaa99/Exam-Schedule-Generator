package com.mycompany.datesheet;

import java.util.List;



public class Student_Room {

    Room room;

    List<Student> students;

    // This function allots a room to students making sure another fitness condition
    //that number of allotted students should be less than capacity
    int add(Room room, Course c, int rem) {

        this.room = room;

        if(rem>=0){

            if(room.capacity >= c.enrolledStudents.size()){

                students=c.enrolledStudents;
                rem=c.enrolledStudents.size()-room.capacity;
            }
            else{

                int i=1;

                if(rem==0)
                    i=0;

                students = c.enrolledStudents.subList(rem-i, room.capacity-1);
                rem=c.enrolledStudents.size()-room.capacity;
            }
        }
        else{

            if(room.capacity+rem >= c.enrolledStudents.size()){

                students=c.enrolledStudents;
                rem=c.enrolledStudents.size()-room.capacity+rem;
            }
            else{

                students = c.enrolledStudents.subList((rem*-1), room.capacity-1);
                rem=c.enrolledStudents.size()-room.capacity+rem;
            }
        }

        return rem;
    }
    
}
