@echo off
@REM GENERATING KEY
@REM store_password=l3s_demo_kbs
@REM alias=l3s_demo
@REM keystore=keystore_l3s_demo
@REM keypass=l3s_demo
@
rem @set KEYTOOL=D:\j2sdkee1.3.1\bin\keytool
@set KEYTOOL=D:\jdk1.5.0_02\bin\keytool
@set ALIAS=ls3_demo

@set L3S_CER=l3s_demo.cer
@set KEYSTORE=keystore_l3s_demo.kst
@set STOREPASS=l3s_demo_kbs
@set VALIDITY=7305

@set AUTH_DNAME=-dname "CN=SAML_AUTHENTICATION_AUTHORITY, OU=kbs, O=UNI Hannover, L=Hannover,S=NS, C=RFA"

@set KEY_ALG=  
rem -keyalg sunx509

rem======================================================================
@echo key for authority
call 	%KEYTOOL% -genkey %AUTH_DNAME% -alias %ALIAS% -validity %VALIDITY% -keystore %KEYSTORE% -storepass %STOREPASS% -keypass %STOREPASS% %KEY_ALG%


rem ======================================================================
rem self signing the certificates
@echo self signing authority certificate
call %KEYTOOL% -selfcert %AUTH_DNAME% -alias %ALIAS% -validity %VALIDITY%  -keystore %KEYSTORE% -storepass %STOREPASS% -keypass %STOREPASS% %KEY_ALG%


rem =======================================================================
rem exporting certificates
rem call export_public_key_certificate_for_kbs_pjt
@echo exporting certificate for authserver
call %KEYTOOL% -export -keystore %KEYSTORE%  -alias %ALIAS%  -file %L3S_CER% -storepass %STOREPASS%

rem =======================================================================
rem importing certificates
rem call import_public_key_certificate_for_kbs_pjt


