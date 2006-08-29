%dynamic predicates

:-dynamic(session/2).

%-------------------------------------------------------------------------------------------
%starts a new session identified by the session-id SId for the client's request Request

start_session( SId, Request):-	assert( session( SId, request( Request) ) ).

%-------------------------------------------------------------------------------------------
%adds a new Literal to the session state identified by SId

%literals received from the other party should be added as : received(Literal)
%literals that are action results must be added as : action(successfull(Literal)) if the action was succesfull or action(unsuccessfull(Literal)) otherwise

add_to_session( SId, Literal):-	assert( session( SId, state( Literal ) ) ).

%
%-------------------------------------------------------------------------------------------
%finds all the relevant rules for the new session and returns a list, ActionList, of all the immediate actions that are to be executed.

filter_policy1( SId, ActionList):-	relevant_rules1( SId),
									partial_evaluation1( SId),
									relevant_rules2( SId),
									select_actions( SId, ActionList).

%-------------------------------------------------------------------------------------------
%finds all the relevant rules in the policy,that are not non_applicable and match the Head request 
%the relevant rules are asserted to the session identified by SId as rule( Id, Head, Body).

relevant_rules1( SId):-	session( SId, request( Request) ),	
		findall( _, relevant_rule1( SId, Request), _).

relevant_rule1( SId, Head):-		rule( Id, Head, Body),
			not( session( SId, rule( Id, Head, _) ) ),	%head is important,e.g, rules with complex terms in the head => more rules share the same rule id	
			not( metarule( SId, Id, Head, sensitivity, non_applicable) ),
			assert( session( SId, rule( Id, Head, Body) ) ),
			process_relevant_body1( SId, Body).

%finds every relevant rule whose head unifies with one predicate in a relevant rule body.
 
process_relevant_body1( _, []).
process_relevant_body1( SId, [ not( First)| Rest] ):-	process_relevant_body1( SId, [ First| Rest] ),
				!.
process_relevant_body1( SId, [ First| Rest] ):-		not( comparison( First) ),
				not( complex_term( First) ),
				not( special_predicate( First) ), 
				relevant_rule1( SId, First).
process_relevant_body1( SId, [ _| Rest] ):-		process_relevant_body1( SId, Rest).

%--------------------------------------------------------------------------------------------
%partial evaluation of the relevant rules against the session state

partial_evaluation1( SId):-	findall( _, evaluate_rule1( SId), _),
		retractall( session( SId, rule( _, _, _) ) ),
		rename_rules( SId, evaluated_rule, rule),
		rename_rules( SId, add_rule, rule).

%the body of each rule if evaluated against the state
evaluate_rule1( SId):-	session( SId, rule( Id, Head, Body)),
		not( session( SId, evaluated_rule( Id, Head, _))),
		evaluate_body1( SId, Id, Head, Body, NewBody),
		assert( session( 0, evaluated_rule( Id, Head, NewBody))).

evaluate_body1( _, _, _, [], []).
evaluate_body1( SId, Id, Head, [ First | Rest], NewBody):-not( comparison( First) ),
			must_evaluate1( SId, First),
			session(SId, state(received(First))),!,
			add_rule( SId, Id, Head),
			evaluate_body1( SId, Id, Head, Rest, NewBody).
			
evaluate_body1( SId, Id, Head, [ First | Rest], NewBody):-not( comparison( First) ),
			must_evaluate1( SId, First),
			session( SId, state(action(successfull(First)))),!,
			evaluate_body1( SId, Id, Head, Rest, NewBody).

evaluate_body1( SId, Id, Head, [ First | Rest], [First| NewBody]):-	evaluate_body1( SId, Id, Head, Rest, NewBody).

must_evaluate1( SId, Predicate):-	metarule( SId, _, Predicate, type, provisional_predicate),
			metarule(SId,_,Predicate, evaluation, immediate),
			metarule(SId,_,Predicate,actor,self),!.
must_evaluate1( SId, Predicate):-	special_predicate(Predicate),!.
must_evaluate1( SId, Predicate):-	Predicate=..[complex_term, Id, _,_],ground(Id).

add_rule( SId, Id, Head):- 	not( session( SId, add_rule( Id, Head, _))),
		rule( Id, Head, Body),!,
		assert( session( SId, add_rule( Id, Head, Body))).

add_rule( _, _, _).

%-------------------------------------------------------------------------------------------
%refines all the relevant rules for the session identified by SId according to the request for this session.

relevant_rules2( SId ):-	session( SId, request( Head) ),
		findall( _, relevant_rule2( SId, Head), _),
		retractall( session( SId, rule( _, _, _) ) ),
		rename_rules( SId, relevant_rule, rule).

