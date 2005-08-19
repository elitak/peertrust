#!/bin/bash 

# Export key: 
echo exporting key
keytool -export -alias SecureServer -keystore ../server_keystore -storepass serverstorepw -keypass serverpw -file L3S.crt

# Sign JAR:
echo signing jar 
jarsigner -keystore ../server_keystore -storepass serverstorepw -keypass serverpw $1 SecureServer 
