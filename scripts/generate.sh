#!/bin/bash

rm client_keystore server_keystore client_cert server_cert

# Generate client public/private key pair into private keystore
keytool -genkey -alias SecureClient -keyalg RSA -keystore client_keystore -dname "cn=alice,ou=Learning Lab Lower Saxony,o=Hannover University,l=Hannover, s=Lower Saxony, c=Germany" -keypass clientpw -storepass clientstorepw

# Generate server public/private key pair into private keystore
keytool -genkey -alias SecureServer -keyalg RSA -keystore server_keystore -dname "cn=elearn,ou=Learning Lab Lower Saxony,o=Hannover University,l=Hannover, s=Lower Saxony, c=Germany" -keypass serverpw -storepass serverstorepw

# Sign client key
keytool -selfcert -alias SecureClient -keystore client_keystore -keypass clientpw -storepass clientstorepw

# Sign server key
keytool -selfcert -alias SecureServer -keystore server_keystore -keypass serverpw -storepass serverstorepw

# export client public key into a certificate
keytool -export -alias SecureClient -file client_cert -keystore client_keystore -storepass clientstorepw

# export server public key into a certificate
keytool -export -alias SecureServer -file server_cert -keystore server_keystore -storepass serverstorepw

#import client public key into server keystore
keytool -import -alias SecureClient -file client_cert -keystore server_keystore -storepass serverstorepw

#import server public key into client keystore
keytool -import -alias SecureServer -file server_cert -keystore client_keystore -storepass clientstorepw

. setClasspath.sh
java org.peertrust.security.credentials.CredentialManager
