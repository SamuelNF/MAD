import java.util.*;

class UIModule implements java.io.Serializable
{
   private String name;
   private List<Assumption> assumptions;
   private List<Rule> rules;

   public UIModule(String newName)
   {
      assumptions = new ArrayList<Assumption>();
      rules = new ArrayList<Rule>();
      name = newName;
   }

   public String toString()
   {
      String s = "%%"+name+"%%\n";
      for(Rule r : rules)
      {
         s = s + r.toString();
      }
      for(Assumption a : assumptions)
      {
         s = s + a.toString();
      }
      return s;
   }

   public void addAssumptions(List<Assumption> list)
   {
      for(Assumption a : list)
      {
         assumptions.add(a);
      }
   }

   public void addRules(List<Rule> list)
   {
      for(Rule r : list)
      {
         rules.add(r);
      }
   }

   public String getName()
   {
      return name;
   }

   public List<Assumption> getAssumptions()
   {
      return assumptions;
   }

   public List<Rule> getRules()
   {
      return rules;
   }
}
