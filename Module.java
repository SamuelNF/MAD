/*
Module class takes a string, writes the string to file, inputs the
file into Clingo, and stores the results. Uses the engine.lp script
to capture Assumption-based argumentation reasoning, with assumptions
being denoted as being part of a admissible set with the "in(<ass>)"
phrase.
*/

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.file.*;

class Module
{
   private List<List<String>> admissible = new ArrayList<List<String>>();
   private List<List<String>> preferred = new ArrayList<List<String>>();
   private List<String> credulous = new ArrayList<String>();
   private List<String> sceptical = new ArrayList<String>();
   private String name;

   private static String testString =
   "%%BC%%\n"+
   "ass(barren).\n"+
   "ass(nonbarren).\n"+
   "in(price(800)):-in(pregnant).\n"+
   "in(nonbarren):-in(pregnant).\n"+
   "in(price(80)):-in(barren).\n"+
   "contrary(barren,nonbarren).";

   public static void main(String[] args)
   {
      Module module = new Module("Test", testString);
      module.displayResults();
   }

   public String name()
   {
      return new String(name);
   }

   public Module(String name, String string)
   {
      this.name = name;
      writeFile(string);
      readFile();
      deriveResults();
   }

   private void writeFile(String fString)
   {
      Path file = Paths.get("./modules/"+name+".lp");
      String writeString = "#include \"engine.lp\".\n" + fString;
      try
      {
         Files.write(file, writeString.getBytes());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public List<List<String>> getPreferred()
   {
      List<List<String>> copy = new ArrayList<List<String>>();
      copy.addAll(preferred);
      return copy;
   }

   public List<String> getCredulous()
   {
      List<String> copy = new ArrayList<String>();
      copy.addAll(credulous);
      return copy;
   }

   public List<String> getSceptical()
   {
      List<String> copy = new ArrayList<String>();
      copy.addAll(sceptical);
      return copy;
   }

   private void readFile()
   {
      Runtime rt = Runtime.getRuntime();
      Process pr = null;
      try
      {
         pr = rt.exec("clingo ./modules/"+name+".lp 0");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

      BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line = null; 
      Pattern pattern = Pattern.compile("in\\(.*?\\).|in\\(.*?\\)");

      try
      {
         while ((line = input.readLine()) != null)
         {
            if(line.startsWith("Answer"))
            {
               List<String> set = new ArrayList<String>();
               line = input.readLine();
              // System.out.println("Admissible set found");
               Matcher matcher = pattern.matcher(line);
               while (matcher.find())
               {    
                  String assumption = matcher.group(0);
              //    System.out.println("Found one "+assumption);
                  assumption = assumption.replace(" ","");
                  assumption = assumption.substring(assumption.indexOf('(')+1, assumption.length() - 1);
                  set.add(assumption);
               }
               admissible.add(set);
            }
         }
      } 
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   private void deriveResults()
   {
      derivePreferred();
      deriveCredulous();
      deriveSceptical();
   }

   private void deriveCredulous()
   {
      for(List<String> set : preferred)
      {
         for(String assumption : set)
         {
            if(!credulous.contains(assumption))
            {
               credulous.add(assumption);
            }
         }
      }
   }

   private void deriveSceptical()
   {
      int memberNum;
      for(String assumption : credulous)
      {
         memberNum = 0;
         for(List<String> set : preferred)
         {
            if(set.contains(assumption)) memberNum++;
         }
         if(memberNum == preferred.size())
         {
            sceptical.add(assumption);
         }
      }
   }

   private void derivePreferred()
   {
      for(List<String> set_1 : admissible)
      {
         boolean subSet = false;
         for(List<String> set_2 : admissible)
         {
            if(set_2.containsAll(set_1) && (set_2.size()>set_1.size()))
            {
               subSet = true;
            }
         }
         if(!subSet) preferred.add(set_1);
      }
   }

   private String setToString(List<String> set)
   {
      boolean first = true;
      String expression = "{";
      for(String ass : set)
      {
         if(!first) expression = expression + ", ";
         expression = expression + ass;
         first = false;
      }
      expression = expression + "}";
      return expression;
   }
         
   public void displayResults()
   {
      System.out.println("\nModule: "+name+"\n-------------------------------");
      System.out.println("Admissible sets:");
      for(List<String> set : admissible)
      {
         System.out.println(setToString(set));
      }

      System.out.println("\nPreferred sets:");
      for(List<String> set : preferred)
      {
         System.out.println(setToString(set));
      }

      System.out.println("\nCredulous assumptions:");
      System.out.println(setToString(credulous));

      System.out.println("\nSceptical assumptions:");
      System.out.println(setToString(sceptical));
      System.out.println("");
   }
}
