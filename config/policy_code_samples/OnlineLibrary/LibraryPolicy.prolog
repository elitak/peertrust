rule(f1,recognized_university(upb),[]).
rule(f2,recognized_university(hu),[]).
rule(f3,recognized_university(epfl),[]).
rule(f4,passwd(mirela,alerim),[]).
rule(f5,passwd(dragos,sogard),[]).
rule(f6,passwd(alina,anila),[]).
rule(f7,has_subscription(dragos,books),[]).
rule(f8,has_subscription(dragos,videotec),[]).
rule(f9,has_subscription(mirela,sonotec),[]).
rule(f10,has_subscription(mirela,books),[]).
rule(f11,has_subscription(alina,books),[]).
rule(f12,trusted_organization(ec),[]).
rule(f13,trusted_organization(euh),[]).
rule(f14,price(books,5),[]).
rule(f15,price(videotec,9),[]).
rule(f16,price(sonotec,7),[]).

rule(r1,allow(access(Resource)),[credential(sa,Student_card),
    complex_term(Student_card,type,student),
    complex_term(Student_card,issuer,I),
    complex_term(Student_card,public_key,K),
    valid_credential(Student_card,I),
    recognized_university(I),challenge(K)]).
    
rule(r2,allow(access(Resource)),[authenticate(U),
    has_subscription(U,Resource)]).

rule(r3,authenticate(U),[declaration(ad,D),complex_term(D,username,U),
    complex_term(D,password,P),passwd(U,P)]).
    
rule(r4,allow(access(Resource)),[european_citizen(X),paid(X,Resource),register(X,U),assert(has_subscription(U,Resource))]).
rule(r5,european_citizen(X),[credential(ea,European_id),complex_term(European_id,owner,X),complex_term(European_id,type,european_citizen),complex_term(European_id,issuer,I),complex_term(European_id,public_key,K),valid_credential(European_id,I),trusted_organization(I),challenge(K)]).
rule(r6,paid(X,Resource),[price(Resource,P),credit_card_payment(X,P),logged("${X} paid ${P} for the resource ${R}")]).
rule(r7,credit_card_payment(X,P),[credential(pc,Credit_card),complex_term(Credit_card,type,credit_card),complex_term(Credit_card,issuer,I),complex_term(Credit_card,owner,X),valid_credential(Credit_card,I),charged(Credit_card,P)]).
rule(r8,charged(C,P),[not_revoked(C),transfer_money(C,P)]).
rule(r9,register(X,U),[declaration(rd,D),complex_term(D,username,U),complex_term(D,password,P),check(U,P,X)]).
rule(r10,check(U,_,X),[passwd(U,_),register(X)]).
rule(r11,check(U,P,X),[not(passwd(U,_)),assert(passwd(U,P)),logged("New user: ${X} registered as ${U}")]).
rule(r12,valid_credential(C,I),[public_key(I,K),verify_signature(C,K)]).

metarule(pred,sensitivity(allow(_),public),[]).
metarule(pred,type(public_key(_,_),provisional_predicate),[]).
metarule(pred,evaluation(public_key(I,_),immediate),[ground(I)]).
metarule(pred,actor(public_key(_,_),self),[]).
metarule(pred,action(public_key(I,K),"connect to ${C} server and get ${I}'s public key ${K}"),[]).
metarule(pred,sensitivity(public_key(_,_),private),[]).
metarule(pred,type(verify_signature(_,_),provisional_predicate),[]).
metarule(pred,evaluation(verify_signature(C,K),immediate),[ground(C),ground(K)]).
metarule(pred,actor(verify_signature(_,_),self),[]).
metarule(pred,action(verify_signature(C,K),"verify the signature on the credential ${C} using public key ${K}"),[]).
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
metarule(pred,type(credit_card_payment(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(credit_card_payment(_,_),public),[]).
metarule(pred,type(charged(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(charged(_,_),public),[]).
metarule(pred,type(not_revoked(_),provisional_predicate),[]).
metarule(pred,evaluation(not_revoked(C),immediate),[ground(C)]).
metarule(pred,actor(not_revoked(_),self),[]).
metarule(pred,action(not_revoked(C),"connect to Visa server and check to see if ${C} was revoked"),[]).
metarule(pred,sensitivity(not_revoked(_),private),[]).
metarule(pred,type(transfer_money(_,_),provisional_predicate),[]).
metarule(pred,evaluation(transfer_money(C,P),immediate),[ground(C),ground(P)]).
metarule(pred,actor(transfer_money(_,_),self),[]).
metarule(pred,action(transfer_money(C,P),"connect to Visa server and transfer the money ${P} from ${C} to the library's account"),[]).
metarule(pred,sensitivity(transfer_money(_,_),public),[]).
metarule(pred,type(logged(_),provisional_predicate),[]).
metarule(pred,evaluation(logged(_),deferred),[]).
metarule(pred,actor(logged(_),self),[]).
metarule(pred,action(logged(M),"write message ${M} in the log file"),[]).
metarule(pred,sensitivity(logged(_),private),[]).
metarule(pred,type(register(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(register(_,_),public),[]).
metarule(pred,type(check(_,_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(check(_,_,_),public),[]).
metarule(pred,type(assert(_),provisional_predicate),[]).
metarule(pred,evaluation(assert(X),immediate),[ground(X)]).
metarule(pred,actor(assert(_),self),[]).
metarule(pred,action(assert(X),"add to the database ${X}"),[]).
metarule(pred,sensitivity(assert(_),public),[]).
metarule(pred,type(valid_credential(_,_),abbreviation_predicate),[]).
metarule(pred,sensitivity(valid_credential(_,_),public),[]).
metarule(pred,type(challenge(_),provisional_predicate),[]).
metarule(pred,evaluation(challenge(K),immediate),[ground(K)]).
metarule(pred,actor(challenge(_),self),[]).
metarule(pred,action(challenge(K),"challenge peer to prove that he has the private key coresponding to public key ${K}"),[]).
metarule(pred,sensitivity(challenge(_),public),[]).
metarule(pred,explanation(not_revoked(C),"The credit card ${C} is checked for revocation."),[]).
metarule(pred,explanation(recognized_university(U),"${U} is a recognized university by the library."),[]).

