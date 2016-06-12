name := "scala-xpath"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
)

resolvers ++= Seq(
)

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshot")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>git@github.com:mprzysucha/scala-xpath.git</url>
    <connection>scm:git:git@github.com:mprzysucha/scala-xpath.git</connection>
  </scm>
    <developers>
      <developer>
        <id>mprzysucha</id>
        <name>Michal Przysucha</name>
        <url>http://michalprzysucha.com</url>
      </developer>
    </developers>)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://github.com/mprzysucha/scala-xpath"))