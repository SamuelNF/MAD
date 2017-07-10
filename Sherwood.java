
// Currently set up to have the customer only able to rescind, because the supplier
// was in a position to expect the possibility of the cow being pregnant, represented
// by nonbarren being an assumption in BS.

class Sherwood
{
   public static void main(String[] args)
   {
      Framework f = new Framework();

      String main =
      "%%main%%\n"+
      "in(rescind(customer)):-mutualmistake(barren),violatebasicassumption(),"+
      "not risk(customer).\n"+
      "in(rescind(supplier)):-mutualmistake(barren),violatebasicassumption(),"+
      "not risk(supplier).\n"+
      //No assumptions - neither party can rescind except via mutual mistake
      "mutualmistake(barren):-hold(pregnant),call{CK+pregnant,nonbarren,sk},"+
      "call{BC,barren,cr},call{BS,barren,cr},call{BC+barren,price(80),sk},"+
      "call{BS+barren,price(80),sk}.\n"+
      "violatebasicassumption():-hold(pregnant),call{CK+pregnant,notsale,sk}.\n"+
      //Not entirely sure how to implement the transaction possibility condition
      //(T or SaleOfCow in Dung's paper).
      "violatebasicassumption():-hold(pregnant),call{CK+pregnant,price(800),sk}.\n"+
      //Problem with current build exemplified above. Need to be able to say something
      //like "If there is a skeptical result of price(A) where A is bigger than X"
      //Use of variables in module calls is not possible if the modules are being
      //compiled seperately by clingo.
      "risk(customer):-mutualmistake(barren),call{BC,nonbarren,cr}.\n"+
      "risk(supplier):-mutualmistake(barren),call{BS,nonbarren,cr}.\n"+
      "hold(pregnant).\n";

      String CK =
      "%%CK%%\n"+
      "ass(barren).\n"+
      "ass(nonbarren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(nonbarren):-in(pregnant).\n"+
      "contrary(barren,nonbarren).\n";

      String KS =
      "%%KS%%\n"+
      "ass(barren).\n"+
      "ass(nonbarren).\n"+
      //Dung does not include ¬barren as an assumption, but I am having to because
      //he does have calls to ¬barren from main. As of yet, only assumptions are
      //reported as sk or cr results, so it needs to be an assumption for
      // this call to work. Same below in KC and CK.
      "in(price(800)):-in(pregnant).\n"+
      "in(nonbarren):-in(pregnant).\n"+
      "contrary(barren,nonbarren).\n";

      String KC =
      "%%KC%%\n"+
      "ass(barren).\n"+
      "ass(nonbarren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(nonbarren):-in(pregnant).\n"+
      "contrary(barren,nonbarren).\n";

      String BC =
      "%%BC%%\n"+
      "ass(barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(nonbarren):-in(pregnant).\n"+
      "contrary(barren,nonbarren).\n"+
      "in(price(80)):-in(barren).\n";

      String BS =
      "%%BS%%\n"+
      "ass(barren).\n"+
      "ass(nonbarren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(nonbarren):-in(pregnant).\n"+
      "contrary(barren,nonbarren).\n"+
      "in(price(80)):-in(barren).\n";

      f.addModString("main",main);
      f.addModString("CK",CK);
      f.addModString("BC",BC);
      f.addModString("BS",BS);
      f.addModString("KC",KC);
      f.addModString("KS",KS);
      f.compileAllModules();
      f.displayResults();
   }
}
