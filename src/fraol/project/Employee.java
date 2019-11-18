package fraol.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Fraol
 */
public class Employee {
       
     private String line = "";
     private String Salary = "";
     private int lineNumber;       //holds the exact place of a person
     private ArrayList<String> list = new ArrayList<String>(); //a container list for general purpose
     private File file;                  //declaring file object
     private PrintWriter writer;         //to write to a file
     private Scanner input;              //to read from a file
       
       
     public void addToMainDB(String Name ,String Sex ,String Address ,String jobType) throws FileNotFoundException{
          file = new File("mainDB.txt");
          writer = new PrintWriter(new FileOutputStream(file,true));
              line = Name +","+ Sex +","+ Address +","+ jobType +","+ Salary;
              writer.println(line);
          writer.close();
     }
       
     public void addToExtra(String Name ,String age ,String payRange ,String phoneNo) throws FileNotFoundException{
          file = new File("extra.txt");
          writer = new PrintWriter(new FileOutputStream(file,true));
              line = Name +","+ age +","+ payRange+"br" +","+ phoneNo +","+ Currentdate();
              writer.println(line);
          writer.close();
     }
       
     public void calculateSalary(String workHour , String payRange){
          double range = Double.parseDouble(payRange);
          double work_Hour = Double.parseDouble(workHour);
          double totalSalary = range*work_Hour*30;
          double tax = 0;
            if(totalSalary >= 9000)
               tax =totalSalary*0.37;   
            else if(totalSalary >= 6000 && totalSalary < 9000)
               tax = totalSalary*0.35;
            else if(totalSalary >= 4000 && totalSalary < 6000)
               tax = totalSalary*0.30;
            else if(totalSalary >= 3000 && totalSalary < 4000)
               tax = totalSalary*0.25;
            else if(totalSalary >= 2000 && totalSalary < 3000)
               tax = totalSalary*0.25;
            else if(totalSalary >= 1500 && totalSalary < 2000)
               tax = totalSalary*0.15;
            else if(totalSalary >= 1000 && totalSalary < 1500)
               tax = totalSalary*0.1;
            else if(totalSalary >= 700 && totalSalary < 1000)
               tax = totalSalary*0.05;
            else
               tax = 0;   
           
          double netSalary = totalSalary - tax;
          Salary = String.valueOf(netSalary);  
     }
       
     public String getSalary(){
        return Salary;
     }
       
     private String Currentdate(){
        String currentDate;
        String AmPm = "";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
           int day_ = Calendar.DAY_OF_MONTH;
           int month_ = cal.get(Calendar.MONTH);
           int year_ = cal.get(Calendar.YEAR);
           String day = String.valueOf(day_);
           String month = String.valueOf(month_+1); //since month is valued from 0-11
           String year = String.valueOf(year_);
           String hours = String.valueOf(date.getHours()%12); //change the time format to 12hrs
           String minutes = String.valueOf(date.getMinutes());
           if(minutes.length() == 1){
            minutes = "0"+minutes;
           }
           if(date.getHours()>12){
            AmPm = "PM";
           }
           else{
            AmPm = "AM";
           }
         
           currentDate = day+"/"+month+"/"+year+ " " + "at " + hours+":"+minutes +" "+AmPm;  
        return currentDate;
     }
       
     public void readFromDB() throws FileNotFoundException{
         file = new File("mainDB.txt");
         input = new Scanner(new FileInputStream(file));
         list.removeAll(list);
           while(input.hasNext()){
              line = input.nextLine();
              list.add(line);
           }
         input.close();
     }
       
     public void readFromExtra() throws FileNotFoundException{
         file = new File("extra.txt");
         input = new Scanner(new FileInputStream(file));
         list.removeAll(list);
           while(input.hasNext()){
              line = input.nextLine();
              list.add(line);
           }
        input.close();
     }
       
     public ArrayList<String> searchPerson(String name) throws FileNotFoundException{
         file = new File("mainDB.txt");
         input = new Scanner(new FileInputStream(file));
           while(input.hasNext()){
             line = input.nextLine();
             String[] attributes = line.split(",");
               if(attributes[0].equalsIgnoreCase(name) || 
                   attributes[0].toLowerCase().startsWith(name.toLowerCase())){
                 list.add(line);
               }
            }
          
        return list;
      }
       
     public ArrayList<String> getList(){
         return list;
     }
       
