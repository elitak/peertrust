#!/usr/bin/perl
##############################################################################
# Authors: Marco Brosowski and Daniel Olmedilla
#
# This script extracts the informatin from the autowhitelist passed as
#   parameter and stores it in a file. It creates two files: one with the
#   e-mail addresses in plain text and the other one with the e-mail
#   addresses hashed
#
##############################################################################

use Fcntl;
use strict;
use Digest::MD5 qw(md5 md5_hex md5_base64);

if ($#ARGV != 0)
{ 
    die "Usage $0 auto-whitelist_file\n\nAuthors: Marco Brosowski and Daniel Olmedilla\nContact: olmedilla\@l3s.de\n" ;
}

# the file containing the DB data
my $dbfile="$ARGV[0]";

# the output file with plain mailaddresses
my $outputplain=$dbfile."-plain";

# the output file with hashed (md5) mailaddresses
my $outputhashed=$dbfile."-hashed";

# declaring hash
my %awlhash;

my $ret = 1 ;

if ($ret == 1)
{
    #die "Cannot open $dbfile: $!" ;
    print "Trying known db formats.\n" ;

    $ret = 0 ;
    use AnyDBM_File ;
    #BEGIN { @AnyDBM_File::ISA = qw(DB_File GDBM_File NDBM_File SDBM_File); }
    print "Trying format AnyDBM\n" ;
    my $awldb = tie %awlhash, "AnyDBM_File", "$dbfile", O_RDONLY, 0600
	or $ret = 1 ;

    if ($ret == 1)
    {
	$ret = 0 ;
	use NDBM_File ;
	print "Trying format NDBM\n" ;
	my $awldb = tie %awlhash, "NDBM_File", "$dbfile", O_RDONLY, 0600
	    or $ret = 1 ;
    }

    if ($ret == 1)
    {
	$ret = 0 ;
	use SDBM_File;
	print "Trying format SDBM\n" ;
	my $awldb = tie %awlhash, "SDBM_File", "$dbfile", O_RDONLY, 0600
	    or $ret = 1 ;
    }

    if ($ret == 1)
    {
	$ret = 0 ;
	use DB_File;
	print "Trying format DB\n" ;
	my $awldb = tie %awlhash, "DB_File", "$dbfile"
	    or $ret = 1 ;
    }

    if ($ret == 1)
    {
	$ret = 0 ;
	use GDBM_File;
	print "Trying format GDBM\n" ;
	my $awldb = tie %awlhash, "GDBM_File", "$dbfile", O_RDONLY, 0600
	    or $ret = 1 ;
    }
    
    if ($ret == 1)
    {
	die "Unknown file db format.\n".'Please inform to olmedilla@l3s.de' ;
    }
}

# tie binds a variable to a package class that will provide the implementation for the variable


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
		# result[1] = ip
		my @result = ();
		@result=split(/\|/, $key);
	
		# hash of hashes with e-mail as key and totalscore and score as
		# subkeys
		$resulthash{$result[0]."|".$result[1]}{totscore}=$value;
	        #$resulthash{$key}{totscore}=$value;
		# next line for debugging
		# print "key: ".$result[0]." totalscore: ".$value."\n";
	}
	
	elsif ($key =~ /@.*/i) {
		my @result=();
		@result=split(/\|/, $key);
		# next line for debugging
		# print "key: ".$result[0]." score: ".$value."\n";
		$resulthash{$result[0]."|".$result[1]}{count}=$value;
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
	print PLAIN $resulthash{$address}{count}." ";
	print HASHED $resulthash{$address}{count}." ";
	print PLAIN $resulthash{$address}{totscore};
	print HASHED $resulthash{$address}{totscore};
	print PLAIN "\n";
	print HASHED "\n";
}
close(PLAIN);
close(HASHED);

untie %awlhash;

print "Files created: $outputplain $outputhashed\n" ;
print "Script finished\n" ;
