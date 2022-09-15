package web.domain

import java.text.SimpleDateFormat
import java.util.Date

case class Day(yyyymmdd: String)

object Day {
  
  var referenceDay: Option[Day] = None
  
  def clear(): Unit = {
    referenceDay = None
  }

  def set(yyyymmdd: String): Unit = {
    referenceDay = Some(Day(yyyymmdd))
  }
  
  def today: Day = {
    referenceDay.getOrElse {
      val yyyymmdd = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
      Day(yyyymmdd) 
    }
  }
}
