package org.llm4s.imagegeneration.clients

import org.llm4s.imagegeneration._
import org.llm4s.imagegeneration.provider._
import org.scalamock.scalatest.MockFactory
import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import requests.Response

import scala.util.Success

class ImageGenerationClientsTest extends AnyFunSuite with Matchers with MockFactory with EitherValues {

  // Common test data
  val prompt            = "a beautiful sunset"
  val mockResponseBytes = """{"data":[{"b64_json":"base64data","url":null}],"created":1234567890}""".getBytes
  val mockSuccessResponse = Response(
    url = "http://test",
    statusCode = 200,
    statusMessage = "OK",
    data = new geny.Bytes(mockResponseBytes),
    headers = Map.empty,
    history = None
  )

  val mockErrorResponse = Response(
    url = "http://test",
    statusCode = 400,
    statusMessage = "Bad Request",
    data = new geny.Bytes("Error message".getBytes),
    headers = Map.empty,
    history = None
  )

  // ==========================================
  // OpenAI Client Tests
  // ==========================================
  test("OpenAIImageClient should build correct payload and parse response") {
    val mockHttpClient = stub[HttpClient]
    val config         = OpenAIConfig(apiKey = "test-key")
    val client         = new OpenAIImageClient(config, mockHttpClient)

    // Mock successful response with correct JSON structure for OpenAI
    val openAiResponseBytes = """{
      "created": 1589478378,
      "data": [
        {
          "b64_json": "base64encodedimage..."
        }
      ]
    }""".getBytes

    val successResponse = Response(
      url = "https://api.openai.com/v1/images/generations",
      statusCode = 200,
      statusMessage = "OK",
      data = new geny.Bytes(openAiResponseBytes),
      headers = Map.empty,
      history = None
    )

    (mockHttpClient.post _).when(*, *, *, *).returns(Success(successResponse))

    val result = client.generateImage(prompt)

    result.isRight shouldBe true
    result.value.prompt shouldBe prompt
    result.value.data shouldBe "base64encodedimage..."
  }

  test("OpenAIImageClient should handle API errors") {
    val mockHttpClient = stub[HttpClient]
    val config         = OpenAIConfig(apiKey = "test-key")
    val client         = new OpenAIImageClient(config, mockHttpClient)

    (mockHttpClient.post _).when(*, *, *, *).returns(Success(mockErrorResponse))

    val result = client.generateImage(prompt)

    result.isLeft shouldBe true
    result.left.value shouldBe a[ValidationError]
  }

  // ==========================================
  // HuggingFace Client Tests
  // ==========================================
  test("HuggingFaceClient should build correct payload and parse response") {
    val mockHttpClient = stub[HttpClient]
    val config         = HuggingFaceConfig(apiKey = "test-key")
    val client         = new HuggingFaceClient(config, mockHttpClient)

    // HuggingFace returns raw bytes
    val hfResponseBytes = "rawimagebytes".getBytes
    val hfSuccessResponse = Response(
      url = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-xl-base-1.0",
      statusCode = 200,
      statusMessage = "OK",
      data = new geny.Bytes(hfResponseBytes),
      headers = Map.empty,
      history = None
    )

    (mockHttpClient.post _).when(*, *, *, *).returns(Success(hfSuccessResponse))

    val result = client.generateImage(prompt)

    result.isRight shouldBe true
    result.value.prompt shouldBe prompt
    // The client converts raw bytes to base64
    result.value.data should not be empty
  }

  // ==========================================
  // Stable Diffusion Client Tests
  // ==========================================
  test("StableDiffusionClient should build correct payload and parse response") {
    val mockHttpClient = stub[HttpClient]
    val config         = StableDiffusionConfig(baseUrl = "http://localhost:7860")
    val client         = new StableDiffusionClient(config, mockHttpClient)

    // SD WebUI returns logical JSON
    val sdResponseBytes = """{
      "images": ["base64encodedimage..."],
      "parameters": {},
      "info": ""
    }""".getBytes

    val sdSuccessResponse = Response(
      url = "http://localhost:7860/sdapi/v1/txt2img",
      statusCode = 200,
      statusMessage = "OK",
      data = new geny.Bytes(sdResponseBytes),
      headers = Map.empty,
      history = None
    )

    (mockHttpClient.post _).when(*, *, *, *).returns(Success(sdSuccessResponse))

    val result = client.generateImage(prompt)

    result.isRight shouldBe true
    result.value.prompt shouldBe prompt
    result.value.data shouldBe "base64encodedimage..."
  }
}
