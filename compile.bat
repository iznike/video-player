@REM javac %FX%,javafx.media src\*.java -d bin
javac --module-path ".\lib" --add-modules javafx.controls,javafx.fxml,javafx.media src\*.java -d bin