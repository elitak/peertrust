# phpMyAdmin SQL Dump
# version 2.5.7-pl1
# http://www.phpmyadmin.net
#
# Host: localhost
# Generation Time: Aug 20, 2004 at 09:23 PM
# Server version: 4.0.20
# PHP Version: 4.3.2
# 
# Database : `mailrank`
# 
CREATE DATABASE `mailrank`;
USE mailrank;

# --------------------------------------------------------

#
# Table structure for table `spam`
#

CREATE TABLE `spam` (
  `ID` bigint(20) NOT NULL default '0',
  `Address` varchar(100) NOT NULL default '',
  `Score` double NOT NULL default '0',
  `Count` bigint(20) NOT NULL default '0',
  KEY `ID` (`ID`,`Address`,`Score`,`Count`)
) TYPE=MyISAM;

# --------------------------------------------------------

#
# Table structure for table `users`
#

CREATE TABLE `users` (
  `ID` bigint(20) NOT NULL auto_increment,
  `User` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`ID`),
  KEY `User` (`User`)
) TYPE=MyISAM AUTO_INCREMENT=8 ;