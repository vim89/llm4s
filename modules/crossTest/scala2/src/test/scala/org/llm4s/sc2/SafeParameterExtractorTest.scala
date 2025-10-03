package org.llm4s.sc2


import org.llm4s.toolapi.SafeParameterExtractor
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SafeParameterExtractorTest extends AnyFlatSpec with Matchers {

  // Test fixtures
  val simpleJson = ujson.Obj(
    "stringValue" -> "test",
    "intValue"    -> 42,
    "doubleValue" -> 42.5,
    "boolValue"   -> true,
    "arrayValue"  -> ujson.Arr(1, 2, 3),
    "objectValue" -> ujson.Obj("key" -> "value")
  )

  val nestedJson = ujson.Obj(
    "level1" -> ujson.Obj(
      "string" -> "nested",
      "number" -> 100,
      "level2" -> ujson.Obj(
        "array"   -> ujson.Arr("a", "b", "c"),
        "boolean" -> false
      )
    )
  )

  val mixedTypesArray = ujson.Obj(
    "mixed" -> ujson.Arr(
      "string",
      42,
      true,
      ujson.Obj("key" -> "value"),
      ujson.Arr(1, 2, 3)
    )
  )

  "SafeParameterExtractor" should "extract string values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    extractor.getString("stringValue") shouldBe Right("test")

    // Failure cases
    extractor.getString("nonexistent") shouldBe Left("required parameter 'nonexistent' (type: string) is missing (available: arrayValue, boolValue, doubleValue, intValue, objectValue, stringValue)")
    extractor.getString("intValue") shouldBe Left("parameter 'intValue' has wrong type - expected string but got number")
  }

  it should "extract integer values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    extractor.getInt("intValue") shouldBe Right(42)

    // Failure cases
    extractor.getInt("stringValue") shouldBe Left("parameter 'stringValue' has wrong type - expected integer but got string")
    extractor.getInt("doubleValue") shouldBe Right(42) // Should work for whole numbers
  }

  it should "extract double values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    extractor.getDouble("doubleValue") shouldBe Right(42.5)
    extractor.getDouble("intValue") shouldBe Right(42.0)

    // Failure cases
    extractor.getDouble("stringValue") shouldBe Left("parameter 'stringValue' has wrong type - expected number but got string")
  }

  it should "extract boolean values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    extractor.getBoolean("boolValue") shouldBe Right(true)

    // Failure cases
    extractor.getBoolean("stringValue") shouldBe Left("parameter 'stringValue' has wrong type - expected boolean but got string")
  }

  it should "extract array values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    val expectedArr = ujson.Arr(1, 2, 3)
    extractor.getArray("arrayValue") shouldBe Right(expectedArr)

    // Failure cases
    extractor.getArray("stringValue") shouldBe Left("parameter 'stringValue' has wrong type - expected array but got string")
  }

  it should "extract object values correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)

    // Success cases
    val expectedObj = ujson.Obj("key" -> "value")
    extractor.getObject("objectValue") shouldBe Right(expectedObj)

    // Failure cases
    extractor.getObject("stringValue") shouldBe Left("parameter 'stringValue' has wrong type - expected object but got string")
  }

  it should "handle nested paths correctly" in {
    val extractor = SafeParameterExtractor(nestedJson)

    // Success cases
    extractor.getString("level1.string") shouldBe Right("nested")
    extractor.getInt("level1.number") shouldBe Right(100)
    val expectedNestedArr = ujson.Arr("a", "b", "c")
    extractor.getArray("level1.level2.array") shouldBe Right(expectedNestedArr)
    extractor.getBoolean("level1.level2.boolean") shouldBe Right(false)

    // Failure cases
    extractor.getString("level1.nonexistent") shouldBe Left(
      "required parameter 'level1.nonexistent' (type: string) is missing (available: level2, number, string)"
    )
    extractor.getString("level1.string.invalid") shouldBe Left(
      "cannot access parameter 'invalid' because parent 'level1.string' is string, not an object"
    )
  }

  it should "handle empty paths correctly" in {
    val extractor = SafeParameterExtractor(simpleJson)
    extractor.getString("") shouldBe Left("required parameter '' (type: string) is missing (available: arrayValue, boolValue, doubleValue, intValue, objectValue, stringValue)")
  }

  it should "handle null values correctly" in {
    val jsonWithNull = ujson.Obj(
      "nullValue" -> ujson.Null
    )
    val extractor = SafeParameterExtractor(jsonWithNull)
    extractor.getString("nullValue") shouldBe Left("parameter 'nullValue' (type: string) is required but value was null")
  }

  it should "handle special characters in paths" in {
    val jsonWithSpecialChars = ujson.Obj(
      "special.key" -> "value",
      "normal" -> ujson.Obj(
        "special.nested" -> "nested value"
      )
    )
    val extractor = SafeParameterExtractor(jsonWithSpecialChars)

    extractor.getString("special.key") shouldBe Left("required parameter 'special' (type: object) is missing (available: normal, special.key)")
    extractor.getString("normal.special.nested") shouldBe Left(
      "required parameter 'normal.special' (type: object) is missing (available: special.nested)"
    )
  }
}
