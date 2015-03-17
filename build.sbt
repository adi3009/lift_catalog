name := "Lift Catalog"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.11.2"

resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "https://oss.sonatype.org/content/repositories/releases"
                )

seq(webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.6"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"   %% "lift-record"    % liftVersion, // see if I can use record (mainly for validations) as repository layer on top of slick
    "net.liftmodules"   %% "lift-jquery-module_2.6" % "2.8",
    "com.typesafe.slick" %% "slick" % "3.0.0-RC1",
    "com.h2database" % "h2" % "1.3.170",
    "postgresql"    % "postgresql"  % "9.1-901-1.jdbc4",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty" % "jetty-plus"          % "8.1.7.v20120910"  % "container,test", // For Jetty Config
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "2.3.12"           % "test"
  )
}

