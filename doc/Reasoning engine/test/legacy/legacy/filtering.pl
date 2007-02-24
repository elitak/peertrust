% Policy.
%
% recognized_university(upb).
% recognized_university(hu).
% recognized_university(epfl).
%
% allow(access(Resource)) :-
%    credential(sa,Student_card[type:student,issuer:I,public_key:K]),
%    valid_credential(Student_card,I),
%    recognized_university(I),
%    challenge(K).
%
% valid credential(C,I) :-
%    public_key(I,K),
%    verify_signature(C,K).

policy([
   rule(recognized_university(upb), []),
   rule(recognized_university(hu), []),
   rule(recognized_university(epfl), []),
   rule(allow(access(_1)), [credential(sa, _2, student, _3, _4), valid_credential(_2, _3), recognized_university(_3), challenge(_4)]),
   rule(valid_credential(_5, _6), [public_key(_6, _7), verify_signature(_5, _7)])
]).

% Metapolicy.
%
% credential(_, _, _, _, _).type:provisional_predicate.
% credential(_, _, _, _, _).actor:peer.
%
% valid_credential(_, _).type:abbreviation_predicate.
% valid_credential(_, _).sensitivity:public.
%
% recognized_university(_).type:state predicate.
% recognized_university(_).evaluation:immediate.
% recognized_university(_).sensitivity:public. 
%
% challenge(_).type:provisional_predicate.
% challenge(K).evaluation:immediate :- ground(K).
% challenge(_).actor:self.
% challenge(_).sensitivity:public.
%
% public_key(_, _).type:provisional_predicate.
% public_key(I, _).evaluation:immediate :- ground(I).
% public_key(_, _).actor:self.
% public_key(_, _).sensitivity:private.
%
% verify_signature(_, _).type:provisional_predicate.
% verify_signature(C, K).evaluation:immediate :- ground(C), ground(K).
% verify_signature(_, _).actor:self.
% verify_signature(_, _).sensitivity:private.

type(credential(_1, _2, _3, _4, _5).type:provisionalPredicate).
actor(credential(_1, _2, _3, _4, _5).actor:peer).

type(valid_credential(_1, _2), abbreviationPredicate).
sensitivity(valid_credential(_1, _2), public).

type(recognized_university(_1), stateQueryPredicate).
evaluation(recognized_university(_1), immediate).
sensitivity(recognized_university(_1), public).

type(challenge(_1), provisionalPredicate).
evaluation(challenge(K), immediate).% :- ground(K).
actor(challenge(_1), self).
sensitivity(challenge(_1), public).

type(public_key(_1, _2), provisionalPredicate).
evaluation(public_key(I, _1), immediate) :- ground(I).
actor(public_key(_1, _2), self).
sensitivity(public_key(_1, _2), private).

type(verify_signature(_1, _2), provisionalPredicate).
evaluation(verify_signature(C, K), immediate) :- ground(C), ground(K).
actor(verify_signature(_1, _2), self).
sensitivity(verify_signature(_1, _2), private).

% Run Time Environment
%
% credential(sa, studentcard[type: student, issuer: hu, public_key: 5272117])

actionWellPerformed(credential(sa, studentcard, student, hu, 5272117)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

extractActions([], Theory, []).
extractActions([H|T], Theory, Actions) :-
   type(H, provisionalPredicate),
   actor(H, self),
   actionWellPerformed(H),
   !,
   extractActions(T, Theory, Actions).
extractActions([H|T], Theory, [H|L]) :-
   type(H, provisionalPredicate),
   actor(H, self),
%  not actionWellPerformed(H),
   evaluation(H, immediate),
   !,
   extractActions(T, Theory, L).
extractActions([H|T], Theory, Actions) :-
   type(H, abbreviationPredicate),
   !,
   extractGoals(H, Theory, X),
   union(T, X, Goals),
   extractActions(Goals, Theory, Actions).
extractActions([H|T], Theory, Actions) :-
   extractActions(T, Theory, Actions).

extractGoals(Predicate, [], []).
% Add variable instantiation in the following rule, something like
%
% if(Predicate is stateQuery)
%    if(proof(Predicate)) add(/*instantiated*/ Predicate);
%    else add(/*not instantiated*/ Predicate);
% else if(Predicate is Provisional)
%    if(actionWellPerformed(Predicate)) add(/*instantiated*/ Predicate);
%    else add(/*not instantiated*/ Predicate);
extractGoals(Predicate, [rule(X, Y)|T], Goals) :-
   unifies(Predicate, X),
   !,
   extractGoals(Predicate, T, Z),
   union(Y, Z, Goals).
extractGoals(Predicate, [H|T], Goals) :-
   extractGoals(Predicate, T, Goals).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

prove([]).
prove([H|T]) :-
   actionWellPerformed(H),
   prove(T).
prove([H|T]) :-
   rule(H, []),
   prove(T).
prove([H|T]) :-
   rule(H, [Bhead|Btail]),
   union([Bhead|Btail], T, X),
   prove(X).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

filter([], Theory, []).
filter([H|T], Theory, FilteredTheory) :-
   filteringStep(H, Theory, L1),
   getBodies(L1, X),
   filterGoals(X, Y),
   union(T, X, Goals),
   filter(Goals, Theory, L2),
   union(L1, L2, FilteredTheory).

filteringStep(Goal, [], []).
filteringStep(Goal, [rule(X, Y)|T1], [H2|T2]) :-
   unifies(Goal, X),
   !,
   filterBody(rule(X, Y), H2),
   filteringStep(Goal, T1, T2).
filteringStep(Goal, [H|T], L) :-
   filteringStep(Goal, T, L).

getBodies([], []).
getBodies([rule(_, B)|T], L) :-
   getBodies(T, L2),
   union(B, L2, L).

filterGoals([], []).
filterGoals([H|T], [H|L]) :-
   type(H, abbreviationPredicate),
   sensitivity(H, public),
   !,
   filterGoals(T, L).
filterGoals([H|T], [H|L]) :-
   type(H, stateQueryPredicate),
   sensitivity(H, public),
   !,
   filterGoals(T, L).
filterGoals([H|T], L) :-
   filterGoals(T, L).

filterBody(rule(H, B), rule(H, FilteredBody)) :-
   filterLiterals(B, FilteredBody).

filterLiterals([], []).
filterLiterals([H|T], [H|L]) :-
   type(H, abbreviationPredicate),
   sensitivity(H, public),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], [blurred(H)|L]) :-
   type(H, abbreviationPredicate),
%  isPrivate(H),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], [H|L]) :-
   type(H, stateQueryPredicate),
   sensitivity(H, public),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], [blurred(H)|L]) :-
   type(H, stateQueryPredicate),
%  isPrivate(H),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], L) :-
   type(H, provisionalPredicate),
   actor(H, peer),
   actionWellPerformed(H),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], [H|L]) :-
   type(H, provisionalPredicate),
   actor(H, peer),
%  not actionWellPerformed(H),
   !,
   filterLiterals(T, L).
filterLiterals([H|T], L) :-
   filterLiterals(T, L).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

unifies(X, X).

union([], L, L).
union([H|T], L1, L) :-
   isIn(H, L1),
   !,
   union(T, L1, L).
union([H|T], L1, [H|L]) :-
   union(T, L1, L).

isIn(H, [H|T]) :-
   !.
isIn(X, [H|T]) :-
   isIn(X, T).