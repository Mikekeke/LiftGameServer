package model
import  Variant._

/**
  * Created by Mikekeke on 27-Jan-17.
  */
object TempHolder {
  val questions = List(
    Question(1, "Question #1", "What is question one?",
      Array(Variant(FIRST, "first var"), Variant(SECOND, "second var"),
        Variant(THIRD, "third var - correct"), Variant(FOURTH, "fourth var")),
      3, "answer to question one", QStatus.NDF, "", ""),

    Question(2, "Question #2", "Who is question two?",
      Array(Variant(FIRST, "first var"), Variant(SECOND, "second var - correct"),
        Variant(THIRD, "third var"), Variant(FOURTH, "fourth var")),
      2, "answer to question one", QStatus.NDF, "", "")
  )
}
