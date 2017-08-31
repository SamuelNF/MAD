#include "engine.lp".
%%BS%%
in(price(800)):-in(pregnant).
in(fertile):-in(pregnant).
in(price(80)):-in(barren).
ass(barren).
contrary(barren,fertile).
ass(fertile).
