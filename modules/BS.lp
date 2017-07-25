#include "engine.lp".
%%BS%%
ass(barren).
ass(-barren).
in(price(800)):-in(pregnant).
in(-barren):-in(pregnant).
contrary(barren,-barren).
in(price(80)):-in(barren).
