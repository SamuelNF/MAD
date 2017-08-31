/*
Framework class takes modules as strings and compiles them into Module objects.
Handles calls to the results of other modules recursively, by attempting to 
compile called modules first (this can cause a loop if the modules include 
circular calls). 
*/

import java.io.*;
import java.util.*;
import java.util.regex.*;

class Framework
{
   private Map<String,String> modules = new LinkedHashMap<String,String>();

//Test module strings
   private static String t1 =
   "%%test1%%\n"+
   "ass(a).\n"+
   "ass(b).\n"+
   "contrary(a,b).\n"+
   "in(a):-call{test2+d,[c],sk}.\n";

   private static String t2 =
   "%%test2%%\n"+
   "ass(a).\n"+
   "ass(b).\n"+
   "contrary(a,b).\n"+
   "in(c):-in(d).\n";

//Returns module name from string
   private String name(String module)
   {
      return module.split("%%")[1];
   }

//Returns string from module
   
   public String module(String name)
   {
      return modules.get(name);
   }

//Adds string to module map
   public void addModString(String name, String string)
   {
      modules.put(name,string);
   }      

//Compiles and prints all module results
   public void getAllResults()
   {
      for(String module: modules.keySet())
      {
         getResults(module);
      }
   }

//Compiles and prints named module results
   public void getResults(String name)
   {
      try
      {
         Module m = compileModule(modules.get(name));
         m.displayResults();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

//Checks module for module calls
   static public boolean containsCall(String module) throws Exception, Error
   {
      Pattern pattern = Pattern.compile("call\\{.*?\\}");
      Matcher matcher = pattern.matcher(module);
      if(matcher.find()) return true;
      else return false;
   }

//Resolves module calls and returns compiled Module object
   public Module compileModule(String module) throws Exception, Error
   {
      Module m;
      while(containsCall(module))
      {
         
         Call call = getCall(module);
         module = resolveCall(module, call);
      }
      m = new Module(name(module), module);
      m.displayResults();
      return m;
   }

//Returns first call string found in module
   static public Call getCall(String module)
   {
      Pattern pattern = Pattern.compile("call\\{.*?\\}");
      Matcher matcher = pattern.matcher(module);
      if(matcher.find())
      {
         return new Call(matcher.group(0));
      }
      else
      {
         return null;
      }
   }

//Creates Module object for target of call, and resolves call based on results
   private String resolveCall(String module, Call call)
   {
      if(call==null) return module;

      String callMod = modules.get(call.module);

      if(call.union!=null)
      {
         callMod = callMod + "in("+ call.union +").\n";
      }
      Module m = null; 
      try
      {
         m = compileModule(callMod);

         if(call.containsVar)
         {
            module = resolveVars(module, m, call);
         }
         else
         {
            if(callSucceeds(m, call))
            {
               System.out.println("CALL SUCCESS - "+call.callString);
               module = affirmCall(module, call);
            }
            else
            {
               System.out.println("CALL FAILED - "+call.callString);
               module = removeCall(module, call);
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return module;
   }

//Resolves call containing free variables
   private String resolveVars(String s, Module m, Call c)
   {
      HashMap<String,String> vars = getVarValues(m,c);
   //   System.out.println(vars);
      s = groundVars(s, vars, c);
      return s;
   }

//Replaces call with variables with variable assignments
   private String groundVars(String s, HashMap<String,String> vars, Call c)
   {
      String assignments = "";
      boolean first = true;
      for (HashMap.Entry<String, String> entry : vars.entrySet()) 
      {
         String key = entry.getKey();
         Object value = entry.getValue();
         if(!first)
         {
            assignments = assignments + ",";
         } else first = false;
         assignments = assignments + key + "=" + value;
      }
      return s.replace(c.callString,assignments);
   }

//Gets the possible variable values from module results and returns them as mapping
//Currently unable to handle more than one possible value per module
   private HashMap<String,String> getVarValues(Module m, Call c)
   {
      HashMap<String,String> vars = new HashMap<String,String>();
      String regex = replaceVars(c.assumption,"[.[^,]]+");
      Pattern pattern = Pattern.compile(regex);

      for(String result: m.getSceptical())
      {
         Matcher matcher = pattern.matcher(result);
         if(matcher.find())
         {
            String v = c.assumption;
            String r = result;
            int vCurrent = 0;
            int rCurrent = 0;
            while(vCurrent<v.length() && rCurrent<r.length())
            {
               if(v.toCharArray()[vCurrent] != r.toCharArray()[rCurrent])
               {
                  String variable = getVar(v.substring(vCurrent));
                  String value = getValue(r.substring(rCurrent));
                  vars.put(variable,value);
                  vCurrent = vCurrent + variable.length();
                  rCurrent = rCurrent + value.length();
               }
               vCurrent++;
               rCurrent++;
            }
         }
      }
      return vars;
   }

//Gets the value out of a substring (used above to extract variable values from module results)
   private String getValue(String s)
   {
      if(s.contains(","))
      {
         return s.split(",")[0];
      }
      else
      {
         s = s.replaceAll("\\)","");
         return s;
      }
   }

//Replaces any variables (words starting in capital) with regex string
   private String replaceVars(String s, String replacement)
   {
      while(getVar(s)!=null)
      {
         s = s.replace(getVar(s),"%");
      }
      s = s.replaceAll("%",replacement);
      return s;
   }   

//Returns first variable in string
   private String getVar(String s)
   {
      String var = null;
      Pattern pattern = Pattern.compile("[A-Z]+[a-z]*");
      Matcher matcher = pattern.matcher(s);
      if(matcher.find())
      {
         var = matcher.group(0);
      }
      return var;
   }

//Removes call from rule tail (same as replacing with tautology)
   private String affirmCall(String string, Call call)
   {
      if(string.contains("in("+call.callString+"),"))
      {
         string = string.replace("in("+call.callString+"),","");
      }
      else if(string.contains("in("+call.callString+")"))
      {
         string = string.replace("in("+call.callString+")","");
      }
      else if(string.contains(call.callString+","))
      {
         string = string.replace(call.callString+",","");
      }
      else if(string.contains(call.callString))
      {
         string = string.replace(call.callString,"");
      }
      string = string.replace(":-.",".");
      string = string.replace(",.",".");
      return string;
   }   

//Removes rule of which call was tail (preventing any implication of head)
   private String removeCall(String string, Call call)
   {
      String[] lines = string.split("\n");
      String newString = "";
      for(String line : lines)
      {
         if(!line.contains(call.callString))
         {
            newString = newString + line + "\n";
         }
      }
      return newString;
   }

//Checks whether a call to a module succeeds
   private boolean callSucceeds(Module m, Call call)
   {
      if(call.negative)
      {
         if(call.type.equals("sk"))
         {
            if(m.getCredulous().contains(call.assumption)) return false;
            else return true;
         }
         else
         {
            if(m.getSceptical().contains(call.assumption)) return false;
            else return true;
         }
      }
      else
      {
         if(call.type.equals("sk"))
         {
            if(m.getSceptical().contains(call.assumption)) return true;
            else return false;
         }
         else
         {
            if(m.getCredulous().contains(call.assumption)) return true;
            else return false;
         }
      }
   }

   public static void main(String[] args)
   {
      Framework f = new Framework();
      f.addModString(f.name(t1),t1);
      f.addModString(f.name(t2),t2);
      f.getAllResults();
   }
}

