package uk.gov.hmrc.cohort

trait Cohort

object CohortCalculator {
  def calculate[T](id: T, availableCohorts: Set[Cohort]): Option[Cohort] = availableCohorts.size match {
    case 0 => None
    case _ => availableCohorts.drop(math.abs(id.hashCode) % availableCohorts.size).headOption
  }
}
