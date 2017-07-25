#include "engine.lp".
%%main%%
party(customer).
party(supplier).
in(rescind(X)):-mutualmistake(barren),violatebasicassumption(),not risk(X),party(X).
mutualmistake(barren):-hold(pregnant).
violatebasicassumption():-hold(pregnant),X=800,X>80.
risk(supplier):-mutualmistake(barren).
hold(pregnant).