relevant_rule2( SId, Head):-	session( SId, rule( Id, Head, Body) ),
		%not( metarule( SId, Id, Head, sensitivity, non_applicable) ),
		not( session( SId, relevant_rule( Id, Head, Body) ) ),
		assert( session( SId, relevant_rule( Id, Head, Body) ) ),
		process_relevant_body2( SId, Body).

%finds every relevant rule whose head unifies with one predicate in a relevant rule body.
 
process_relevant_body2( _, []).
process_relevant_body2( SId, [ not( First)| Rest] ):-	process_relevant_body2( SId, [ First| Rest] ),
				!.
process_relevant_body2( SId, [ First| Rest] ):-		not( comparison( First) ),
				not( complex_term( First) ),
				not( special_predicate( First) ), 
				relevant_rule2( SId, First).

process_relevant_body2( SId, [ _| Rest] ):-		process_relevant_body2( SId, Rest).

%--------------------------------------------------------------------------------------------
%select all the immediate actions from the state identified by SId

%the action are returned in a list [L1,L2,..] where Li is
% immediate_action(Predicate,Action) for provisional predicates
% immediate_action(Function1(Arg1,..,Argn), Package, Function2, [Args]) for the in predicate

select_actions( SId, ActionList):-	findall( _, select_action( SId), _),
			collect( SId, immediate_action( _, _), ActionList1),
			collect( SId, immediate_action( _, _, _, _), ActionList2),
			append( ActionList1, ActionList2, ActionList),
			retractall( session( SId, immediate_action(_,_) ) ),
			retractall( session( SId, immediate_action(_,_,_,_) ) ).

select_action( SId):-		session( SId, rule( Id, Head, Body) ),
			process_action_body( SId, Body).
	
process_action_body( _, []).

process_action_body( SId, [ First| Rest]):-	process_literal(SId,First),
			process_action_body( SId, Rest).

process_literal(SId,Literal):-	not(comparison(Literal)),
								not(complex_term(Literal)),
								not( session( SId, state(action(successfull(Literal))))),
								not( session( SId, state(action(unsuccessfull(Literal))))),
								get_action( SId,Literal),!.
process_literal(_,_).	

get_action( SId, Predicate):-	metarule( SId, _, Predicate, type, provisional_predicate),
			metarule( SId, _, Predicate, actor, self),
			metarule( SId, _, Predicate, evaluation, immediate),
			metarule( SId, _, Predicate, action, Action),
			not( session( SId, immediate_action( Predicate, _) ) ),
			assert( session( SId, immediate_action( Predicate, Action) ) ).
			
get_action( SId, Predicate):-	Predicate=..[ in, Function1, Package, Function2, Args],
			not( session( SId, immediate_action( in, Function1, Package, Function2, Args) ) ),
			assert( session( SId, immediate_action( in, Function1, Package, Function2, Args) ) ).	

%--------------------------------------------------------------------------------------------
% evaluates the ActionResults and selects the new immediate actions in regards with session SId

% action results should must be returned in a list [L1,L2..] where Li is
% successfull(Result) if the action result was successful
% unsuccessfull(Result) otherwise

filter_policy2( SId, ActionResults, ActionList):-		process_action_results( SId, ActionResults),
							partial_evaluation2( SId),
							select_actions( SId, ActionList).

process_action_results( SId, []).
process_action_results( SId, [ First| Rest] ):- add_to_session( SId, action(First)),
												process_action_results( SId, Rest).
											
%--------------------------------------------------------------------------------------------			
%partial evaluation of the relevant rules against the session state

partial_evaluation2( SId):-	findall( _, evaluate_rule2( SId), _),
		retractall( session( SId, rule( _, _, _) ) ),
		rename_rules( SId, evaluated_rule, rule).

%the body of each rule if evaluated against the state
evaluate_rule2( SId):-	session( SId, rule( Id, Head, Body)),
		evaluate_body2( SId, Body, NewBody),
		not( session( SId, evaluated_rule( Id, Head, NewBody))),
		assert( session( 0, evaluated_rule( Id, Head, NewBody))).

evaluate_body2( _, [], []).
evaluate_body2( SId, [ First | Rest], NewBody):-	not( comparison( First) ), write(First),
				must_evaluate2( SId, First), 
				session( SId, state(action(successfull(First)))),!,
				evaluate_body2( SId, Rest, NewBody).
evaluate_body2( SId, [ First | Rest], [First| NewBody]):-	evaluate_body2( SId, Rest, NewBody).

