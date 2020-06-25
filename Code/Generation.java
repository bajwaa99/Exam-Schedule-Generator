package com.mycompany.datesheet;

import static com.mycompany.datesheet.Driver.DAYS;
import static com.mycompany.datesheet.Driver.FITNESS_CRITERIA;
import static com.mycompany.datesheet.Driver.SLOTS;
import java.util.ArrayList;



public class Generation {

    ArrayList<Chromosome> chromosomes=new ArrayList<>();
    
    //This function returns the number of occurrences of a student in the array of students giving exam 
    //on a same day and slot. The value is used in fitness calculation
    int findingMatch1(ArrayList<Student> fMatch,Student s){

        int count=0;

        for(int b=0;b<fMatch.size();b++){
        
            if(s.rollNo==fMatch.get(b).rollNo)
                count++;
        }
        
        return count;
        
    }
    
    void fitnessCalculation(ArrayList<Student> students, int maxRoomCap){
       
        for(int a=0;a<chromosomes.size();a++){

            for(int i=0;i<DAYS;i++){
                
                //This initializes the number of clashes of all students 
                for(int z=0;z<students.size();z++){
                    students.get(z).counting=0;
                }
                
                for(int j=0;j<SLOTS;j++){
                
                    if(chromosomes.get(a).table[i][j]!=null){
                
                        int roomCapacity=0;
                        
                        //This checks the fitness condition that no student can have more than 2 exams on a single slot
                        for(int k=0; k<chromosomes.get(a).table[i][j].courses.size();k++){

                            for(int m=0;m<chromosomes.get(a).table[i][j].courses.get(k).enrolledStudents.size();m++){
                           
                                roomCapacity+=chromosomes.get(a).table[i][j].courses.get(k).enrolledStudents.size();
                              
                                if(roomCapacity > maxRoomCap){

                                    int val=roomCapacity-maxRoomCap;
                                    chromosomes.get(a).fitnessValue+=val;

                                    break;
                                }

                                if(findingMatch1(chromosomes.get(a).table[i][j].students,chromosomes.get(a).table[i][j].courses.get(k).enrolledStudents.get(m))>2){
                        
                                    chromosomes.get(a).addClashCourse(chromosomes.get(a).table[i][j].courses.get(k));
                                    chromosomes.get(a).fitnessValue++;
                                }
                            }
                        }
                        
                        boolean flag=false;
                       //This checks two conditions of fitness given below.
                        for(int k=0; k<students.size();k++){
                        
                            for(int m=0;m<chromosomes.get(a).table[i][j].courses.size();m++){
                                
                                if(chromosomes.get(a).table[i][j].courses.get(m).enrolledStudents.contains(students.get(k))){
                                    
                                    students.get(k).counting++;
                            
                                    if(students.get(k).counting>3){// Fitness condition 1: No student can have more than 3 exams on a day
                                    
                                        chromosomes.get(a).addClashCourse(chromosomes.get(a).table[i][j].courses.get(m));
                                        chromosomes.get(a).fitnessValue++;
                                    }
                              
                                    if(students.get(k).counting>2 && m>0 && chromosomes.get(a).clashCourse.contains(chromosomes.get(a).table[i][j].courses.get(m-1))){
                                        //Fitness condition 2: No student can have more than 2 exams on a consecutive slot
                                    
                                        chromosomes.get(a).addClashCourse(chromosomes.get(a).table[i][j].courses.get(m));
                                        chromosomes.get(a).fitnessValue++;
                                    }
                                    
                                    flag=true;
                                }
                                
                                if(flag==true)
                                    break;
                            }
                            
                            if(flag==true)
                                 break;
                        
                        }
                    }
                }
            }
        }
    }

    //This function selects the best two fitness value chromosomes for the crossover
    ArrayList<Integer> chromosomesSelection() {
        int index1=0,index2=1;
        int val1=Integer.MAX_VALUE,val2=Integer.MAX_VALUE;
       
        for(int i=0;i<chromosomes.size();i++){
        
            if(chromosomes.get(i).fitnessValue<val1){
                index1=i;
                val1=chromosomes.get(i).fitnessValue;
            }
            else if(chromosomes.get(i).fitnessValue<val2){
                index2=i;
                val2=chromosomes.get(i).fitnessValue;
            }
        }
        ArrayList<Integer> indices=new ArrayList<>();
        indices.add(index1);
        indices.add(index2);
        return indices;
    }

    //This function checks the whole population and returns the best fit chromosome whose fitness
    //criteria is met
    Chromosome criteriaMet() {
       
        int min=FITNESS_CRITERIA;
        
        int index=0;
        
        for(int i=0;i<chromosomes.size();i++)
        
            if(chromosomes.get(i).fitnessValue < min){
            
                min=chromosomes.get(i).fitnessValue;
                index=i;
            }
        
        if(min>=FITNESS_CRITERIA)
            return null;
        
        return chromosomes.get(index);
        
    }
    
    Chromosome getMinFitness(){
        int min=Integer.MAX_VALUE;
        Chromosome c=null;
        for(int i=0;i<chromosomes.size();i++)
            if(chromosomes.get(i).fitnessValue < min){
                min=chromosomes.get(i).fitnessValue;
                c=chromosomes.get(i);
            }
                
        return c;
    
    }
}
