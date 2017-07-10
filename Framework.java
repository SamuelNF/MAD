import java.io.*;
import java.util.*;
import java.util.regex.*;

class Framework
{
   private Map<String,String> moduleStrings = new HashMap<String,String>();
   private Map<String,Module> modules = new HashMap<String,Module>();

   private static String t1 =
   "%%Test1%%\n"+
   "ass(a).\n"+
   "ass(b).\n"+
   "contrary(a,b).\n"+
   "in(a):-call{Test3,a,sk}.\n";

   private static String t2 =
   "%%Test2%%\n"+
   "ass(a).\n"+
   "ass(b).\n"+
   "contrary(a,b).\n"+
   "in(c):-call{Test3,b,sk}.\n"+
   "in(a):-call{Test3+c,a,sk}.\n";

   private static String t3 =
   "%%Test3%%\n"+
   "ass(a).\n"+
   "ass(b).\n"+
   "contrary(a,b).\n"+
   "in(a):-in(c).\n";

   public void addModString(String name, String string)
   {
      moduleStrings.put(name,string);
   }      

   public static void main(String[] args)
   {
      Framework f = new Framework();
      f.addModString(f.name(t1),t1);
      f.addModString(f.name(t2),t2);
      f.addModString(f.name(t3),t3);
      f.compileAllModules();
      f.displayResults();
   }

   private boolean containsCall(String module)
   {
      Pattern pattern = Pattern.compile("call\\{.*?\\}");
      Matcher matcher = pattern.matcher(module);
      if(matcher.find())
      {
         return true;
      }
      else return false;
   }

   private String name(String module)
   {
      return module.split("%%")[1];
   }

   public void displayResults()
   {
      for(Module m : modules.values())
      {
         m.displayResults();
      }
   }

   private int totalCalls()
   {
      int calls = 0;
      for(String module: moduleStrings.values())
      {
         if(containsCall(module))
         {
            calls = calls + ((module.length()-module.replace("call","").length())/4);
         }
      }
      return calls;
   }
  
   public void compileAllModules()
   {
      int passes = 0;
      int maxPasses = moduleStrings.size() + totalCalls();

      while(modules.size() < moduleStrings.size())
      {   
         passes++;
         System.out.print("\n%%%% Pass: "+passes+" %%%%\n\n"); 
         for(String module: moduleStrings.keySet())
         {
            if(!modules.containsKey(module))
            {
               compileModule(module);
            }
         }
         if(passes > maxPasses)
         {
            System.out.print("\n--------\nERROR - MAXIMUM PASSES REACHED\n--------\n");
            return;
         }      
      }
   }

   private void compileModule(String name)
   {
      String module = moduleStrings.get(name);
      System.out.println("Examining \""+name+"\":\n"+module);
      if(containsCall(module))
      {
         System.out.println("CALL FOUND");
         checkCall(name);
      }
      else
      {
         System.out.println("COMPILING MODULE \""+name+"\"\n-------------");
         modules.put(name,new Module(name,module));
      }
   }

   private void checkCall(String name)
   {
      Pattern pattern = Pattern.compile("call\\{.*?\\}");
      String mString = moduleStrings.get(name);
      Matcher matcher = pattern.matcher(mString);
      if(matcher.find())
      {
         Call call = new Call(matcher.group(0));
         if(modules.containsKey(call.module))
         {
            System.out.println(name+" - RESOLVING CALL "+call.callString);
            if(call.hasUnion())
            {
               String tempModule = moduleStrings.get(call.module) + "in("+ call.union +").\n";
               call.module = "temp";
               moduleStrings.put("temp",tempModule);
               compileModule("temp");
               moduleStrings.put(name,resolveCall(mString,call));
               moduleStrings.remove("temp");
               modules.remove("temp");
            }
            else
            {
               moduleStrings.put(name,resolveCall(mString, call));
            }
         }
         else System.out.println("CANNOT RESOLVE CALL "+call.callString);
      }
   }

   private String resolveCall(String string, Call call)
   {
      if(call.type.equals("sk"))
      {
         if(modules.get(call.module).getSceptical().contains(call.assumption))
         {
            System.out.println(call.callString+" succeeded!");
            string = affirmCall(string, call);
         }
         else
         {
            System.out.println(call.callString+" failed!");
            string = removeCall(string, call);
         }
      }
      else if(call.type.equals("cr"))
      {
         if(modules.get(call.module).getCredulous().contains(call.assumption))
         {
            System.out.println(call.callString+" succeeded!");          
            string = affirmCall(string, call);
         }
         else
         {
            System.out.println(call.callString+" failed!");
            string = removeCall(string, call);
         }
      }
      System.out.println("====== New Module String ======\n"+string+"====================");
      return string; 
   }

   private String affirmCall(String string, Call call)
   {
      if(string.contains(","+call.callString))
      {
         return string.replace(","+call.callString,"");
      }
      else if(string.contains(call.callString))
      {
         return string.replace(call.callString,"");
      }
      System.out.println("%%%%%%%%% SOMETHING WENT WRONG WITH CALL AFFIRMATION %%%%%%%%");
      return string;
   }      

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
}
