#include "engine.lp".
%%CK%%
ass(barren).
in(price(800)):-in(pregnant).
in(-barren):-in(pregnant).
in(notsale):-in(stolen).
contrary(barren,-barren).
in(pregnant).
