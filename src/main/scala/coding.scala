/** human coding of courses into categories based on areas and topics */
object coding {
  type HP    = Double    // Higher education credit Points
  type Area  = String    // General topic
  type Topic = String    // Specific topic (less than 10 per course)
  type Mode  = String    // Type of teaching, e.g. "projekt", "laboration", ""

  case class Course(             // Examples:
    id: String,                  // EDAA45
    hp: HP,                      // 7.5
    name: String,                // Programmering, grundkurs
    areas: Set[Area] = Set(),    // Set("programmering")
    topics: Set[Topic] = Set(),  // Set("kontrollstrukturer", "sekvenser", "tabeller", "mängder", "matriser", "algoritmer", "imperativ programmering", "funktionsprogrammering", "objektorientering")
    modes: Set[Mode] = Set(),    // Set("laboration", "föreläsning", "övning", "skriftlig tentamen")
  )

  val courses: List[Course] = List( //Program C, Informations- och kommunikationsteknik
    Course(id = "EDAA01", hp = 7.5, name = "Programmeringsteknik - fördjupningskurs",
      areas = Set("programmering"),
      topics = Set("användargränssnitt", "abstrakta datatyper", "rekursion", "komplexitetsanalys", "objektorientering")
    ),
    Course(id = "EDAA55", hp = 9.0, name = "Programmeringsteknik",
      areas = Set("programmering"),
      topics = Set("kontrollstrukturer", "sekvenser", "matriser", "algoritmer", "imperativ programmering", "objektorientering", "matlab")
    ),
    Course(id = "EDAF60", hp = 4.5, name = "Objektorienterad modellering och design", areas = Set(), topics = Set()),
    Course(id = "EDAF75", hp = 7.5, name = "Databasteknik", areas = Set(), topics = Set()),
    Course(id = "EDAF90", hp = 7.5, name = "Webbprogrammering", areas = Set(), topics = Set()),
    Course(id = "EITA25", hp = 7.5, name = "Datasäkerhet", areas = Set(), topics = Set()),
    Course(id = "EITA30", hp = 7.5, name = "Informationsöverföring", areas = Set(), topics = Set()),
    Course(id = "EITA50", hp = 7.5, name = "Signalbehandling i multimedia", areas = Set(), topics = Set()),
    Course(id = "EITA55", hp = 7.5, name = "Kommunikationssystem", areas = Set(), topics = Set()),
    Course(id = "EITF05", hp = 4.0, name = "Webbsäkerhet", areas = Set(), topics = Set()),
    Course(id = "EITF70", hp = 6.0, name = "Datorteknik", areas = Set(), topics = Set()),
    Course(id = "EITF95", hp = 4.5, name = "Kösystem", areas = Set(), topics = Set()),
    Course(id = "EITG05", hp = 7.5, name = "Digital kommunikation", areas = Set(), topics = Set()),
    Course(id = "ETSA02", hp = 6.0, name = "Programvaruutveckling - metodik", areas = Set(), topics = Set()),
    Course(id = "ETSF05", hp = 9.0, name = "Internetprotokoll", areas = Set(), topics = Set()),
    Course(id = "ETSF25", hp = 7.5, name = "Affärsdriven programvaruutveckling", areas = Set(), topics = Set()),
    Course(id = "EXTA65", hp = 4.5, name = "Kognition", areas = Set(), topics = Set()),
    Course(id = "FAFA60", hp = 5.0, name = "Fotonik", areas = Set(), topics = Set()),
    Course(id = "FMAA05", hp = 15,  name = "Endimensionell analys", areas = Set(), topics = Set()),
    Course(id = "FMAB20", hp = 6.0, name = "Linjär algebra", areas = Set(), topics = Set()),
    Course(id = "FMAB30", hp = 6.0, name = "Flerdimensionell analys", areas = Set(), topics = Set()),
    Course(id = "FMIF45", hp = 4.0, name = "Hållbarhet och resursanvändning med perspektiv på informations- och kommunikationsteknik", areas = Set(), topics = Set()),
    Course(id = "FMSF55", hp = 7.5, name = "Matematisk statistik, allmän kurs", areas = Set(), topics = Set()),
    Course(id = "FRTF05", hp = 7.5, name = "Reglerteknik, allmän kurs", areas = Set(), topics = Set()),
    Course(id = "MAMA15", hp = 7.5, name = "Interaktionsdesign, grundkurs", areas = Set(), topics = Set()),
    Course(id = "MAMN01", hp = 7.5, name = "Avancerad interaktionsdesign ", areas = Set(), topics = Set()),
  )

}