     public void deletePerson(String removeline) throws IOException{
          file = new File("mainDB.txt");
          File copyFile = new File("copyDB.txt");
          input = new Scanner(new FileInputStream(file));
          writer = new PrintWriter(new FileOutputStream(copyFile,true));
          int count=0;
          while(input.hasNext()){
             line = input.nextLine();
               if(!line.equalsIgnoreCase(removeline)){
                 writer.println(line);
                }
               else{
                 lineNumber = count;
                 line = "";
               }
             count++; 
           }
        writer.close();
        input.close();
        file.delete();
        copyFile.renameTo(file);           //rename the temporary file to mainDB.txt
        deletePersonFromExtra(lineNumber); //remove the person from extra 
        new EmployeeIncentive().removeFromIncentive(lineNumber);
     }
       
     private void deletePersonFromExtra(int lineNo) throws FileNotFoundException{
          file = new File("extra.txt");
          File copyFile = new File("cpyExtra.txt");
          input = new Scanner(new FileInputStream(file));
          writer = new PrintWriter(new FileOutputStream(copyFile,true));
          int Line = 0;
              while(input.hasNext()){
                 line = input.nextLine();
                 if(Line != lineNo){
                   writer.println(line);
                 }
                 else{
                   line = "";
                 } 
                Line++;    
               }
            writer.close();
            input.close();
            file.delete();
            copyFile.renameTo(file);
              
     } 
       
     public void formatDatabase() throws IOException{
          file = new File("mainDB.txt");
          file.delete();         //delete file
          file.createNewFile();  //recreate the file
          formatExtra();         //the same for extra.txt
     }
       
     private void formatExtra() throws IOException{
          File file_ = new File("extra.txt");
          file_.delete();
          file_.createNewFile();
     }
       
     public void storeInformation(String Companyname,String address,String website) throws FileNotFoundException{
          file  = new File("companyInfo.dat");
          writer = new PrintWriter(new FileOutputStream(file));
            line = Companyname + "," + address + "," + website;
            writer.println(line);
         writer.close();
     }
       
     public void storePassword(String Admin ,String Password) throws FileNotFoundException{
         file = new File("pass.dat");
         writer = new PrintWriter(new FileOutputStream(file));
           line = Admin + "," + Password;
           writer.println(line);
         writer.close();
     }
       
     public String getInformation() throws IOException{
          file = new File("companyInfo.dat"); 
          line = "";
          if(!file.exists())                 //creating the file if no exists
             file.createNewFile();
          input = new Scanner(new FileInputStream(file));
           while(input.hasNext()){
             line = input.nextLine();
           }
          input.close();
          return line;
     }
       
     public String getPassword() throws IOException{
          file = new File("pass.dat");
          line = "";
          if(!file.exists())
            file.createNewFile();
          input = new Scanner(new FileInputStream(file));
           while(input.hasNext()){
             line = input.nextLine();
           }
          input.close();
          return line;
     }
       
     public String[] readLineFromExtra(String givenLine) throws FileNotFoundException{
          String[] attributes = new String[5];
          int Line = 0;
          int correctLine = getCorrectLine(givenLine);
          file = new File("extra.txt");
          input = new Scanner(new FileInputStream(file));
           while(input.hasNext()){
             line = input.nextLine();
               if(Line == correctLine){
                 attributes = line.split(",");
                 break;
               }
             Line++; 
           }
        input.close();
        return attributes;
     }
       
     private int getCorrectLine(String requiredLine) throws FileNotFoundException{
         int count =0;
         file = new File("mainDB.txt");
         input = new Scanner(new FileInputStream(file));
           while(input.hasNext()){
             line = input.nextLine();
               if(line.equalsIgnoreCase(requiredLine)){
                  break;
               }
              count++; 
           }
          input.close();
         return count;  
     }
       
     public void UpdateMainDB(String oldData,String NewData,String ExtraData) throws FileNotFoundException{
          file = new File("mainDB.txt");
          File copyfile = new File("copy.txt");
          input = new Scanner(new FileInputStream(file));
          writer = new PrintWriter(new FileOutputStream(copyfile, true));
          int count=0; //count line number
           while(input.hasNext()){
             line = input.nextLine();
              if(oldData.equalsIgnoreCase(line)){
                 line = NewData + Salary;
                 lineNumber = count; //save line number
              }
             writer.println(line);
             count++; 
           }
         input.close();
         writer.close();
         file.delete();
         copyfile.renameTo(file);
         UpdateExtra(lineNumber, ExtraData);
     }
       
