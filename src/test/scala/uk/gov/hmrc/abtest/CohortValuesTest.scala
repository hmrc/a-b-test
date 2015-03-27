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

package uk.gov.hmrc.abtest

import org.scalatest.{LoneElement, Matchers, WordSpec}
import play.api.GlobalSettings
import play.api.test.{FakeApplication, WithApplication}

class CohortValuesTest extends WordSpec with Matchers with LoneElement {

  object cohort1 extends Cohort {
    override def name = "cohort1"
  }
  object cohort2 extends Cohort {
    override def name = "cohort2"
  }

  "Loading cohorts from configuration" should {

    "balk when no cohorts enabled" in new WithApplication(FakeApplication(withGlobal = Some(new GlobalSettings {}), additionalConfiguration = Map(
      "abTesting.cohort.cohort1.enabled" -> false,
      "abTesting.cohort.cohort2.enabled" -> false))) with ConfiguredCohortValues[Cohort] {
      def availableValues: List[Cohort] = List(cohort1, cohort2)

      intercept[IllegalArgumentException] {
        verifyConfiguration()
      }
    }

    "default to the only configured cohort" in new WithApplication(FakeApplication(withGlobal = Some(new GlobalSettings {}), additionalConfiguration = Map(
      "abTesting.cohort.cohort1.enabled" -> false,
      "abTesting.cohort.cohort2.enabled" -> true))) with ConfiguredCohortValues[Cohort] {
      def availableValues: List[Cohort] = List(cohort1, cohort2)

      verifyConfiguration()

      cohorts.values should contain only cohort2
    }

    "load multiple cohorts" in new WithApplication(FakeApplication(withGlobal = Some(new GlobalSettings {}), additionalConfiguration = Map(
      "abTesting.cohort.cohort1.enabled" -> true,
      "abTesting.cohort.cohort2.enabled" -> true))) with ConfiguredCohortValues[Cohort] {
      def availableValues: List[Cohort] = List(cohort1, cohort2)

      verifyConfiguration()

      cohorts.values should contain allOf(cohort1, cohort2)
    }
  }
}
