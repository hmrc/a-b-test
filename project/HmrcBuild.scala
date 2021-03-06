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

import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory

object HmrcBuild extends Build {
  import uk.gov.hmrc.DefaultBuildSettings._
  import uk.gov.hmrc.versioning.SbtGitVersioning
  import uk.gov.hmrc.{SbtAutoBuildPlugin, SbtBuildInfo}
  import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

  val appDependencies = Seq(
    "com.typesafe.play" %% "play" % "2.5.19" % "optional",
    "com.typesafe.play" %% "play-test" % "2.5.19" % "test",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "1.5.1"     % "test",
    "org.pegdown" % "pegdown" % "1.4.2" % "test",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )

  lazy val `a-b-test` = (project in file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
    .settings(majorVersion := 3)
    .settings(
      libraryDependencies ++= appDependencies,
      crossScalaVersions := Seq("2.11.6")
//      resolvers := Seq(
//        Resolver.bintrayRepo("hmrc", "releases"),
//        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
//      )
    )
}
