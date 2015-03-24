/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.cohort

import org.scalatest.{WordSpec, Matchers}

class CohortSpec extends WordSpec with Matchers {

  "Cohort calculator" should {
    val id = "an id"
    val anotherId = "another"
    val differentId = 1234

    val cohort = new Cohort() {}
    val anotherCohort = new Cohort() {}

    import CohortCalculator._

    "return None when no cohorts are supplied" in {
        calculate(id, Set()) should be (None)
     }

     "return only available cohort when only one specified" in {
       calculate(id, Set[Cohort](cohort)) should be (Some(cohort))
     }

    "return matching cohort for given id" in {
      calculate(id, Set[Cohort](cohort, anotherCohort)) should be (Some(cohort))
      calculate(anotherId, Set[Cohort](cohort, anotherCohort)) should be (Some(anotherCohort))
    }

    "return matching cohort for an id of any type" in {
      calculate(differentId, Set[Cohort](cohort)) should be (Some(cohort))
    }
  }
}
