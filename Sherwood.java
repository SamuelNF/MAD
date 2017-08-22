// Currently set up to have on resolution the customer able to rescind, because the supplier
// was in a position to expect the possibility of the cow being pregnant, represented
// by -barren being an assumption in BS.

class Sherwood
{
   public static void main(String[] args)
   {
      Framework f = new Framework();

      String main =
      "%%main%%\n"+
      "party(customer).\n"+
      "party(supplier).\n"+
      //These are superfluous but needed to make a positive literal that the variable
      //can be part of in the rule below.
      "in(rescind(X)):-mutualmistake(barren),violatebasicassumption(),"+
      "not risk(X),party(X).\n"+
      //No assumptions - neither party can rescind except via mutual mistake
      "mutualmistake(barren):-hold(pregnant),call{CK+pregnant,[-barren],sk},"+
      "call{BC,[barren],cr},call{BS,[barren],cr},call{BC+barren,[price(80)],sk},"+
      "call{BS+barren,[price(80)],sk}.\n"+
      "violatebasicassumption():-hold(stolen),call{CK+stolen,[notsale],sk}.\n"+
      //Not entirely sure how to implement the transaction possibility condition
      //(T or SaleOfCow in Dung's paper).
      "violatebasicassumption():-hold(pregnant),call{CK+pregnant,[price(X)],sk},X>80.\n"+
      "risk(customer):-mutualmistake(barren),call{BC,[-barren],cr}.\n"+
      "risk(supplier):-mutualmistake(barren),call{BS,[-barren],cr}.\n"+
      "hold(pregnant).\n";

      String CK =
      "%%CK%%\n"+
      "ass(barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(-barren):-in(pregnant).\n"+
      "contrary(barren,-barren).\n";

      String KS =
      "%%KS%%\n"+
      "ass(barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(barren):-in(pregnant).\n"+
      "contrary(barren,-barren).\n";

      String KC =
      "%%KC%%\n"+
      "ass(barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(-barren):-in(pregnant).\n"+
      "contrary(barren,-barren).\n";

      String BC =
      "%%BC%%\n"+
      "ass(barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(-barren):-in(pregnant).\n"+
      "contrary(barren,-barren).\n"+
      "in(price(80)):-in(barren).\n";

      String BS =
      "%%BS%%\n"+
      "ass(barren).\n"+
      "ass(-barren).\n"+
      "in(price(800)):-in(pregnant).\n"+
      "in(-barren):-in(pregnant).\n"+
      "contrary(barren,-barren).\n"+
      "in(price(80)):-in(barren).\n";

      f.addModString("main",main);
      f.addModString("CK",CK);
      f.addModString("BC",BC);
      f.addModString("BS",BS);
      f.addModString("KC",KC);
      f.addModString("KS",KS);
      f.getResults("main");
   }
}
