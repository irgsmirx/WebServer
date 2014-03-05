#!/usr/bin/perl -w
use strict;
use warnings;
use CGI qw/:standard/;

my $q = new CGI;

print $q->header;

print $q->start_html(-title   => 'It works!');

print $q->h1("It works!");

print $q->end_html;