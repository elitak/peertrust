#!/usr/bin/perl

use Net::SMTP;
use Digest::MD5 qw(md5 md5_hex md5_base64);
use strict;

my $command = $ARGV[0]; #can be setValues, getEntryBy{User|Spamaddress|Score|Count}
my $user = $ARGV[1];
my $userhash = md5_hex($user);
my $spamaddress = $ARGV[2];
my $spamhash = md5_hex($spamaddress);
my $score = $ARGV[3];
my $count = $ARGV[4];

my $mailaddress = "brosowski\@l3s.de";
my $recipient = "broso\@stud.uni-hannover.de";
my $subject = "Mailrank request";
my $mailserver = "server1.learninglab.uni-hannover.de";
my $messagebody = "$command:$userhash:$spamhash:$score:$count";

# open the smtp connection

my $smtp = Net::SMTP->new($mailserver)
	or die "No connection to server";

# set the mailaddress as from:
$smtp->mail($mailaddress)
	or return warn $smtp->message;

#set the recipient, use only addresses that ok for the server
$smtp->recipient($recipient,{SkipBad=>1})
	or return warn $smtp->message;

#send the message

#the header
$smtp->data()
	or return warn $smtp->message;
$smtp->datasend("To: $recipient\n");
$smtp->datasend("From: $mailaddress\n");
$smtp->datasend("Subject: $subject\n");
$smtp->datasend("\n");

#the message body
$smtp->datasend($messagebody);
$smtp->datasend(".");
$smtp->dataend();

#quit the communication
$smtp->quit;
