import java.util.*;

public class Assumption implements java.io.Serializable
{
   public final String assumption;
   public final String contrary;

   public Assumption(String ass, String con)
   {
      assumption = ass;
      contrary = con;
   }
   
   public String getAssumption()
   {
      return assumption;
   }

   public String getContrary()
   {
      return contrary;
   }

   public String toString()
   {
      String s = "ass("+assumption+").\n";
      if(!contrary.isEmpty())
      {
         s = s + "contrary("+assumption+","+contrary+").\n";
      }
      return s;
   }
}
