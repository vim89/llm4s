package org.llm4s.samples.toolapi

import org.llm4s.toolapi._
import upickle.default._

/**
 * Example demonstrating multiple tools with different parameter types
 */
object MultiToolExample {
  // Result types
  case class CalculationResult(result: Double)
  case class SearchResult(query: String, results: Seq[String])

  // Provide implicit readers/writers
  implicit val calculationResultRW: ReadWriter[CalculationResult] = macroRW
  implicit val searchResultRW: ReadWriter[SearchResult]           = macroRW

  def main(args: Array[String]): Unit = {
    // 1. Calculator tool
    val calculatorSchema = Schema
      .`object`[Map[String, Any]]("Calculator parameters")
      .withProperty(
        Schema.property(
          "operation",
          Schema
            .string("Mathematical operation to perform")
            .withEnum(Seq("add", "subtract", "multiply", "divide"))
        )
      )
      .withProperty(
        Schema.property(
          "a",
          Schema.number("First operand")
        )
      )
      .withProperty(
        Schema.property(
          "b",
          Schema.number("Second operand")
        )
      )

    def calculatorHandler(params: SafeParameterExtractor): Either[String, CalculationResult] =
      for {
        operation <- params.getString("operation")
        a         <- params.getDouble("a")
        b         <- params.getDouble("b")
        result <- operation match {
          case "add"      => Right(a + b)
          case "subtract" => Right(a - b)
          case "multiply" => Right(a * b)
          case "divide" =>
            if (b == 0) Left("Division by zero")
            else Right(a / b)
          case _ => Left(s"Unknown operation: $operation")
        }
      } yield CalculationResult(result)

    val calculatorTool = ToolBuilder[Map[String, Any], CalculationResult](
      "calculator",
      "Performs basic arithmetic operations",
      calculatorSchema
    ).withHandler(calculatorHandler).build()

    // 2. Search tool
    val searchSchema = Schema
      .`object`[Map[String, Any]]("Search parameters")
      .withProperty(
        Schema.property(
          "query",
          Schema.string("Search query")
        )
      )
      .withProperty(
        Schema.property(
          "limit",
          Schema
            .integer("Maximum number of results")
            .withRange(Some(1), Some(10))
        )
      )

    def searchHandler(params: SafeParameterExtractor): Either[String, SearchResult] =
      for {
        query <- params.getString("query")
        limit <- params.getInt("limit")
      } yield {
        // Mock search results
        val mockResults = (1 to limit).map(i => s"Result $i for query: $query")
        SearchResult(query, mockResults)
      }

    val searchTool = ToolBuilder[Map[String, Any], SearchResult](
      "search",
      "Searches for information",
      searchSchema
    ).withHandler(searchHandler).build()

    // Create registry with both tools
    val toolRegistry = new ToolRegistry(Seq(calculatorTool, searchTool))

    // Example executions
    val calcRequest = ToolCallRequest(
      functionName = "calculator",
      arguments = ujson.Obj(
        "operation" -> "multiply",
        "a"         -> 5.2,
        "b"         -> 3.0
      )
    )

    val searchRequest = ToolCallRequest(
      functionName = "search",
      arguments = ujson.Obj(
        "query" -> "scala programming",
        "limit" -> 3
      )
    )

    // Execute tool calls
    println("Calculator result:")
    toolRegistry.execute(calcRequest) match {
      case Right(json) => println(json.render(indent = 2))
      case Left(error) => println(s"Error: $error")
    }

    println("\nSearch result:")
    toolRegistry.execute(searchRequest) match {
      case Right(json) => println(json.render(indent = 2))
      case Left(error) => println(s"Error: $error")
    }

    // Generate OpenAI tool definitions
    println("\nTool definitions for OpenAI:")
    println(toolRegistry.getToolDefinitions("openai").render(indent = 2))
  }
}
