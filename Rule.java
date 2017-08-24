import java.util.*;

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
         s = s + ":-"+"in("+tail+")";
      }
      s = s + ".\n";
      return s;
   }
}
