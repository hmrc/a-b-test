/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.abtest

import org.scalatest.{LoneElement, Matchers, WordSpec}
import org.scalatestplus.play.OneServerPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder


class NoneEnabledCohortValuesTest extends WordSpec with Matchers with LoneElement with OneServerPerSuite {

  object cohort1 extends Cohort {
    override def name = "cohort1"
  }

  object cohort2 extends Cohort {
    override def name = "cohort2"
  }

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
        "abTesting.cohort.cohort1.enabled" -> false,
        "abTesting.cohort.cohort2.enabled" -> false))
    .build()

  "Loading cohorts from configuration" should {

    "throw an IllegalArgumentException when no cohorts are enabled" in
      new ConfiguredCohortValues[Cohort] {

        def availableValues: List[Cohort] = List(cohort1, cohort2)

        an[IllegalArgumentException] should be thrownBy verifyConfiguration()
      }
  }
}
class OneEnabledCohortValuesTest extends WordSpec with Matchers with LoneElement with OneServerPerSuite {

  object cohort1 extends Cohort {
    override def name = "cohort1"
  }

  object cohort2 extends Cohort {
    override def name = "cohort2"
  }

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
        "abTesting.cohort.cohort1.enabled" -> false,
        "abTesting.cohort.cohort2.enabled" -> true))
    .build()

  "Loading cohorts from configuration" should {

    "default to the only configured cohort" in
      new ConfiguredCohortValues[Cohort] {

        def availableValues: List[Cohort] = List(cohort1, cohort2)

        verifyConfiguration()

        cohorts.values should contain only cohort2
      }
  }
}
class BothEnabledCohortValuesTest extends WordSpec with Matchers with LoneElement with OneServerPerSuite {

  object cohort1 extends Cohort {
    override def name = "cohort1"
  }

  object cohort2 extends Cohort {
    override def name = "cohort2"
  }

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
        "abTesting.cohort.cohort1.enabled" -> true,
        "abTesting.cohort.cohort2.enabled" -> true))
    .build()

  "Loading cohorts from configuration" should {
    "load multiple cohorts" in
      new ConfiguredCohortValues[Cohort] {

        def availableValues: List[Cohort] = List(cohort1, cohort2)

        verifyConfiguration()

        cohorts.values should contain allOf(cohort1, cohort2)
      }
  }
}
class OnlyOneConfiguredAndEnabledCohortValuesTest extends WordSpec with Matchers with LoneElement with OneServerPerSuite {

  object cohort1 extends Cohort {
    override def name = "cohort1"
  }

  object cohort2 extends Cohort {
    override def name = "cohort2"
  }

  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
        "abTesting.cohort.cohort2.enabled" -> true))
    .build()

  "Loading cohorts from configuration" should {
    "disable a cohort by default/when config is omitted" in
      new ConfiguredCohortValues[Cohort] {

        def availableValues: List[Cohort] = List(cohort1, cohort2)

        cohorts.values should contain only cohort2
      }
  }
}