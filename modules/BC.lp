#include "engine.lp".
%%BC%%
ass(barren).
in(price(800)):-in(pregnant).
in(nonbarren):-in(pregnant).
contrary(barren,nonbarren).
in(price(80)):-in(barren).
