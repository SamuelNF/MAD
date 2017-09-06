%% ABA Engine (shows all admissible sets including empty set)

contrary(Y,X) :- contrary(X,Y).

in(X) :- not out(X), ass(X).
out(X) :- not in(X), ass(X).

:- ass(X), not in(X), not out(X).

:- contrary(Y,X), in(Y), in(X).

#show in/1.
