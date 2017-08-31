#include "engine.lp".
%%main%%
in(party(customer)).
in(party(supplier)).
in(hold(pregnant)).
in(mutualMistake(barren)):-in(hold(pregnant)).
in(rescind(X)):-in(mutualMistake(barren)),in(violateBasicAssumption()),not in( risk(X)),in( party(X)).
in(violateBasicAssumption()):-in(hold(pregnant)),X>80,X=800.
in(risk(supplier)):-in(mutualMistake(barren)).
