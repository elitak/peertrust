#!/usr/bin/perl
use IO::Socket::SSL qw(debug4);
use Net::POP3;

my $username = "broso";
my $password = "";
my $pop3host = "";

my $pop3 = Net::POP3->new($pop3host, Timeout=>30)
	or die "Can't make a connection";

# Login to the pop3 server (plain!!)
my $login = $pop3->login($username, $password)
	or die "Login failed! Maybe wrong username/passwd?";




sub usessl {
#todo
    my $client = new IO::Socket::SSL("server1.learninglab.uni-hannover.de,pop3s", SSL_ca_file=>"/Users/broso/Desktop/l3s.crt")
		or die "Can't get an ssl connection.";

    if (defined $client) {
        print $client "GET / HTTP/1.0\r\n\r\n";
        print <$client>;
        close $client;
    } else {
        warn "I encountered a problem: ",
          IO::Socket::SSL::errstr();
    }
}
