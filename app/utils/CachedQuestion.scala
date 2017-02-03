package utils

import model.Question

/**
  * Created by ibes on 03.02.17.
  */
object CachedQuestion {
  var q: Option[Question] = None
  def get = q
  def cache(q1: Question): Unit = q = Some(q1)

}
