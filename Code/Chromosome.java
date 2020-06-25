package com.mycompany.datesheet;

import static com.mycompany.datesheet.Driver.DAYS;
import static com.mycompany.datesheet.Driver.SLOTS;
import java.util.ArrayList;



public class Chromosome {

    int index;

    Data[][] table=new Data[DAYS][SLOTS];

    //This array contains the list of Courses where clashes have occurred. It will be used in crossover
    ArrayList<Course> clashCourse=new ArrayList<>();

    int fitnessValue=0;
    
    //This function adds course in the table at specific day and slot
    void addCourse(Course c){
       
        if(table[c.day.index-1][c.slot.index-1]==null){
            table[c.day.index-1][c.slot.index-1]=new Data();
        }
        
        table[c.day.index-1][c.slot.index-1].courses.add(c);
        
        for(int i=0;i<c.enrolledStudents.size();i++){
            table[c.day.index-1][c.slot.index-1].students.add(c.enrolledStudents.get(i));
        }
        
    }
    
    //This function adds the courses in clashCourse array if not already present
    void addClashCourse(Course c){
     
        int flag=0;
        
        for(int i=0;i<clashCourse.size();i++){
        
            if(clashCourse.get(i).name.equals(c.name))
                flag=1;
        
        }
        
        if(flag==0){
            clashCourse.add(c);
        }
    }
    
    Course removeCourse(String name){
        
        for(int i=0;i<DAYS;i++){
         
            for(int j=0;j<SLOTS;j++){
                
                if(table[i][j]!=null){
                
                    for(int k=0;k<table[i][j].courses.size();k++)
                        if(table[i][j].courses.get(k).name.equals(name))    
                            return table[i][j].courses.remove(k);
                }
            }
        }
        
        return null;
    }
    
    Course findCourse(String name){
      
        for(int i=0;i<DAYS;i++){
         
            for(int j=0;j<SLOTS;j++){
             
                if(table[i][j]!=null){
                    
                    for(int k=0;k<table[i][j].courses.size();k++)
                        if(table[i][j].courses.get(k).name.equals(name))    
                            return table[i][j].courses.get(k);
                        
                }
            }
        }
        
        return null;
    }
    
    //This function prints the chromosome normally
    void print(){
        
        System.out.println("Fitness= "+fitnessValue);
        
        for(int i=0;i<DAYS;i++){
        
            int flag=1;
            
            for(int j=0;j<SLOTS;j++){
            
                if(table[i][j]!=null){
                
                    if(flag==1){
                        System.out.println("_____________\nDay is= "+ table[i][j].courses.get(0).day.name);
                        flag=0;
                    }
                    
                    System.out.println("Slot is= "+ table[i][j].courses.get(0).slot.name);
                    
                    System.out.println("Courses are= ");
                        
                    for(int a=0;a<table[i][j].courses.size();a++)
                        System.out.println(table[i][j].courses.get(a).name);
                        
                    System.out.println();

                }
            }
        }
    }

    //This function allots rooms to students giving exam on a certain day and slot
    void AllotRooms(ArrayList<Room> room) {
       
        int rIndex=-1;
       
        for(int i=0;i<DAYS;i++){
        
            for(int j=0;j<SLOTS;j++){
            
                if(table[i][j]!=null){
                
                    int flag=0;
                    
                    for(int k=0;k<table[i][j].courses.size();k++){
                    
                        int remainder = 0;
                        
                        do{

                            rIndex++;
                        
                            Student_Room r = new Student_Room();
                            
                            if(flag==1)
                                remainder*=-1;
                            
                            remainder = r.add(room.get(rIndex), table[i][j].courses.get(k), remainder);
                            
                            table[i][j].student_room.add(r);
                            
                            if(remainder<0){
                            
                                rIndex--;
                                remainder=0;
                                flag=1;
                            }
                            
                        }while(remainder>0);
                    }
                }
            }
        }
    }

    //This function prints the chromosome in datesheet style
    void printing(ArrayList<Day> days, ArrayList<Slot> slots){
        
        System.out.println("Fitness= "+fitnessValue);
        
        System.out.print("\t");
       
        for(int i=0;i<SLOTS;i++)
            System.out.print(slots.get(i).name+"\t\t");
        
        System.out.println();
        
        for(int i=0;i<DAYS;i++){
            System.out.print("____________________________________________________________"
                    + "_________________________________________________________\n" + days.get(i).name );
            int[] printarray=new int[SLOTS];
            for(int a=0;a<SLOTS;a++)
                printarray[a]=0;
            boolean flag=true;
            while(flag){
                for(int j=0;j<SLOTS;j++){
                    if(table[i][j]==null){
                        System.out.print("\t\t");
                    }
                    else{
                        if(printarray[j] < table[i][j].courses.size()){
                            System.out.print("\t"+table[i][j].courses.get(printarray[j]).name);
                            printarray[j]++;
                        }
                        else{
                                System.out.print("\t\t");
                        }
                    }
                    if(j==SLOTS-1){
                        flag=false;
                        for(int z=0;z<SLOTS;z++){
                            if(table[i][z]!=null && table[i][z].courses.size()!= printarray[z]){
                                flag=true;
                                break;
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
    }
}
