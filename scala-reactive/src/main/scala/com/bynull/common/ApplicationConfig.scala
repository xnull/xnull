package com.bynull.common

import java.util.Properties

object ApplicationConfig extends ApplicationConfig {

  private val properties = {
    val p = new Properties()
    p.load(ClassLoader.getSystemResourceAsStream("app.properties"))
    p
  }

  private def cfgParam(paramName: String): String = {
    val property = properties.getProperty(paramName)
    if (property == "") {
      sys.error("Required parameter: " + paramName)
    }
    Option(property).getOrElse(sys.error("Required parameter: " + paramName))
  }

  override def twitterApiKey: String = cfgParam("twitter.api.key")

  override def twitterApiSecret: String = cfgParam("twitter.api.secret")
}

abstract class ApplicationConfig {
  def twitterApiKey: String
  def twitterApiSecret: String
}