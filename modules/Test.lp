#include "engine.lp".
%%BC%%
ass(barren).
ass(nonbarren).
in(price(800)):-in(pregnant).
in(nonbarren):-in(pregnant).
in(price(80)):-in(barren).
contrary(barren,nonbarren).