#!/usr/bin/perl -w

use IO::Socket::INET;
use Digest::MD5 qw(md5 md5_hex md5_base64);
use strict;

my $command = $ARGV[0];
my $user = $ARGV[1];
my $userhash = md5_hex($user);
my $address = $ARGV[2];
my $addresshash = md5_hex($address);
my $count = $ARGV[4];
my $score = $ARGV[3];

my $remote_host="127.0.0.1";
my $remote_port=4444;

if( $command eq "getEntryByUser" && $ARGV[1] ne "-f") {
    if ( $user eq "" ) { die "Usage: $0 getEntryByUser <mailaddress>"; }

    my $socket = IO::Socket::INET->new(PeerAddr => $remote_host,
                                 PeerPort => $remote_port,
                                 Proto    => "tcp",
                                 Type     => SOCK_STREAM)
         or die "Couldn't connect to $remote_host:$remote_port : $!\n";

    # No Buffering
    $socket->autoflush(1);

    print $socket "getEntryByUser:$userhash\n";

    #while ( <$socket> ) { print };
	
	while (defined(my $msg_out = STDIN->getline)) {
		print $socket $msg_out;
		my $msg_in = <$socket>;
		print $msg_in;
}
	
    # and terminate the connection when we're done.
    close($socket);


} elsif ( $command eq "setValues" && $ARGV[1] ne "-f" ) {
      if ( ($user eq '') || ($count eq  '') )
		{ die "Usage: $0 setValues <senderaddress> <address> <score> <count>"; }


        my $socket = IO::Socket::INET->new(PeerAddr => $remote_host,
                                         PeerPort => $remote_port,
                                         Proto    => "tcp",
                                         Type     => SOCK_STREAM)
                 or die "Couldn't connect to $remote_host:$remote_port : $!\n";

            # No Buffering
            $socket->autoflush(1);


    # Send the Mailaddress, score and count to the Server
    # Insert here for loop for reading a file or use sub method
    print $socket "setValues:$userhash:$addresshash:$score:$count\n";

    while ( <$socket> ) { print };
    #my $line;
    #while (defined ($line = <$socket>)) {
    #print $line;
    #}
    # and terminate the connection when we're done.
    close($socket);

} elsif ( $ARGV[1] eq "-f" ) {

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
         
} else { die "Usage: $0 (getEntryBySender|setValues) parameter or $0 (getEntryBySender|setValues) -f <awl-hashed-file>"}

