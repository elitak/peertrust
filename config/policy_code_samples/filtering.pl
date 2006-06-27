%dynamic predicates

:-dynamic(session/2).

%-------------------------------------------------------------------------------------------
%starts a new session identified by the session-id SId for the Head request

start_session( SId, Head):-	assert( session( SId, request( Head) ) ).

add_to_session( SId, Rule):-	assert( session( SId, Rule ) ).

%-------------------------------------------------------------------------------------------
%finds all the relevant rules for the new session and returns a list, ActionList, of all the immediate actions that are to be executed.

filter_policy1( SId, ActionList):-	relevant_rules1( SId),
		select_actions( SId, ActionList).

%-------------------------------------------------------------------------------------------
%finds all the relevant rules in the policy,that are not non_applicable and match the Head request 
%the relevant rules are asserted to the session identified by SId as relevant_rule( Id, Head, Body).

relevant_rules1( SId):-	session( SId, request( Head) ),	
		findall( _, relevant_rule1( SId, Head), _).

relevant_rule1( SId, Head):-		rule( Id, Head, Body),
			not( session( SId, rule( Id, Head, _) ) ),		
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
%select all the immediate actions from the state identified by SId

select_actions( SId, ActionList):-	findall( _, select_action( SId), _),
			collect( SId, immediate_action( _, _), ActionList),
			retractall( session( SId, immediate_action(_) ) ).

select_action( SId):-		session( SId, rule( Id, Head, Body) ),
			get_action( SId, Id, Head),
			process_action_body( SId, Body).
	
get_action( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, provisional_predicate),
			metarule( SId, Id, Predicate, actor, self),
			metarule( SId, Id, Predicate, evaluation, immediate),
			metarule( SId, Id, Predicate, action, Action),
			not( session( SId, immediate_action( Action) ) ),
			assert( session( SId, immediate_action( Predicate, Action) ) ).
get_action( _, _, _).
		
process_action_body( _, []).
process_action_body( SId, [ First| Rest]):-	not( comparison( First) ),
			not( complex_term( First) ),
			not( special_predicate( First) ), 
			get_action( SId, _, First).
process_action_body( SId, [ _| Rest]):-	process_action_body( SId, Rest).

%--------------------------------------------------------------------------------------------
% evaluates the ActionResults and selects the new immediate actions in regards with session SId

filter_policy2( SId, ActionResults, ActionList):-		assert( session( SId, counter( 0) ) ),
				rename_rules( SId, rule, rule0 ),
				process_action_results( SId, ActionResults),
				retract( session( SId, counter( N) ) ),
				get_name( rule, N, Name),
				rename_rules( SId, Name, rule),
				select_actions( SId, ActionList).

process_action_results( SId, []).
process_action_results( SId, [ First| Rest] ):-		retract( session( SId, counter( N) ) ),
				NewN is N+1,
				assert( session( SId, counter( NewN) ) ),
				get_name( rule, N, OldName),
				get_name( rule, NewN, NewName),
				findall( _, evaluate_action_result( SId, First, OldName, NewName), _),
				process_action_results( SId, Rest),
				!.	

evaluate_action_result( SId, ActionResult, OldName, NewName ):-	F1 =.. [ OldName, Id, Head, Body],
					retract( session( SId, F1 ) ),
					delete( ActionResult, Body, NewBody), 
					F2 =.. [ NewName, Id, Head, NewBody], 
					assert( session( SId, F2) ).

%-------------------------------------------------------------------------------------------
%the third phase in the filtering process

filter_policy3( SId):-	relevant_rules2( SId),
		blurring_rules( SId),
		relevant_rules2( SId),
		select_peer_actions( SId),
		add_explanations( SId),
		rename_predicates( SId).

%-------------------------------------------------------------------------------------------
%refinds all the relevant rules for the session identified by SId according to the request for this session.

relevant_rules2( SId ):-	session( SId, request( Head) ),
		findall( _, relevant_rule2( SId, Head), _),
		retractall( session( SId, rule( _, _, _) ) ),
		rename_rules( SId, relevant_rule, rule).

relevant_rule2( SId, Head):-	retract( session( SId, rule( Id, Head, Body) ) ),
		not( metarule( SId, Id, Head, sensitivity, non_applicable) ),
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
%blurring rules

blurring_rules( SId):-	findall( _, blurring_rule( SId), _),
		rename_rules( SId, blurred_rule, rule).