     private void UpdateExtra(int lineNo ,String newData) throws FileNotFoundException{
          /*format the extra.txt textfile 
            by saving each line on the list
           */
          try {
             cleanTextfile();
          } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Data fetch error","IO error",JOptionPane.ERROR_MESSAGE); 
          }
         file = new File("extra.txt");
         writer = new PrintWriter(new FileOutputStream(file, true));
           for(int i=0 ; i<list.size() ; i++){
              line = list.get(i);
                if( i == lineNo){
                  String[] attributes = line.split(",");
                  line = newData + attributes[4]; //can't update the registration date
                }
              writer.println(line); 
            }      
         writer.close();
     }
       
     private void cleanTextfile() throws IOException{
          file = new File("extra.txt");
          input = new Scanner(new FileInputStream(file));
          list.removeAll(list);
            while(input.hasNext()){
              line = input.nextLine();
              list.add(line); 
            }
          input.close();
          file.delete();
          file.createNewFile();
          
     }
       
     public ArrayList<String> sortData(String key) throws FileNotFoundException{
        list.removeAll(list); //clean the container
        ArrayList<String> names = new ArrayList<String>();
        /*Sort either the main display or the extra display
          based on the given key value
        */
        if(key.equalsIgnoreCase("extra")){
           file = new File("extra.txt");
           input = new Scanner(new FileInputStream(file));
             while(input.hasNext()){
               line = input.nextLine();
               String[] attributes = line.split(",");
               names.add(attributes[0]);
               list.add(line);
             } 
            input.close();
            /*Now sort the names in A-Z format*/
             for(int i=0 ; i<names.size()-1 ; i++){
              for(int j=i+1 ; j<names.size() ; j++){
                 if(0 > names.get(j).compareToIgnoreCase(names.get(i))){
                   String temp = names.get(i);
                   names.set(i,names.get(j));
                   names.set(j, temp);
                 } 
               }
             } 
            /*Now add the old elements to the sorted name*/
             for(int i=0 ; i<names.size() ; i++){
              for(int j=0 ; j<list.size() ; j++){
                 if(list.get(j).startsWith(names.get(i))){
                  names.set(i,list.get(j).replaceFirst(names.get(i), names.get(i)));
                  list.remove(j);
                 } 
               }
             } 
        
        }else{
          file = new File("mainDB.txt");
          input = new Scanner(new FileInputStream(file));
            while(input.hasNext()){
               line = input.nextLine();
               String[] attributes = line.split(",");
               names.add(attributes[0]);
               list.add(line);
            } 
            input.close();
            /*Now sort the names in A-Z format*/
            for(int i=0 ; i<names.size()-1 ; i++){
              for(int j=i+1 ; j<list.size() ; j++){
               if(0 > names.get(j).compareToIgnoreCase(names.get(i))){
                   String temp = names.get(i);
                   names.set(i,names.get(j));
                   names.set(j, temp);
                } 
              }
            } 
           /*Now add the old elements to the sorted name*/ 
            for(int i=0 ; i<names.size() ; i++){
              for(int j=0 ; j<list.size() ; j++){
                if(list.get(j).startsWith(names.get(i))){
                  names.set(i,list.get(j).replaceFirst(names.get(i), names.get(i)));
                  list.remove(j);
                } 
              }
            } 
    
        }
           
        return names;
     }
       
     public int EmployeeSize() throws FileNotFoundException{
          readFromDB();
          int size = list.size();
          list.removeAll(list);
          return size;
     } 
      
     public ArrayList<String> filterByjob() throws FileNotFoundException{
        file = new File("mainDB.txt");
        input = new Scanner(new FileInputStream(file));
        ArrayList<String> filteredList = new ArrayList<String>();
        int count = 0;
           while(input.hasNext()){
              line = input.nextLine();
              String[] attributes = line.split(",");
              list.add(attributes[3]); //add jobTypes to the list
           }
         if(!list.isEmpty()){ 
             for(int i=0 ; i<list.size() ; i++){
                 String temp = list.get(i);
                   if(!filteredList.contains(temp)){
                      filteredList.add(temp);  //add single jobType with out duplication
                   }                                                                
                } 
             }
             
             for(int i=0 ; i<filteredList.size() ;i++){
                String temp = filteredList.get(i);
                 for(int j=0 ; j<list.size() ; j++){
                    if(list.get(j).equalsIgnoreCase(temp)){
                      count++;  //if same jobfield is found it counts
                    } 
                 }
               filteredList.set(i, String.valueOf(count) +"->->            "+ temp);  //updating with number and jobtype
               count = 0;                                                             // reset the counter
             }
         input.close();
         return filteredList; 
     }
    
}

      
