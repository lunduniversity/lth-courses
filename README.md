# lth-courses
Tools for analyzing courses at the Faculty of Engineering LTH, Lund University.
# How to run
  * Download `sbt` (using apt-get, homebrew or from https://www.scala-sbt.org/download.html)

  * Type command `sbt` and then `run` or `run --words digital` as in this example:
```
sbt:lth-courses> run
[info] Running Main

  *** COURSES AT LTH 2018_19 ***

                      courses: 1104
           obligatory courses: 491
          alt.-oblig. courses: 93
             elective courses: 691

  filtered obligatory courses: 94 (19%)
  containing at least one of these strings: programmering digital programvara

AADA01,AADA05,AADA10,AADA15,AADA20,AADA25,AAHA60,AAHF35,AAHN15,ASEN01,ASEN05,ASEN15,BMEF05,BMEF15,EDAA01,EDAA10,EDAA20,EDAA30,EDAA35,EDAA45,EDAA50,EDAA55,EDAA60,EDAA65,EDAF05,EDAF15,EDAF20,EDAF25,EDAF40,EDAF45,EDAF55,EDAF60,EDAF75,EDAF85,EDAF90,EDAN15,EEMF15,EIEF30,EIEF35,EITA10,EITA15,EITA20,EITA25,EITA30,EITA35,EITA40,EITA50,EITF05,EITF15,EITF35,EITF55,EITF60,EITF65,EITF70,EITF75,EITF90,EITG01,EITG05,EITN21,EITN75,EITN85,ESSF10,ETIN20,ETIN25,ETIN35,ETIN40,ETIN55,ETIN70,ETSA02,ETSF20,ETSF30,ETTN01,EXTA50,EXTF80,EXTG01,FAFA10,FMAN70,FMEA10,FMNF05,FMNF10,FMNF15,IDEA21,IDEA50,IDEF06,IDEF25,IDEN10,IDEN25,KETF40,MAMN01,MIOF30,MMTF05,MMTF10,MMTF25,TBAA01

[success] Total time: 1 s, completed Jan 13, 2019 10:43:24 PM
sbt:lth-courses> run --words programmering
[info] Running Main --words programmering

  *** COURSES AT LTH 2018_19 ***

                      courses: 1104
           obligatory courses: 491
          alt.-oblig. courses: 93
             elective courses: 691

  filtered obligatory courses: 38 (7%)
  containing at least one of these strings: programmering

EDAA01,EDAA10,EDAA20,EDAA30,EDAA35,EDAA45,EDAA50,EDAA55,EDAA60,EDAA65,EDAF05,EDAF15,EDAF20,EDAF25,EDAF40,EDAF45,EDAF55,EDAF60,EDAF75,EDAF85,EDAF90,EIEF30,EIEF35,EITA15,EITA20,EITA25,EITF05,EITF55,EITF70,ETSA02,ETSF20,EXTA50,FAFA10,FMAN70,FMNF15,MAMN01,MIOF30,MMTF25

[success] Total time: 0 s, completed Jan 13, 2019 10:44:00 PM
sbt:lth-courses>


```