blurring_rule( SId):-	retract( session( SId, rule( Id, Head, Body) ) ),
		not( must_blurr( SId, Id, Head) ),
		process_blurr_body( SId, Body, NewBody),
		assert( session( SId, blurred_rule( Id, Head, NewBody) ) ).

must_blurr( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, provisional_predicate),
			not( metarule( SId, Id, Predicate, evaluation, immediate) ),
			not( metarule( SId, Id, Predicate, actor, peer) ),
			!.

must_blurr( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, state_predicate),
			not( metarule( SId, Id, Predicate, sensitivity, public) ),
			!.

must_blurr( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, abbreviation_predicate),
			not( metarule( SId, Id, Predicate, sensitivity, public) ),
			!.

must_blurr( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, decisional_predicate),
			not( metarule( SId, Id, Predicate, sensitivity, public) ).

process_blurr_body( _, [], []).
process_blurr_body( SId, [ not( First)| Rest1], [ not( blurred( First) )| Rest2]):-	not( comparison( First) ),
						not( complex_term( First) ),
						must_blurr( SId, _, First),
						!,
						process_blurr_body( SId, Rest1, Rest2).

process_blurr_body( SId, [ First| Rest1], [ blurred( First) | Rest2]):-		not( comparison( First) ),
						not( complex_term( First) ),
						must_blurr( SId, _, First),
						!,
						process_blurr_body( SId, Rest1, Rest2).

process_blurr_body( SId, [ First| Rest1], [ First| Rest2]):-			process_blurr_body( SId, Rest1, Rest2).

%--------------------------------------------------------------------------------------------
%select the actions that are to be executed by the peer

select_peer_actions( SId):-	findall( _, select_peer_action( SId), _).

select_peer_action( SId):-	session( SId, rule( Id, Head, Body) ),
		get_peer_action( SId, Id, Head),
		process_peer_action_body( SId, Body).
	
get_peer_action( SId, Id, Predicate):-	metarule( SId, Id, Predicate, type, provisional_predicate),
			metarule( SId, Id, Predicate, actor, peer),
			metarule( SId, Id, Predicate, evaluation, immediate),
			metarule( SId, Id, Predicate, action, Action),
			not( session( SId, metarule( pred, action( Predicate, Action), [] ) ) ),
			assert( session( SId, metarule( pred, action( Predicate, Action), [] ) ) ).
get_peer_action( _, _, _).
		
process_peer_action_body( _, []).
process_peer_action_body( SId, [ First| Rest]):-	not( comparison( First) ),
				not( complex_term( First) ),
				not( special_predicate( First) ), 
				get_peer_action( SId, _, First).
process_peer_action_body( SId, [ _| Rest]):-		process_peer_action_body( SId, Rest).

%--------------------------------------------------------------------------------------------
%add explanations to the session identified by SId for the rules selected

add_explanations( SId):-	findall( _, add_explanation( SId), _).

add_explanation( SId):-	session( SId, rule( Id, Head, Body) ),
		get_explanation( SId, Id, Head),
		process_explanation_body( SId, Body).
	
get_explanation( SId, Id, Head):-	nonvar( Id),
			metarule( id, explanation( Id, Explanation), Body),
			prove_body( SId, Body),
			!,
			not( session( SId, metarule( id, explanation( Id, Explanation), [] ) ) ),
			assert( session( SId, metarule( id, explanation( Id, Explanation), [] ) ) ).

get_explanation( SId, _, Head):-	nonvar( Head),
			metarule( pred, explanation( Head, Explanation), Body),
			prove_body( SId, Body),
			not( session( SId, metarule( pred, explanation( Head, Explanation), [] ) ) ),
			assert( session( SId, metarule( pred, explanation( Head, Explanation), [] ) ) ).

get_explanation( _, _, _).

process_explanation_body( _, []).
process_explanation_body( SId, [ not( First) | Rest]):-	process_explanation_body( SId, [ First | Rest ] ).
process_explanation_body( SId, [ First | Rest]):-	not( comparison( First) ),
				not( complex_term( First) ),
				get_explanation( SId, _, First).
process_explanation_body( SId, [ First | Rest]):-	process_explanation_body( SId, Rest).

%--------------------------------------------------------------------------------------------
%rename the abbreviations predicates for the session identified by SId

rename_predicates( SId):-		assert( session( SId, counter( 0) ) ),
			findall( _, rename_predicate( SId), _),
			retract( session( SId, counter( _) ) ),
			findall( _, rename_explanation( SId), _),
			retractall( session( SId, rename_predicate( _, _) ) ),
			rename_rules( SId, renamed_rule, rule),
			rename_rules( SId, renamed_metarule, metarule).

