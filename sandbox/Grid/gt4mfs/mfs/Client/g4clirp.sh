javac -classpath ../MathService/build/stubs/classes/:$CLASSPATH g4clifs/impl/ClientRP.java
java -classpath ../MathService/build/stubs/classes/:$CLASSPATH g4clifs.impl.ClientRP https://127.0.0.1:8443/wsrf/services/ionut/services/MathFactoryService