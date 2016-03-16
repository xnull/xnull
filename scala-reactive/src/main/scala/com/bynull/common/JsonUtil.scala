package com.bynull.common

import com.fasterxml.jackson.databind.{SerializationFeature, DeserializationFeature, ObjectMapper, PropertyNamingStrategy}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
  * Created by null on 10/28/15.
  */
object JsonUtil {
  private val ObjectMapper = buildMapper()
  private val UnderscoreObjectMapper = buildUnderscoreMapper()

  private def buildMapper() = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
    mapper
  }

  private def buildUnderscoreMapper() = {
    val mapper = buildMapper()
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
  }

  def fromJson[A](json: String, objectType: Class[A]): A = {
    ObjectMapper.readValue(json, objectType)
  }

  def toJson(data: AnyRef): String = {
    ObjectMapper.writeValueAsString(data)
  }

  def fromJsonWithUnderscore[A](json: String, objectType: Class[A]): A = {
    UnderscoreObjectMapper.readValue(json, objectType)
  }

  def toJsonWithUnderscore(data: AnyRef): String = {
    UnderscoreObjectMapper.writeValueAsString(data)
  }
}