rename_predicate( SId):-		retract( session( SId, rule( Id, Head, Body) ) ),
			do_rename( SId, Id, Head, NewHead),
			process_rename_body( SId, Body, NewBody),
			assert( session( SId, renamed_rule( Id, NewHead, NewBody) ) ).

process_rename_body( _, [], []).
process_rename_body( SId, [ Predicate| Rest1], [ NewPredicate| Rest2]):-	do_rename( SId, _, Predicate, NewPredicate),
					process_rename_body( SId, Rest1, Rest2).


do_rename( SId, Id, not( Predicate), not( NewPredicate) ):-		must_rename( SId, Id, Predicate, NewPredicate),
					!.
do_rename( SId, Id, blurred( Predicate), blurred( NewPredicate) ):-	must_rename( SId, Id, Predicate, NewPredicate),
					!.
do_rename( SId, Id, Predicate, NewPredicate):-		not( complex_term( Predicate) ),
					not( comparison( Predicate) ),
					must_rename( SId, Id, Predicate, NewPredicate),
					!.
do_rename( SId, Id, Predicate, Predicate).

must_rename( SId, Id, Predicate, NewPredicate):-	metarule( SId, Id, Predicate, type, abbreviation_predicate),
				!,
				Predicate =.. [ Name| Args],
				get_new_name( SId, Name, NewName),
				 NewPredicate =.. [ NewName| Args].

must_rename( SId, Id, Predicate, Predicate).

get_new_name( SId, Name, NewName):-		session( SId, rename_predicate( Name, NewName) ),
				!.
		
get_new_name( SId, Name, NewName):-		retract( session( SId, counter( N) ) ),
				!,
				NewN is N+1,
				assert( session( SId, counter( NewN) ) ),
				get_name( predicat, N, NewName),
				assert( session( SId, rename_predicate( Name,NewName) ) ).

rename_explanation( SId):-	retract( session( SId, metarule( pred, explanation( Predicate, Explanation), [] ) ) ),
		do_rename( SId, Id, Predicate, NewPredicate), 
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

%-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
%the policy

rule(f1,recognized_university('UPB'),[]).
rule(f2,recognized_university('HU'),[]).
rule(f3,recognized_university('EPFL'),[]).

rule(f4,passwd(mirela,alerim),[]).
rule(f5,passwd(dragos,sogard),[]).
rule(f6,passwd(alina,anila),[]).

rule(f7,has_subscription(dragos,'OnlineBooks'),[]).
rule(f8,has_subscription(dragos,'Videotec'),[]).
rule(f9,has_subscription(mirela,'Sonotec'),[]).
rule(f10,has_subscription(mirela,'OnlineBooks'),[]).
rule(f11,has_subscription(alina,'OnlineBooks'),[]).

rule(f12,trusted_organization('EC'),[]).
rule(f13,trusted_organization('EUH'),[]).

rule(f14,price('OnlineBooks','5E'),[]).
rule(f15,price('Videotec','9E'),[]).
rule(f16,price('Sonotec','7E'),[]).

rule(r1,allow(access(Resource)),[valid_credential(C),complex_term(C,type,'Student'),complex_term(C,issuer,U),complex_term(C,public_key,K),recognized_university(U),challenge(K)]).

rule(r2,allow(access(Resource)),[authenticate(U),has_subscription(U,Resource)]).

rule(r3,authenticate(U),[declaration(ad,D),complex_term(D,username,U),complex_term(D,password,P),passwd(U,P)]).

rule(r4,allow(access(Resource)),[european_citizen(X), paid(X,Resource), register(X)]).

rule(r5,european_citizen(X),[valid_credential(C),complex_term(C,owner,X),complex_term(C,type,'European citizen'),complex_term(C,issuer,O),complex_term(C,public_key,K),trusted_organization(O),challenge(K)]).

rule(r6,paid(X,R),[price(R,P),credit_card_payment(X,C,P),logged('X paid P for the resource R')]).

rule(r7,credit_card_payment(X,C,P),[valid_credential(C),complex_term(C,type,'credit_card'),complex_term(C,issuer,'Visa'),complex_term(C,owner,X),charged(C,P)]).

rule(r8,charged(C,P),[not(revoked(C)),not(blacklisted(C)),transfer_money(C,P)]).

