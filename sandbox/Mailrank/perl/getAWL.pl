#!/usr/bin/perl

use DB_File;
use Fcntl;
use strict;
use Digest::MD5 qw(md5 md5_hex md5_base64);

#getting the home-directory from the shell environment
my $home=$ENV{'HOME'};

# the file containing the Berkeley-DB data
my $dbfile="$home/.spamassassin/auto-whitelist";

# the output file with plain mailaddresses
my $outputplain="$home/.spamassassin/awl-plain";

# the output file with hashed (md5) mailaddresses
my $outputhashed="$home/.spamassassin/awl-hashed";

# declaring hash
my %awlhash;

# tie binds a variable to a package class that will provide the implementation for the variable
my $awldb = tie %awlhash, "DB_File", "$dbfile"
	or die "Cannot open $dbfile.";

# The keys of this hash are like
# pamela4701@eudoramail.com|ip=213.41|totscore
# and the values are like
# 8.7472
# test with values(%hash); and keys(%hash);
# every mail address has two entries:
# e.g.
# pamela4701@eudoramail.com|ip=213.41|totscore
# pamela4701@eudoramail.com|ip=213.41
# where totscore is the over-all score (value) and the
# value of the second line is the count
# of mails received from this sender
# write this to a file one entry per line and nice it a little bit
# replace | with ' '
# do it with a hash of hashes, keys are mailaddresses, subkeys are totalscore and score
# IMPORTANT: Every time the hash is accessed it returns the value
# key triples in a different order
# (the triples not the keys and values itself of course)
# just in case you are wondering

my %resulthash;
%resulthash = ();

while (my ($key,$value) = each(%awlhash)) {
	
	# regex that matches the line described above
	if ($key =~ /@.*totscore/i) {
	
		# local array just for splitting the line
		# using only the e-mail address, not the ip
		# result[0] = mailaddress
		# reslut[1] = ip
		my @result = ();
		@result=split(/\|/, $key);
	
		# hash of hashes with e-mail as key and totalscore and score as
		# subkeys
		$resulthash{$result[0]}{totscore}=$value;
		# next line for debugging
		# print "key: ".$result[0]." totalscore: ".$value."\n";
	}
	
	elsif ($key =~ /@.*/i) {
		my @result=();
		@result=split(/\|/, $key);
		# next line for debugging
		# print "key: ".$result[0]." score: ".$value."\n";
		$resulthash{$result[0]}{score}=$value;
	};
};

# open two file handles
open(PLAIN, ">$outputplain");
open(HASHED, ">$outputhashed");

# printing all out via the filehandles
my $address;
my $scores;
foreach $address ( keys %resulthash ) {
	print PLAIN $address." ";
	print HASHED md5_hex($address)." ";
	print PLAIN $resulthash{$address}{score}." ";
	print HASHED $resulthash{$address}{score}." ";
	print PLAIN $resulthash{$address}{totscore};
	print HASHED $resulthash{$address}{totscore};
	print PLAIN "\n";
	print HASHED "\n";
}
close(PLAIN);
close(HASHED);



untie %awlhash;
