ass(rescind(customer)).
ass(rescind(supplier)).
in(rescind(customer)):-mutualmistake(barren),violatebasicassumption(),not risk(customer).
in(rescind(supplier)):-mutualmistake(barren),violatebasicassumption(),not risk(supplier).
mutualmistake(barren):-hold(pregnant).
violatebasicassumption():-hold(pregnant).
hold(pregnant).


#show in/1.
#show out/1.

