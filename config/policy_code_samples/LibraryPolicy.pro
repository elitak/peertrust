/*---------------------------------------------------------------------------------------*/
//Facts:

recognized_university('UPB').
recognized_university('HU').
recognized_university('EPFL').

passwd(mirela,alerim).
passwd(dragos,sogard).
passwd(alina,anila).

has_subscription(dragos,'OnlineBooks').
has_subscription(dragos,'Videotec').
has_subscription(mirela,'Sonotec').
has_subscription(mirela,'OnlineBooks').
has_subscription(alina,'OnlineBooks').

trusted_organization('EC').
trusted_organization('EUH').

price('OnlineBooks',5E).
price('Videotec',9E).
price('Sonotec',7E).


/*---------------------------------------------------------------------------------------*/
//Rules:

[r1] allow(access(Resource)):-	valid_credential( C[ type:"Student", issuer:U, public_key:K]),
				recognized_university(U),
				challenge(K).

[r2] allow(access(Resource)):-	authenticate(U),has_subscription(U,Resource).
									
[r3] authenticate(U):-	declaration( ad,D[username:U,password:P]),passwd(U,P).


[r4] allow(access(Resource)):-	european_citizen(X),
				paid(X,Resource),
				register(X).
									
[r5] european_citizen(X):-	valid_credential(C [owner:X,type:"European citizen",issuer:O,public_key:K]),         
				trusted_organization(O),
				challenge(K).
							
[r6] paid(X,R):-		price(R,P),
				credit_card_payment(X,C,P),
				logged('X paid P for the resource R').
				
[r7] credit_card_payment(X,C,P):-	valid_credential(C[type:"credit_card", issuer:'Visa', owner:X]),charged(C,P).
								

[r8] charged(C,P):-	not revoked(C),
			not blacklisted(C),
			transfer_money(C,P).
						
							
[r9] register(X):- 	declaration(rd, D[username:U,password:P]),check(U,P,X).

[r10] check(U,_,X):-	passwd(U,_),register(X).
[r11] check(U,P,X):-	not passwd(U,_),
			assert(passwd(U,P)),
			logged('New user: X registered as U').
					
					
[r12] valid_credential(C):-	credential(C[issuer:CA]),public_key(CA,K),
				verify_signature(C,K).
							
[r13] allow(release(C[type:"member",owner:'Library',issuer:'BBB'])).


							
/*---------------------------------------------------------------------------------------*/
//Metapolicy:		

public_key(_,_).type:provisional_predicate.
public_key(CA,_).evaluation:immediate:-ground(CA).
public_key(_,_).actor:self.
public_key(CA,K).action:"connect to $C server and get $CA's public key $K".

verify_signature(_,_).type:provisional_predicate.
verify_signature(C,K).evaluation:immediate:-ground(C),ground(K).
verify_signature(_,_).actor:self.
verify_signature(C,K).action:"verify the signature on the credential $C using public key $K".

recognized_university(_).type:state_predicate.
recognized_university(_).evaluation:immediate.				
								
authenticate(_).type:abbreviation_predicate.

passwd(_,_).type:state_predicate.
passwd(_,_).evaluation:immediate.
passwd(_,_).sensitivity:private.

has_subscription(_,_).type:state_predicate.
has_subscription(_,_).evaluation:immediate.
has_subscription(_,_).sensitivity:private.

european_citizen():abbreviation_predicate.

trusted_organization(_).type:state_predicate.
trusted_organization(_).evaluation:immediate.

paid(_,_).type:abbreviation_predicate.

price(_,_).type:state_predicate.
price(_,_).evaluation:immediate. 

credit_card_payment(_,_,_).type:abbreviation_predicate.

charged(_,_).type:abbreviation_predicate.
   
revoked(_).type:provisional_predicate.
revoked(C).evaluation:immediate :- ground(C).
revoked(_).actor:self.
revoked(C).action:"connect to Visa server and check to see if $C was revoked".

backlisted(_).type:provisional_predicate.
baclisted(C).evaluation:immediate:-ground(C).
blacklisted(_).actor:self.
blacklisted(_).action:"connect to Visa server and check to see if $C is on the black list".
blacklisted(_).sensitivity:private.

transfer_money(_,_).type:provisional_predicate.
transfer_money(C,P).evaluation:immediate:-ground(C),ground(P).
transfer_money(_,_).actor:self.
transfer_money(C,P).action:"connect to Visa server and transfer the money $P from $C into the library's account".

logged(_).type:provisional_predicate.
logged(_).evaluation:deferred.
logged(_).actor:self.
logged(M).action:"write message $M in the log file".

register(_).type:abbreviation_predicate.

check(_,_,_).type:abbreviation_predicate.	

assert(_).type:provisional_predicate.
assert(X).evaluation:immediate:-ground(X).
assert(_).actor:self.
assert(X).action:"add to the database $X".


valid_credential(_).type:abbreviation_predicate.	
								
challenge(_).type:provisional_predicate.
challenge(K).evaluation:immediate:-ground(K).
challenge(_).actor:self.
challenge(K).action:"challenge peer to prove that he has the private key coresponding to public key $K".
