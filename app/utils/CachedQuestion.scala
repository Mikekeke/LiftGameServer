package utils

import model.Question

/**
  * Created by ibes on 03.02.17.
  */
object CachedQuestion {
  private var qNum: Option[Int] = None
  private var q: Option[Question] = None
  def get = q
  def cache(qNumber: Int, q1: Question): Unit = {
    q = Some(q1)
    qNum = Some(qNumber)
  }
  def getNum = qNum.get

  def clear = {
    qNum = None
    q= None
  }

}
