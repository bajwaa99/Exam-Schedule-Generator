package com.mycompany.datesheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 17L-4113,17L-6309
 */

public class Driver {
    
    public static int DAYS;
    
    public static int SLOTS;
    
    public static final int STUDENTS=50;//3169 //108   //50
    
    public static int COURSES=50;//223 //50 //50
    
    public static final int POPULATION_SIZE=500;
    
    public static int FITNESS_CRITERIA=0;   //represents the value of fitness above which the result is rejected
    
    
    //all arrays are loaded from files in this function
    private static void readingFiles(ArrayList<Day> days,ArrayList<Slot> slots,ArrayList<Room> rooms,
            ArrayList<Course> courses,ArrayList<Student> students){
        
        
        try  
        {  
        // reading file general.info
        // this file inputs the number of days and the slots per day for exam schedule
            
            File file = new File("C:\\Users\\bajwa\\Documents\\NetBeansProjects\\Datesheet\\Data\\general.info.txt");
            Scanner scanner = new Scanner(file);
            
            DAYS=scanner.nextInt();
            
            SLOTS=scanner.nextInt();

        }  
        catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        
        for(int i=0;i<DAYS;i++){
            days.add(new Day(i+1,"Day "+(i+1)));
        }
        
        for(int i=0;i<SLOTS;i++){
            slots.add(new Slot(i+1,"Slot "+(i+1)));
        }
        
    
        try  
        {  
            
        //reading file capacity.room
        //this file contains the capacities of all available rooms    
            
            File file = new File("C:\\Users\\bajwa\\Documents\\NetBeansProjects\\Datesheet\\Data\\capacity.room.txt");
            Scanner scanner = new Scanner(file);
            
            for(int i=0;scanner.hasNextInt();i++){
                rooms.add(new Room("Room "+i,scanner.nextInt()));
            }
              
        } catch (FileNotFoundException ex) {  
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
          
       
        for(int i=0;i<COURSES;i++){
            courses.add(new Course("Course "+(i+1),i+1));
        }
         
        for(int i=0;i<STUDENTS;i++){
            students.add(new Student("Student "+(i+1),i+1));
        }
        
        
        try  
        {  
        // reading file registration.data  
        // this file contains all students registered in specific courses
            
            File file = new File("C:\\Users\\bajwa\\Documents\\NetBeansProjects\\Datesheet\\Data\\registration.data2.txt");
            Scanner scanner = new Scanner(file);
            int i=0,j=0;
             
            while(scanner.hasNextInt()){
                if(j==STUDENTS){
                    i++;
                    j=0;
                }
                int val=scanner.nextInt();
                if(val==1){
                    courses.get(i).enrollStudent(students.get(j));
                    students.get(j).enrollCourse(courses.get(i));
                }
                j++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        for(int i=0;i<courses.size();i++){
            if(courses.get(i).enrolledStudents.isEmpty()){
                courses.remove(i);
                i--;
            }
        }
        COURSES=courses.size();

    }
    
    private static Generation initialize(ArrayList<Course> courses, 
            ArrayList<Day> days, ArrayList<Slot> slots){
        Generation generation=new Generation();
        Random rand=new Random();
         
        for(int a=0;a<POPULATION_SIZE;a++){
                    
                    Chromosome c=new Chromosome();

                    c.index=a;

                    ArrayList<Course> remaincourses=new ArrayList<>();

                    for(int i=0;i<COURSES;i++){
                        remaincourses.add(new Course(courses.get(i)));
                    }
                    
                    for(int i=0;i<COURSES;i++){

                        int random1= rand.nextInt(DAYS);

                        int random2= rand.nextInt(SLOTS);

                        int random3= rand.nextInt(remaincourses.size());

                        remaincourses.get(random3).day=days.get(random1);

                        remaincourses.get(random3).slot=slots.get(random2);

                        c.addCourse(remaincourses.get(random3));

                        remaincourses.remove(random3);

                    }

                    generation.chromosomes.add(c);
                
                }
        return generation;
    }
    
    private static Generation mutation(Generation generation, ArrayList<Day> days, ArrayList<Slot> slots,ArrayList<Course> courses){
        //this function randomly selectes courses from a complete generation of chromosomes
        // and performs mutation by changing their days and slots.
        
        Random rand=new Random();
        
        for(int i=0;i<POPULATION_SIZE;i++){
            
            Course c = generation.chromosomes.get(i).removeCourse(courses.get(rand.nextInt(courses.size())).name);
            c.day=days.get(rand.nextInt(DAYS-1));
            c.slot=slots.get(rand.nextInt(SLOTS-1));

                if(generation.chromosomes.get(i).table[c.day.index-1][c.slot.index-1]==null){
                    generation.chromosomes.get(i).table[c.day.index-1][c.slot.index-1]=new Data();
                }
        
                generation.chromosomes.get(i).table[c.day.index-1][c.slot.index-1].courses.add(c);
        }
        return generation;
    }
    
    public static void main(String[] args) {
        
        
        ArrayList<Day> days=new ArrayList<>();
        
        ArrayList<Slot> slots=new ArrayList<>();
        
        ArrayList<Room> rooms=new ArrayList<>();
        
        ArrayList<Course> courses=new ArrayList<>();
        
        ArrayList<Student> students=new ArrayList<>();
        
        // this function loads all the files into the arrays
        readingFiles(days,slots,rooms,courses,students);
        
       
        
        int totalGenerations=1;
        
        Generation generation = initialize(courses,days,slots);
                
        Chromosome result=null;
        Random rand=new Random();
            
        Chromosome bestChromosome=generation.chromosomes.get(0);
        
        int maxRoomCap=0; // maximum capacity of all rooms
        
        for(int i=0;i<rooms.size();i++)
            maxRoomCap +=rooms.get(i).capacity;
        
        while(true){
            
            //fitness of the complete population is calculated by this function
            generation.fitnessCalculation(students,maxRoomCap);
                
            result=generation.getMinFitness();
            
            if(result.fitnessValue<bestChromosome.fitnessValue)
                bestChromosome=result;
                
            System.out.println("Total Generations="+totalGenerations+"\n");
                
            if(bestChromosome.fitnessValue <= FITNESS_CRITERIA){
                break;
            }
                
            //best two chromosomes are selected for crossover by this function
            ArrayList<Integer> chromos = generation.chromosomesSelection();
                
            //new generation of population is returned after crossover by this function
            generation = crossover(generation.chromosomes.get(chromos.get(0)),generation.chromosomes.get(chromos.get(1)),courses);

            float p = rand.nextFloat();
                
            if(p>0.9){

                //if crossover has occurred REINITIALIZE times and no result obtained then do mutation
                generation = mutation(generation,days,slots,courses);
               
                //After every mutation, the FITNESS_CRITERIA is incremented by a certain factor to ensure flexibility.
                FITNESS_CRITERIA*=2;

            }
             if(FITNESS_CRITERIA==0)
                    FITNESS_CRITERIA=COURSES;
             
            totalGenerations++;

        }


        if(result!=null){
            System.out.println("\n\n______________________________________________________________________________________"
                + "____________________________\n_________________________________________________RESULT__________________"
                    + "_________________________________________\n");

           // result.print();   //this function print the result in simple order
           System.out.println("Total Generations="+totalGenerations);
           bestChromosome.printing(days,slots); //this functions print the result in datesheet order
           
        }
        
    }

    private static Generation crossover(Chromosome chromo1, Chromosome chromo2, ArrayList<Course> courses) {
       //random courses are selected from both chromosomes and a new generation of 
       //chromosomes are generated from them
       
        Random rand=new Random();
        
        Generation generation= new Generation();
        
        for(int a=0;a<POPULATION_SIZE;a++){
        
            Chromosome c=new Chromosome();
            
            c.index=a;
            
            int random1= rand.nextInt(COURSES);
            
            //remainIndex contains indices of the courses which are to be inherited from
            //first chromosome into the next generation
            ArrayList<Integer> remainIndex=new ArrayList<>();
            
            for(int i=0;i<random1;i++){
            
                int random2= rand.nextInt(COURSES);
                
                if(!remainIndex.contains(random2)){
                
                    remainIndex.add(random2);
                    
                    Course course = chromo1.findCourse(courses.get(random2).name);
                    
                    if(course !=null)
                        c.addCourse(course);
                }
                else{
                    i--;
                }
            }
          
            //the indices of courses not selected from first chromosome are selected from
            //the second one to be inherited in the next generation
            for(int i=0;i<COURSES;i++){
            
                if(!remainIndex.contains(i)){
                
                    Course course = chromo2.findCourse(courses.get(i).name);
                    
                    if(course !=null)
                        c.addCourse(course);
                }
            }
            
            generation.chromosomes.add(c);
        }
        
        return generation;
    }
}
