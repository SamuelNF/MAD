#include "engine.lp".
%%KS%%
ass(barren).
ass(nonbarren).
in(price(800)):-in(pregnant).
in(nonbarren):-in(pregnant).
contrary(barren,nonbarren).
