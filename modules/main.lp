#include "engine.lp".
%%main%%
in(rescind(customer)):-mutualmistake(barren),violatebasicassumption(),not risk(customer).
in(rescind(supplier)):-mutualmistake(barren),violatebasicassumption(),not risk(supplier).
mutualmistake(barren):-hold(pregnant).
violatebasicassumption():-hold(pregnant).
risk(supplier):-mutualmistake(barren).
hold(pregnant).
