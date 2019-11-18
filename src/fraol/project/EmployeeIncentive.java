package fraol.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/*
*this class is used to facilitate incentive payment process within a month
*all cash paid within a month added each other and removed 
*from the file at the end of the month means they are paid with a salary
*this class extends Employee class in order to access registered employees
*/

public class EmployeeIncentive extends Employee{
    
    private String line = "";  //string holder for general purpose
    private File file;
    private Scanner input;
    private PrintWriter writer;
    private ArrayList<String> list = new ArrayList<String>();  //a container list for general purpose
           
    public void filterIncentives() throws IOException{
       /*clear out already paid incentives with in a month*/ 
         file = new File("incentive.txt");
         if(!file.exists())
            file.createNewFile();                         
         input = new Scanner(new FileInputStream(file));
             while(input.hasNext()){
               line = input.nextLine();
                String[] attributes = line.split(",");
                 if(Integer.parseInt(attributes[2]) == CurrentMonth() && Integer.parseInt(attributes[3]) == CurrentYear() ) {
                   list.add(line);
                 }else{
                 line = "";
               }
             }
         input.close();
         reWrite();       //re-write the filtered data to the file
         
     }
    
    private void reWrite() throws IOException{
      file = new File("incentive.txt");
      file.delete();
      file.createNewFile();
          writer = new PrintWriter(new FileOutputStream(file,true)); 
          if(!list.isEmpty()){
             for(int i=0 ; i<list.size() ; i++){
                writer.println(list.get(i));
             }
          }    
       list.removeAll(list);
       writer.close();
    }
    
    private int CurrentMonth(){
       Calendar cal = Calendar.getInstance();
       int month = cal.get(Calendar.MONTH); 
       
       return (month+1);  //since month is scaled from 0-11 in system
    }
    
    private int CurrentYear(){
       Calendar cal = Calendar.getInstance();
       int year = cal.get(Calendar.YEAR);
       
       return year;
    
    }
    
    public void addIncentives(int lineNo , String value) throws IOException{
        file = new File("incentive.txt");
         if(!file.exists())
           file.createNewFile();
        input = new Scanner(new FileInputStream(file));
        while(input.hasNext()){
            line = input.nextLine();
            list.add(line);
          }
      input.close();
      reAdd(lineNo, value);   //if there is existing incentive it add on it 
      
    }
    
    private void reAdd(int lineNo, String value) throws IOException{
      /*this method add new incentive or readd on existing incentive*/  
        
        file = new File("incentive.txt"); 
        file.delete();
        file.createNewFile();
        writer = new PrintWriter(new FileOutputStream(file,true));
           if(checkLine(lineNo)){
             incrementIncentive(lineNo, value);  //add on existing value
           }
           else{
             line = lineNo +","+ value +","+ String.valueOf(CurrentMonth()) +","+ String.valueOf(CurrentYear());
             list.add(line);
           }
        
        for(int i=0 ; i<list.size(); i++){
          writer.println(list.get(i));
        }   
      writer.close();
      
    }
    
    private boolean checkLine(int lineNo){
      /*checks whether a given line(Employee) is in the incentive list*/
        
        boolean check = false;
          for(int i=0 ; i<list.size() ;i++){
            String[] attributes = list.get(i).split(",");
            if(Integer.parseInt(attributes[0]) == lineNo ){
              check = true;
              break;
            }
        }
      
      return check;
      
    }
    
    private void incrementIncentive(int lineNo, String value){
      /*Increment an incentive on the existing incentive*/  
        for(int i=0 ; i<list.size(); i++){
           String[] attributes = list.get(i).split(",");
            if(Integer.parseInt(attributes[0]) == lineNo){
              double total = Double.parseDouble(attributes[1]) + Double.parseDouble(value);
              String total_ = String.valueOf(total);
              line = attributes[0] +","+ total_ +","+ attributes[2] +","+ attributes[3];
              list.set(i, line);
             break;
           }
        }    
    }
    
    public ArrayList<String> lineofIncentives() throws IOException{
      /*this method returns a list of paid employees with thier cash amount*/  
      
        ArrayList<String> lineList = new ArrayList<String>();
        file = new File("incentive.txt");
         if(!file.exists())
           file.createNewFile();
        input = new Scanner(new FileInputStream(file));
          while(input.hasNext()){
            line = input.nextLine();
            String[] attributes = line.split(",");
            lineList.add(nameList().get(Integer.parseInt(attributes[0])) + " ->->->         " + attributes[1]+"br");
          }
       input.close();
      return lineList;
    }
    
    public ArrayList<String> nameList() throws FileNotFoundException{
      /*the method helps us to fill the combox with the name of employees*/  
        readFromDB();
        list = getList();
         if(!list.isEmpty()){
           for(int i=0; i<list.size() ; i++){
             line = list.get(i);
             String[] attributes = line.split(",");
             list.set(i, attributes[0]);
           }
         }
       return list;
    }
    
    public void removeFromIncentive(int lineNo) throws IOException{
       /*This method delete person from incentive list
        *whenever we remove a person from file
        */
        file = new File("incentive.txt");
        if(!file.exists())
          file.createNewFile();
        input = new Scanner(new FileInputStream(file));
          while(input.hasNext()){
            line = input.nextLine();
            list.add(line);
          }
        for(int i=0 ; i<list.size() ; i++){
            String[] attributes = list.get(i).split(",");
              if(attributes[0].equals(String.valueOf(lineNo))){
                 list.remove(i);
              }
        }
        
        input.close();
        file.delete();
        file.createNewFile();
        writer = new PrintWriter(new FileOutputStream(file));
          for(String x:list){
            writer.println(x);
          }
        writer.close();
        
          
    }
    
    
}