rule(r9,register(X),[declaration(rd,D),complex_term(D,username,U),complex_term(D,password,P),check(U,P,X)]).

rule(r10,check(U,_,X),[passwd(U,_),register(X)]).

rule(r11,check(U,P,X),[not(passwd(U,_)),assert(passwd(U,P)),logged('New user: X registered as U')]).

rule(r12,valid_credential(C),[credential(c,C),complex_term(C,issuer,CA),public_key(CA,K),verify_signature(C,K)]).

rule(r13,allow(release(C)),[]).
rule(r13,complex_term(C,type,'member'),[]).
rule(r13,complex_term(C,owner,'Library'),[]).
rule(r13,complex_term(C,issuer,'BBB'),[]).

metarule(pred,sensitivity(allow(_),public),[]).

metarule(pred,type(public_key(_,_),provisional_predicate),[]).
metarule(pred,evaluation(public_key(CA,_),immediate),[ground(CA)]).
metarule(pred,actor(public_key(_,_),self),[]).
metarule(pred,action(public_key(CA,K),'connect to $C server and get $CAs public key $K'),[]).
metarule(pred,sensitivity(public_key(_,_),private),[]).

metarule(pred,type(verify_signature(_,_),provisional_predicate),[]).
metarule(pred,evaluation(verify_signature(C,K),immediate),[ground(C),ground(K)]).
metarule(pred,actor(verify_signature(_,_),self),[]).
metarule(pred,action(verify_signature(C,K),'verify the signature on the credential $C using public key $K'),[]).
metarule(pred,sensitivity(verify_signature(_,_),private),[]).

metarule(pred,type(recognized_university(_),state_predicate),[]).
metarule(pred,evaluation(recognized_university(_),immediate),[]).
metarule(pred,sensitivity(recognized_university(_),public),[]).

metarule(pred,type(authenticate(_),abbreviation_predicate),[]).
metarule(pred,sensitivity(authenticate(_),public),[]).

metarule(pred,type(passwd(_,_),state_predicate),[]).
metarule(pred,evaluation(passwd(_,_),immediate),[]).
metarule(pred,sensitivity(passwd(_,_),private),[]).

metarule(pred,type(has_subscription(_,_),state_predicate),[]).
metarule(pred,evaluation(has_subscription(_,_),immediate),[]).
metarule(pred,sensitivity(has_subscription(_,_),private),[]).

metarule(pred,type(european_citizen(_),abbreviation_predicate),[]).
metarule(pred,sensitivity(european_citizen(_),public),[]).

metarule(pred,type(trusted_organization(_),state_predicate),[]).
metarule(pred,evaluation(trusted_organization(_),immediate),[]).
metarule(pred,sensitivity(trusted_organization(_),public),[]).

