
class Call
{
   public String module;
   public String assumption;
   public String type;
   public String callString;
   public String union = null;

   public Call(String string)
   {
      callString = string;
      module = string.split(",")[0];
      module = module.split("\\{")[1];
      if(module.contains("+"))
      {
         String[] moduleString = module.split("\\+");
         module = moduleString[0];
         union = moduleString[1];
      }
      assumption = string.split(",")[1];
      type = string.split(",")[2];
      type = type.split("\\}")[0];
   }

   public boolean hasUnion()
   {
      if(union==null) return false;
      else return true;
   }

   public static void main(String[] args)
   {
      Call call = new Call("call{M1+b,a,sk}");
      System.out.println(call.module);
      System.out.println(call.assumption);
      System.out.println(call.type);
      System.out.println(call.hasUnion());
      System.out.println(call.union);
   }
}
