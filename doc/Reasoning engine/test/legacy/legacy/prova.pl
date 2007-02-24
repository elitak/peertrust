% Metainterpreter

% a(X) :- b(X), c(X).
% c(X) :- d(X).
% b(0).
% d(0).
% b(1).
% d(1).

theory([
   rule(a(X1), [b(X1), c(X1)]),
   rule(c(X2), [d(X2)]),
   rule(b(0), []),
   rule(d(0), []),
   rule(b(1), []),
   rule(d(1), [])
]).

prove([], Theory).
prove([H|T], Theory) :-
   proveAtom(H, Theory),
   prove(T, Theory).

proveAtom(Atom, Theory) :-
   append(_, [rule(Atom, [])|_], Theory).
proveAtom(Atom, Theory) :-
   append(_, [rule(Atom, Goals)|_], Theory),
   notEmptyList(Goals),
   prove(Goals, Theory).

notEmptyList([H|T]).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Procedural version:
% 
% Rule[] theory;
% Vector<Atom> goal;
% Vector<Rule> relevantRules;
% 
% for each x in goal{
%    if(!isProvisional(x)) for each y in theory if(unifies(x, y.getHead())){
%       relevantRules.add(y);
%       for each z in y.getBody() goal.add(z);
%    }
% }
% return relevantRules;

unify(aa, aX).
unify(ab, aX).
unify(ba, bX).
unify(bb, bX).

getBody(aXifbXcX, [bX, cX]).
getBody(bXifaXcX, [aX, cX]).

getHead(aXifbXcX, aX).
getHead(bXifaXcX, bX).

% relevantRules(Goal, Theory, RelevantRules)
%param Goal
%param Theory
%param RelevantRules
%return true if RelevantRules contains all rules of Theory relevant for proving Goal

relevantRules(Goal, Theory, RelevantRules) :-
   isSubsetOf(Goal, X),
   isCompleteGoal(X, RelevantRules),
   isCompleteRuleset(RelevantRules, X, Theory).


% isCompleteGoal(Goal, Ruleset)
%param Goal
%param Ruleset
%return true if Goal contains each atom of the body of each rule in Ruleset

isCompleteGoal(Goal, []).
isCompleteGoal(Goal, [H|T]) :-
   getBody(H, X),
   isSubsetOf(X, Goal),
   isCompleteGoal(Goal, T).


% isCompleteRuleset(Ruleset, AtomList, Theory).
%param Ruleset a set of rules containing all rules of Theory whose head unifies with some not provisional atom in AtomList.
%param AtomList 
%param Theory
%return true if all rules belonging to Theory whose head unifies with some not provisional atom in AtomList are in Ruleset.

isCompleteRuleset(Ruleset, [], Theory).
isCompleteRuleset(Ruleset, [H|T], Theory) :-
   isProvisional(H),
   !,
   isCompleteRuleset(Ruleset, T, Theory).
isCompleteRuleset(Ruleset, [H|T], Theory) :-
   getUnifyingRules(H, Theory, X),
   isSubsetOf(X, Ruleset),
   isCompleteRuleset(Ruleset, T, Theory).


% getUnifyingRules(Atom, Ruleset, UnifyingRules).
%param Atom the atom the heads of the rules in Ruleset should unify with.
%param Ruleset the set of rules, whose heads should unify with Atom.
%param UnifyingRules the subset of Ruleset containing just the rules, whose heads unify with Atom.
%return true if UnifyingRules is the subset of Ruleset containing just the rules, whose heads unify with Atom.

getUnifyingRules(X, [], []).
getUnifyingRules(X, [H|T], [H|Y]) :-
   getHead(H, Z),
   unify(X, Z),
   !,
   getUnifyingRules(X, T, Y).
getUnifyingRules(X, [H|T], Y) :-
   getUnifyingRules(X, T, Y).


% isSubsetOf(SubSet, Set)
%param SubSet the supposed subset.
%param Set the supposed superset.
%return true if SubSet is a subset of Set.

isSubsetOf([], X).
isSubsetOf([H|T], X) :-
   isContainedIn(H, X),
   isSubsetOf(T, X).


% isContainedIn(Element, List)
%param Element: The element to be searched in List.
%param List: The List in which Element is searched.
%return true if Element is in List.

isContainedIn(H, [H|T]) :-
   !.
isContainedIn(X, [H|T]) :-
   isContainedIn(X, T).