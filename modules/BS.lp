#include "engine.lp".
%%BS%%
ass(barren).
ass(nonbarren).
in(price(800)):-in(pregnant).
in(nonbarren):-in(pregnant).
contrary(barren,nonbarren).
in(price(80)):-in(barren).
