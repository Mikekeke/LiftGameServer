package model
import  Variant._

/**
  * Created by Mikekeke on 27-Jan-17.
  */
object TempHolder {
  val questions = List(
    Question(1, "Question #1", "What is question one?",
      Map(FIRST -> Variant(FIRST, "first var"), SECOND -> Variant(SECOND, "second var"),
        THIRD -> Variant(THIRD, "third var - correct"), FOURTH -> Variant(FOURTH, "fourth var")),
      3, "answer to question one", QStatus.NDF, "", ""),

    Question(2, "Question #2", "Who is question two?",
      Map(FIRST -> Variant(FIRST, "first var"), SECOND -> Variant(SECOND, "second var - correct"),
        THIRD -> Variant(THIRD, "third var"), FOURTH -> Variant(FOURTH, "fourth var")),
      2, "answer to question TWO", QStatus.NDF, "", "")
  )
}