must_evaluate2( SId, Predicate):-	metarule( SId, _, Predicate, type, provisional_predicate),
			metarule(SId, _,Predicate, evaluation, immediate),
			metarule(SId,_,Predicate,actor,self),!.
must_evaluate2( SId, Predicate):-	Predicate=..[in|_],!.
must_evaluate2( SId, Predicate):-	Predicate=..[complex_term, Id, _,_],ground(Id).	

%-------------------------------------------------------------------------------------------
%the third phase in the filtering process

filter_policy3( SId):-	relevant_rules2( SId),
		blurring_rules( SId),
		relevant_rules2( SId),
		select_peer_actions( SId),
		add_explanations( SId),
		rename_predicates( SId).

%--------------------------------------------------------------------------------------------
%blurring rules

blurring_rules( SId):-	assert( session( SId, counter( 0) ) ),
		findall( _, blurring_rule( SId), _),
		retractall( session( SId, rename_predicate( _,_, _) ) ),
		rename_rules( SId, blurred_rule, rule).

blurring_rule( SId):-	retract( session( SId, rule( Id, Head, Body) ) ),
		not( must_blurr( SId, Head) ),
		process_blurr_body( SId, [Head], [NewHead]),
		process_blurr_body( SId, Body, NewBody),
		assert( session( SId, blurred_rule( Id, NewHead, NewBody) ) ).

must_blurr( SId, Predicate):-		metarule( SId, _, Predicate, type, provisional_predicate),
			not( metarule( SId, _, Predicate, evaluation, immediate) ),
			not( metarule( SId, _, Predicate, actor, peer) ),
			!.

must_blurr( SId, Predicate):-		metarule( SId, _, Predicate, type, state_predicate),
			not( metarule( SId, _, Predicate, sensitivity, public) ),
			!.

must_blurr( SId, Predicate):-		metarule( SId, _, Predicate, type, abbreviation_predicate),
			not( metarule( SId, _, Predicate, sensitivity, public) ),
			!.

must_blurr( SId, Predicate):-		metarule( SId, _, Predicate, type, decisional_predicate),
			not( metarule( SId, _, Predicate, sensitivity, public) ).

process_blurr_body( _, [], []).

process_blurr_body( SId, [ First| Rest1], [ blurred( NewFirst)| Rest2]):-	not( comparison( First) ),
					not( complex_term( First) ),
					not(list(First)),
					not(number(First)),
					must_blurr( SId, First),
					!,
					First=..[ Predicate | Args],
					process_blurr_body( SId, Args, NewArgs),
					length( Args, NrArgs),
					get_new_name( SId, Predicate, NrArgs, NewPredicate),
					append( [NewPredicate], NewArgs, L),
					NewFirst=..L,
					assert(  session( SId, blurred_predicate( First, NewFirst))),
					process_blurr_body( SId, Rest1, Rest2).

process_blurr_body( SId, [ First| Rest1], [ NewFirst| Rest2]):-		not( comparison( First) ),
					not( complex_term( First) ),
					not( list( First)),
					not(number(First)),
					!,
					First=..[ Predicate | Args],
					process_blurr_body( SId, Args, NewArgs),
					append( [Predicate], NewArgs, L),
					NewFirst=..L,
					process_blurr_body( SId, Rest1, Rest2).

process_blurr_body( SId, [ First| Rest1], [ First| Rest2]):-	process_blurr_body( SId, Rest1, Rest2).

%--------------------------------------------------------------------------------------------
%select the actions that are to be executed by the peer

select_peer_actions( SId):-	findall( _, select_peer_action( SId), _),
							retractall(session(SId,rule(_,_,_))),
							rename_rules(SId,action_rule,rule).

select_peer_action( SId):-	session( SId, rule( Id, Head, Body) ),
		process_peer_action_body( SId, Body, NewBody),
		not(session( SId, action_rule( Id, Head, NewBody))),
		assert(session( SId, action_rule( Id, Head, NewBody) )).
	
process_peer_action_body( _, [], []).
process_peer_action_body( SId, [ First| Rest], [ do(First)| NewRest]):-	not( comparison( First) ),
				not( complex_term( First) ),
				not( special_predicate( First) ), 
				peer_action( SId, First),!,
				process_peer_action_body( SId, Rest, NewRest).
process_peer_action_body( SId, [ Literal| Rest],[Literal|NewRest]):-		process_peer_action_body( SId, Rest,NewRest).

