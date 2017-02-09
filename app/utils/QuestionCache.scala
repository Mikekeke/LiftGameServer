package utils

import model.Question

/**
  * Created by ibes on 03.02.17.
  */
object QuestionCache {
  private var qNum: Option[Int] = None
  private var q: Option[Question] = None
  private var allQ: Option[Seq[Question]] = None

  def cacheCurrent(qNumber: Int, q1: Question): Unit = {
    q = Some(q1)
    qNum = Some(qNumber)
  }
  def getCurrent = q

  def getNum = qNum.get
  def clear = {
    qNum = None
    q= None
  }

  def cacheAll(questions: Seq[Question]) = allQ = Some(questions)
  def getAll = allQ
}
