:-dynamic(counter/1).
:-dynamic(tree/3).
:-dynamic(processed/2).

credential_selection( Root):-	request( Request),
		assert( counter( 0) ),
		findall( No, process_rule( Request, No), OptionList),
		get_number( Root),
		retract( counter(_) ),
		assert( tree( Root, or, OptionList) ),
		retractall( processed_rule( _, _) ).

get_number( No):-	retract( counter( No) ),
		NewNo is No+1,
		assert( counter( NewNo) ),
		!.

process_rule( Head, No):-  	rule( Id, Head, Body),
		process_rule_helper( Id, Body, No).

process_rule_helper( Id, _, No):-	processed_rule( Id, No),!.

process_rule_helper( Id, Body, No):-	process_body( Body, Credentials),!,
			Credentials \= [],
			get_number( No),
			assert( tree( No, and, Credentials)),
			assert( processed_rule( Id, No) ).

process_body( [], []).

process_body( [ Literal | Body], [ Credential | Credentials]):-		check_literal( Literal, Id, true),
					retrieve_complex_terms( Id, Body, BodyRest, ComplexTerms),!,
					append( [ Literal ], ComplexTerms , Credential),
					process_body( BodyRest, Credentials).		

process_body( [ Literal | Body], [ action( Literal, Action) | Credentials]):-	check_literal( Literal, Action, false),!,
					process_body( Body, Credentials).

process_body( [ Literal | Body], [  Nr | Credentials]):-	not(comparison(Literal)),
				not(complex_term(Literal)),
				findall( No, process_rule( Literal, No), OptionList),
				OptionList \= [],!,
				get_number( Nr),
				assert( tree( Nr, or, OptionList) ),
				process_body( Body, Credentials).

process_body( [ _ | Body], Credentials):-	process_body( Body, Credentials).

retrieve_complex_terms( _, [], [], []).
retrieve_complex_terms( Id, [First|Body],BodyRest,[First|ComplexTerms]):- ground(Id),First=..[complex_term,Id,_,_],!,
	retrieve_complex_terms( Id, Body, BodyRest, ComplexTerms).
retrieve_complex_terms( Id, [First|Body],BodyRest,[First|ComplexTerms]):- var(Id),First=..[complex_term,CId,_,_],CId =Id,!,
	retrieve_complex_terms( Id, Body, BodyRest, ComplexTerms).
retrieve_complex_terms( _, Body, Body, []).

%what is selected
check_literal( Literal, Id, true):-	Literal =.. [ credential, _, Id].
check_literal( Literal, Id, true):-	Literal =.. [ declaration, _, Id].
check_literal( Literal, Action, false):-	Literal=..[do|_],
			metarule(pred,action(Literal,Action),_).

append( [], L, L).
append( [ First1| Rest1], L2, [ First1| Rest] ):-	append( Rest1, L2, Rest).

comparison(X):- X= '='(_,_).
comparison(X):- X= '>'(_,_).
comparison(X):- X= '>='(_,_).
comparison(X):- X= '<'(_,_).
comparison(X):- X= '<='(_,_).
comparison(X):- X= '!='(_,_).
comparison(X):- X= 'is'(_,_).

complex_term(X):-X= 'complex_term'(_,_,_).