peer_action( SId, Predicate):-	metarule( SId, _, Predicate, type, provisional_predicate),
			metarule( SId, _, Predicate, actor, peer),
			%metarule( SId, _, Predicate, evaluation, immediate),
			metarule( SId, _, Predicate, action, Action),
			not( session( SId, metarule( pred, action( Predicate, Action), [] ) ) ),
			assert( session( SId, metarule( pred, action( do(Predicate), Action), [] ) ) ).

%--------------------------------------------------------------------------------------------
%add explanations to the session identified by SId for the rules selected

add_explanations( SId):-	findall( _, add_explanation( SId), _),
		findall( _, add_blurr_explanation( SId), _),
		retractall( session( SId, blurred_predicate( _, _) ) ).

add_explanation( SId):-	session( SId, rule( Id, Head, Body) ),
		get_explanation( SId, Id, Head),
		process_explanation_body( SId, Body).
	
get_explanation( SId, Id, _):-		nonvar( Id),
			metarule( id, explanation( Id, Explanation), Body),
			prove_body( SId, Body),
			!,
			not( session( SId, metarule( id, explanation( Id, Explanation), _) ) ),
			assert( session( SId, metarule( id, explanation( Id, Explanation), [] ) ) ).

get_explanation( SId, _, Head):-	nonvar( Head),
			metarule( pred, explanation( Head, Explanation), Body),
			prove_body( SId, Body),
			not( session( SId, metarule( pred, explanation( Head, Explanation), _ ) ) ),
			assert( session( SId, metarule( pred, explanation( Head, Explanation), [] ) ) ).

get_explanation( _, _, _).

process_explanation_body( _, []).

process_explanation_body( SId, [ First | Rest]):-	not( comparison( First) ),
				not( complex_term( First) ),
				not( list(First)),
				not( number(First) ),
				get_explanation( SId, _, First),
				First=..[ Predicate| Args],
				process_explanation_body( SId, Args).

process_explanation_body( SId, [ First | Rest]):-	process_explanation_body( SId, Rest).

add_blurr_explanation( SId):- session(  SId, blurred_predicate( OldPredicate, NewPredicate)  ),
		metarule( pred, explanation( OldPredicate, Explanation), Body),
		prove_body( SId, Body),
		not( session( SId, metarule( pred, explanation( NewPredicate, Explanation), _ ) ) ),
		assert( session( SId, metarule( pred, explanation( NewPredicate, Explanation), [] ) ) ).

%--------------------------------------------------------------------------------------------
%rename abbreviations and state predicates for the session identified by SId

rename_predicates( SId):-		findall( _, rename_predicate( SId), _),
			findall( _, rename_explanation( SId), _),
			retract( session( SId, counter( _) ) ),
			retractall( session( SId, rename_predicate( _, _, _) ) ),
			rename_rules( SId, renamed_rule, rule),
			rename_rules( SId, renamed_metarule, metarule).

rename_predicate( SId):-		retract( session( SId, rule( Id, Head, Body) ) ),
			do_rename( SId, Head, NewHead),
			process_rename_body( SId, Body, NewBody),
			assert( session( SId, renamed_rule( Id, NewHead, NewBody) ) ).

process_rename_body( _, [], []).
process_rename_body( SId, [ Predicate| Rest1], [ NewPredicate| Rest2]):-	do_rename( SId, Predicate, NewPredicate),
					process_rename_body( SId, Rest1, Rest2).


do_rename( SId, Literal, NewLiteral):-	not( complex_term(Literal) ),
			not( comparison(Literal) ),
			not( list(Literal)),
			not( number(Literal)),
			Literal=..[Name|Args],!,
			process_rename_body( SId, Args, NewArgs),
			do_rename_helper( SId, Literal, NewName),
			append([NewName],NewArgs,L),
			NewLiteral=..L.			
do_rename( SId, Literal, Literal).

do_rename_helper( SId, Predicate,NewName):- 	must_rename( SId, Predicate),
				!,
				Predicate =.. [ Name| Args],
				length( Args, NrArgs),
				get_new_name( SId, Name, NrArgs, NewName).

do_rename_helper( SId, Predicate, Name):-		Predicate=..[Name|Args].
		
must_rename( SId, Predicate):-	metarule( SId, _, Predicate, type, abbreviation_predicate),!.
must_rename( SId, Predicate):-	metarule( SId, _, Predicate, type, state_predicate).

get_new_name( SId, Name, NrArgs, NewName):-	session( SId, rename_predicate( Name, NrArgs, NewName) ),
				!.
		
