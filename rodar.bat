@echo off
REM SOL Pintura Eletrostatica - Cadastro de Cliente
REM Compila e executa sem precisar de Maven. Requer apenas o JDK instalado.

if not exist bin mkdir bin
javac -encoding UTF-8 -cp "lib\sqlite-jdbc-3.40.1.0.jar" -d bin src\main\java\br\ufms\sol\Main.java src\main\java\br\ufms\sol\negocio\*.java src\main\java\br\ufms\sol\persistencia\*.java src\main\java\br\ufms\sol\view\*.java
if errorlevel 1 goto erro

java -cp "bin;lib\sqlite-jdbc-3.40.1.0.jar" br.ufms.sol.Main
goto fim

:erro
echo.
echo Falha na compilacao. Verifique se o JDK esta instalado (javac -version).
pause

:fim
