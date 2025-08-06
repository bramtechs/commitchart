# commitchart

Personal commit count chart generator without any external Java dependencies.
At least Java 18 is required as it utilizes the built-in Java file server.

```console
./gradlew jar

java -jar commitchart-1.0-SNAPSHOT.jar    1280     720      git.doomhowl-interactive.com        C:\\dev\\_dump
#                                      | width  | height |              caption          |   dir with (bare) repos |
```

![http://localhost:8080/commitchart/2025.png?darkMode=true](dark.png#gh-dark-mode-only)
![http://localhost:8080/commitchart/2025.png?darkMode=false](light.png#gh-light-mode-only)