get_new_name( SId, Name, NrArgs, NewName):-	retract( session( SId, counter( N) ) ),
				!,
				NewN is N+1,
				assert( session( SId, counter( NewN) ) ),
				get_name( predicat, N, NewName),
				assert( session( SId, rename_predicate( Name, NrArgs, NewName) ) ).

rename_explanation( SId):-	retract( session( SId, metarule( pred, explanation( Predicate, Explanation), [] ) ) ),
		do_rename( SId, Predicate, NewPredicate), 
		assert( session( SId, renamed_metarule( pred, explanation( NewPredicate, Explanation), []) ) ).

rename_explanation( SId):-	retract( session( SId, metarule( id, explanation( Id, Explanation), [] ) ) ),
		assert( session( SId, renamed_metarule( id, explanation( Id, Explanation), []) ) ).

%-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
%protune-usefull

%--------------------------------------------------------------------------------------------
%checks wheather for the rule identified by Id or for the predicate that unifies with Head there is a satisfied metarule  with the given Value for the given Attribute, in regards to session SId.

metarule( SId, Id, _, Attribute, Value):-	nonvar( Id),
			F =..[ Attribute, Id, Value],
			metarule( id, F, Body), 
			prove_body( SId, Body),
			!.

metarule( SId, _, Head, Attribute, Value):-	nonvar( Head),
			F =..[ Attribute, Head, Value],
			metarule( pred, F, Body),
			prove_body( SId, Body).

%tries to prove all the goals in the given list for the session SId.

prove_body( SId, []).
prove_body( SId, [ First| Rest]):-	prove( SId, First),
			prove_body( SId, Rest).

prove( SId, not( Goal) ):-		not( prove( SId, Goal) ), 
			!.

prove( SId, metarule( id, Field) ):-	Field =..[ Attribute, Id, Value],
			metarule( SId, Id, _, Attribute, Value), 
			!.

prove( SId, metarule( pred, Field)):-	Field =..[ Attribute, Head, Value],
			metarule( SId, _, Head, Attribute, Value), 
			!.

%looking for the goal in the session
prove( SId, Goal):-		session( SId, rule( _, Goal, Body) ),
			prove_body( SId, Body),
			!.

%looking for the goal in the session state
prove( SId, Goal):-		session( SId, state(Goal) ),
			!.
			
%looking for the goal in the policy
prove( SId, Goal):-		rule( _, Goal, Body),
			prove_body( SId, Body).

%for comparison or ground
prove( SId, Goal):-		Goal.

%-------------------------------------------------------------------------------------------
%rename the rules belonging to the session Sid from OldName to NewName

rename_rules( Sid, OldName, NewName):-	findall( _, rename_rule( SId, OldName, NewName), _).

rename_rule( SId, OldName, NewName):-	F1 =.. [ OldName, Id, Head, Body],
			retract( session( SId, F1) ),
			F2 =.. [ NewName, Id, Head, Body],
			assert( session( SId, F2) ). 	
			

%--------------------------------------------------------------------------------------------
%collects all the predicates with whom Predicate unifies in the list L,from the session identified by  SId

collect( SId, Predicate, L):-	findall( Predicate, session( SId, Predicate), L).


%--------------------------------------------------------------------------------------------

% L is the concatenation of the lists L1 and L2.
%append( +L1, +L2, -L).

append( [], L, L).
append( [ First1| Rest1], L2, [ First1| Rest] ):-	append( Rest1, L2, Rest).

% constracts the name X from the atom A and the number N
%get_name( _A, +N, -X).

get_name( A, N, X):-	atom_chars( A, L1),
		number_chars( N, L2),
		append( L1, L2, L),
		atom_chars( X, L).

%--------------------------------------------------------------------------------------------

comparison(X):- X= '='(_,_).
comparison(X):- X= '>'(_,_).
comparison(X):- X= '>='(_,_).
comparison(X):- X= '<'(_,_).
comparison(X):- X= '<='(_,_).
comparison(X):- X= '!='(_,_).
comparison(X):- X= 'is'(_,_).

special_predicate(X):-X= 'in'(_,_,_,_).
special_predicate(X):-X= 'declaration'(_,_).
special_predicate(X):-X= 'credential'(_,_).

complex_term(X):-X= 'complex_term'(_,_,_).

%--------------------------------------------------------------------------------------------
%deletes element X from the list L, the result is NewL

delete( _, [], [] ):-!.
delete( X, [ X| Rest], Rest ):-!.
delete( X, [ First| Rest1], [ First| Rest2] ):-	delete( X, Rest1, Rest2).

%-----------------------------------------------------------------------------------------------
read_state(SId, List):- findall( Literal, session( SId, state(Literal)), List).
