
party(sherwood).
party(walker).
condition(barren).
hold(pregnant).
greaterthan(800,80). 

in(rescind(Y)) :- violatebasicassumption(), party(Y).

violatebasicassumption() :- hold(Omega), call(module(union(ck,Omega)), assumption(price(cost)),sk), greaterthan(cost,80).
