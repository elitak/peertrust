#!/usr/bin/perl -w

use IO::Socket::INET;
use Digest::MD5 qw(md5 md5_hex md5_base64);
use strict;

my $user = $ARGV[0];
my $userhash = md5_hex($user);

my $remote_host="127.0.0.1";
my $remote_port=4444;

    my $socket = IO::Socket::INET->new(PeerAddr => $remote_host,
                                 PeerPort => $remote_port,
                                 Proto    => "tcp",
                                 Type     => SOCK_STREAM)
         or die "Couldn't connect to $remote_host:$remote_port : $!\n";

    # No Buffering
    $socket->autoflush(1);

    my $answer;
    
    print $socket "getEntryByUser:$userhash\n";
    print $answer while read($socket,$answer,1024) > 0;

    # and terminate the connection when we're done.
    close($socket);



sub file {
    # The following is part of the getAWL.pl script I wrote:

    #getting the home-directory from the shell environment
    my $home=$ENV{'HOME'};

    # the output file with hashed (md5) mailaddresses
    my $default="$home/.spamassassin/awl-hashed";

    # Reads the file -f <file>
    # otherwise it reads the $default

        if (-f $ARGV[1]) {
            open(FILE, "<$ARGV[1]") or
                die "Can't open $ARGV[1]: $!\n";

            my $line;
            while (defined ($line = <FILE>)) {
            print $line; #change here! sub call for method to handle data
            }
            close FILE

         } else {
            open(DEFAULT, "<$default") or
                die "Can't open $default: $!\n";

            my $line;
            while (defined ($line = <DEFAULT>)) {
            print $line;
            }
            close DEFAULT;
         }
    }     

