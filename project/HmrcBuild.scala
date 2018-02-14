import play.twirl.sbt.Import.TwirlKeys
import play.twirl.sbt.SbtTwirl
import sbt.Keys._
import sbt._

object HmrcBuild extends Build {

  import play.core.PlayVersion
  import uk.gov.hmrc.DefaultBuildSettings._
  import uk.gov.hmrc.NexusPublishing._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  import scala.util.Properties.envOrElse

  val appName = "sso-domains"
  val appOrganization = "uk.gov.hmrc"
  val appVersion = envOrElse("SSO_DOMAINS_VERSION", "999-SNAPSHOT")

  lazy val library = Project(appName, file("."))
    .enablePlugins(SbtTwirl)
    .settings(
      organization := appOrganization,
      version := appVersion
    )
    .settings(scalaSettings: _*)
    .settings(defaultSettings(false): _*)
    .settings(
      targetJvm := "jvm-1.8",
      shellPrompt := ShellPrompt(appName),
      scalaVersion := "2.11.8",
      libraryDependencies ++= dependencies,
      publishArtifact := true,
      publishArtifact in Test := false,
      crossScalaVersions := Seq("2.11.8"))
    .settings(nexusPublishingSettings: _*)
    .settings(SbtBuildInfo(): _*)
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"))
    .settings(TwirlKeys.templateImports ++= Seq("play.api.mvc._", "play.api.data._", "play.api.i18n._", "play.api.templates.PlayMagic._"))
    .settings(unmanagedSourceDirectories in sbt.Compile += baseDirectory.value / "src/main/twirl")

  private val dependencies = Seq(
    "com.typesafe.play" %% "play" % PlayVersion.current,
    "uk.gov.hmrc" %% "play-config" % "5.0.0",
    "uk.gov.hmrc" %% "http-verbs" % "7.2.0",
    "org.scalatest" %% "scalatest" % "3.0.4" % "test",
    "org.mockito" % "mockito-core" % "2.15.0" % "test",
    "uk.gov.hmrc" %% "hmrctest" % "3.0.0" % "test"
  )
}
