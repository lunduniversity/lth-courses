# lth-courses

Tools for analyzing courses at the Faculty of Engineering LTH, Lund University.

# How to run this app

## Download

  * Download [master.zip](https://github.com/lunduniversity/lth-courses/archive/master.zip) and unpack, **or** `git clone` if you have git.

  * Make sure you have [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed. Check this by typing in terminal `javac -version`, which should give something similar to `javac 1.8.0_191`

  * Make sure you have `sbt` installed. Check this by typing in terminal `sbt sbtVersion`, which should print some version (any version will do). To install `sbt`:
      - on Linux (debian/Ubuntu): in terminal type `sudo apt-get install sbt`
      - on MacOS: install  and in terminal type: `brew install sbt@1`
      - on Windows: download [msi-installer](https://piccolo.link/sbt-1.2.8.msi)

## Execute

  * Open a terminal and navigate to `lth-courses` where you have downloaded this repo.
  * Type `sbt` and then execute with `run --server localhost 8090` as in this example:
```
$ sbt
sbt:lth-courses> run --server localhost 8090
```
  * Open your browser and surf to "http://localhost:8090"