metarule(pred,type(paid(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(paid(_,_),public),[]).

metarule(pred,type(price(_,_),state_predicate),[]).
metarule(pred,evaluation(price(_,_),immediate),[]).
metarule(pred,sensitivity(price(_,_),public),[]).

metarule(pred,type(credit_card_payment(_,_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(credit_card_payment(_,_,_),public),[]).

metarule(pred,type(charged(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(charged(_,_),public),[]).

metarule(pred,type(revoked(_),provisional_predicate),[]).
metarule(pred,evaluation(revoked(C),immediate),[ground(C)]).
metarule(pred,actor(revoked(_),self),[]).
metarule(pred,action(revoked(C),'connect to Visa server and check to see if $C was revoked'),[]).
metarule(pred,sensitivity(revoked(_),private),[]).

metarule(pred,type(blacklisted(_),provisional_predicate),[]).
metarule(pred,evaluation(blacklisted(C),immediate),[ground(C)]).
metarule(pred,actor(blacklisted(_),self),[]).
metarule(pred,action(blacklisted(_),'connect to Visa server and check to see if $C is on the black list'),[]).
metarule(pred,sensitivity(blacklisted(_),private),[]).

metarule(pred,type(transfer_money(_,_),provisional_predicate),[]).
metarule(pred,evaluation(transfer_money(C,P),immediate),[ground(C),ground(P)]).
metarule(pred,actor(transfer_money(_,_),self),[]).
metarule(pred,action(transfer_money(C,P),'connect to Visa server and transfer the money $P from $C into the librarys account'),[]).
metarule(pred,sensitivity(transfer_money(_,_),public),[]).

metarule(pred,type(logged(_),provisional_predicate),[]).
metarule(pred,evaluation(logged(X),deferred),[ ]).
metarule(pred,actor(logged(_),self),[]).
metarule(pred,action(logged(M),'write message $M in the log file'),[]).
metarule(pred,sensitivity(logged(_),private),[]).

metarule(pred,type(register(_),abbreviation_predicate),[]).
metarule(pred,sensitivity(register(_),public),[]).

metarule(pred,type(check(_,_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(check(_,_,_),public),[]).

metarule(pred,type(assert(_),provisional_predicate),[]).
metarule(pred,evaluation(assert(X),immediate),[ground(X)]).
metarule(pred,actor(assert(_),self),[]).
metarule(pred,action(assert(X),'add to the database $X'),[]).
metarule(pred,sensitivity(assert(_),public),[]).

metarule(pred,type(valid_credential(_),abbreviation_predicate),[]).
metarule(pred,sensitivity(valid_credential(_),public),[]).

metarule(pred,type(challenge(_),provisional_predicate),[]).
metarule(pred,evaluation(challenge(K),immediate),[ ground(K) ]).
metarule(pred,actor(challenge(_),self),[]).
metarule(pred,action(challenge(K),'challenge peer to prove that he has the private key coresponding to public key $K'),[]).
metarule(pred,sensitivity(challenge(_),public),[]).

%--------------------------------------------------------------------------------------------

%%%relevant_rules

%metarule(pred,sensitivity(paid(_,R),non_applicable),[ metarule( pred, type( paid( _, _), abbreviation_predicate) ) , ground(R) , price( R, P ), P = '5E' ] ).

%rule(r15, predicat( R),[ price( R, P), write(P), P='5E' ] ).
%metarule( pred, sensitivity(paid(_,R),non_applicable), [ predicat(R) ] ).

%predicat:-	assert( session( 0, relevant_rule( r15, received( credential( c1, creditcard ) ), []) ) ),
%	assert( session( 0, relevant_rule( r15, complex_term( creditcarda, type, 'member'),[]) )),
%	assert( session( 0, relevant_rule( r15, complex_term( creditcardb, issuer, 'BBB'),[]) )).
%metarule(pred,sensitivity(paid(_,R),non_applicable),[  complex_term(C,type,'member'), complex_term(C,issuer,'BBB')] ).

%%%select actions

%metarule(pred,type(challenge(_),provisional_predicate),[]).
%metarule(pred,evaluation(challenge(K),immediate),[ ]).
%metarule(pred,actor(challenge(_),self),[]).
%metarule(pred,action(challenge(K),'challenge peer to prove that he has the private key coresponding to public key $K'),[]).
%metarule(pred,sensitivity(challenge(_),public),[]).

%%%select actions for peer

%metarule(pred,type(challenge(_),provisional_predicate),[]).
%metarule(pred,evaluation(challenge(K),immediate),[ ]).
%metarule(pred,actor(challenge(_),peer),[]).
%metarule(pred,action(challenge(K),'challenge peer to prove that he has the private key coresponding to public key $K'),[]).
%metarule(pred,sensitivity(challenge(_),public),[]).

%%% add explanations

%metarule( pred, explanation( recognized_university(_), 'I know you.'), []).
%metarule( id, explanation( r1, 'I allow you.'), []).
%metarule( pred, explanation( authenticate(X), 'I know you.'), []).

%%% rename abbreviation predicates

%-----------------------------------------------------------------------------------------------
%%%testing
give_credential( SId):-	add_to_session( SId, rule( r13, credential( c, 'StudentCard' ), [] ) ),
		add_to_session( SId, rule( r13, complex_term( 'StudentCard', issuer, 'EPFL' ), [] ) ),
		add_to_session( SId, rule( r13, complex_term( 'StudentCard', type, 'Student' ), [] ) ).

%start_session(0,allow(access('OnlineBooks'))).
%filter_policy2(0,[ public_key('EPFL', 717), verify_signature( 'StudentCard' , 717), challenge( 717) ], L).

%task( L):-	start_session(0,allow(access('OnlineBooks'))),
%	give_credential(0),
%	filter_policy1( 0,L).

filter1:-	start_session(0,allow(access('OnlineBooks'))),
	filter_policy1( 0, ActionList),
	filter_policy2( 0, ActionResult, ActionList),
	filter_policy3( 0).

filter2:-	start_session(0,allow(access('OnlineBooks'))),
	give_credential( 0),
	filter_policy1( 0, ActionList),
	filter_policy2( 0, ActionResult, ActionList),
	filter_policy3( 0).

