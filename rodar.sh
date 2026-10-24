#!/bin/bash
# SOL Pintura Eletrostatica - Cadastro de Cliente
# Compila e executa sem precisar de Maven. Requer apenas o JDK instalado.

set -e
JAR="lib/sqlite-jdbc-3.40.1.0.jar"

mkdir -p bin
javac -encoding UTF-8 -cp "$JAR" -d bin $(find src/main/java -name "*.java")
java -cp "bin:$JAR" br.ufms.sol.Main
