import java.util.*;
import java.io.*;
import java.util.regex.*;


public class Rule implements java.io.Serializable
{
   public final String head;
   public final String tail;
   
   public Rule(String h, String t)
   {
      head = h;
      tail = t;
   }

   public String getHead()
   {
      return head;
   }

   public String getTail()
   {
      return tail;
   }

   public String toString()
   {
      String s = "in("+head+")";
      if(!tail.isEmpty())
      {
         s = s + ":-"+sanitiseTail(tail);
      }
      s = s + ".\n";
      return s;
   }


   private String sanitiseTail(String tail)
   {
      Pattern callPattern = Pattern.compile("call\\{.*?\\}");
      Pattern operatorPattern = Pattern.compile("[><=]");
      Matcher matcher1;
      Matcher matcher2;
      String s = "";
      List<String> calls = new ArrayList<String>();
      try
      {
         while(Framework.containsCall(tail))
         {
            String c = Framework.getCall(tail).callString;
            calls.add(c);
            tail = tail.replace(c+",", "");
            tail = tail.replace(c,"");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      String[] parts = tail.split(",");
      for(String p : parts)
      {
         matcher1 = callPattern.matcher(p);
         matcher2 = operatorPattern.matcher(p);
         if(matcher1.find()||matcher2.find())
         {
            s = s + p + ",";
         }
         else
         {
            if(!p.isEmpty())
            {
               if(p.contains("not"))
               {
                  p = p.replace("not","");
                  s = s + "not in(" + p + "),";
               }
               else
               {
                  s = s + "in(" + p + "),";
               }
            }
         }
      }
      for(String c : calls)
      {
         s = s + c.replace("[not ","[-") + ",";
      }
      s = s.substring(0, s.length() - 1);
      System.out.println("$$$$$$$"+s);
      return s;
   }
}
