party(customer).
party(supplier).
in(rescind(X)):-mutualmistake(barren),violatebasicassumption(),not risk(X),party(X).
mutualmistake(barren):-hold(pregnant).
violatebasicassumption(X):-hold(pregnant),X=10,Y=20,X>Y.
violatebasicassumption(X):-hold(pregnant),X=30,Y=40,X>Y.
risk(supplier):-mutualmistake(barren).
hold(pregnant).


contrary(Y,X) :- contrary(X,Y).

in(X) :- not out(X), ass(X).
out(X) :- not in(X), ass(X).

:- contrary(Y,X), in(Y), in(X).


