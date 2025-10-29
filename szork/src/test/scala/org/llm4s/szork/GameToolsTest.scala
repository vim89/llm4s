package org.llm4s.szork

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import upickle.default._

class GameToolsTest extends AnyFlatSpec with Matchers {

  "list_inventory tool" should "work with empty object arguments" in {
    GameTools.clearInventory()

    val tool   = GameTools.listInventoryTool
    val result = tool.execute(ujson.Obj())

    result should be a Symbol("right")
    val jsonResult = result.getOrElse(fail("Expected success"))
    val inventory  = read[GameTools.InventoryListResult](jsonResult)

    inventory.count shouldBe 0
    inventory.inventory shouldBe empty
    inventory.message should include("empty")
  }

  "list_inventory tool" should "work with null arguments" in {
    GameTools.clearInventory()

    val tool   = GameTools.listInventoryTool
    val result = tool.execute(ujson.Null)

    // This documents the current behavior - does it work or fail?
    result match {
      case Right(jsonResult) =>
        val inventory = read[GameTools.InventoryListResult](jsonResult)
        inventory.count shouldBe 0
        println(s"SUCCESS: Zero-parameter tool works with null arguments")

      case Left(error) =>
        println(s"FAILURE: Zero-parameter tool failed with null arguments: $error")
        fail(s"Zero-parameter tool should accept null arguments but got error: $error")
    }
  }

  "list_inventory tool" should "return items after adding them" in {
    GameTools.clearInventory()
    GameTools.setInventory(List("sword", "shield"))

    val tool   = GameTools.listInventoryTool
    val result = tool.execute(ujson.Obj())

    result should be a Symbol("right")
    val jsonResult = result.getOrElse(fail("Expected success"))
    val inventory  = read[GameTools.InventoryListResult](jsonResult)

    inventory.count shouldBe 2
    inventory.inventory should contain theSameElementsAs List("sword", "shield")
  }

  "list_inventory tool schema" should "have no required parameters" in {
    val tool       = GameTools.listInventoryTool
    val openAITool = tool.toOpenAITool(strict = true)

    val functionObj = openAITool("function").obj
    functionObj("name").str shouldBe "list_inventory"

    val parameters = functionObj("parameters").obj
    parameters("type").str shouldBe "object"
    parameters("properties").obj shouldBe empty
    parameters("required").arr shouldBe empty // No required parameters
  }

  "add_inventory_item tool" should "add items correctly" in {
    GameTools.clearInventory()

    val tool = GameTools.addInventoryItemTool
    val result = tool.execute(ujson.Obj("item" -> "torch"))

    result should be a Symbol("right")
    val jsonResult = read[GameTools.InventoryModifyResult](result.getOrElse(fail("Expected success")))

    jsonResult.success shouldBe true
    jsonResult.item shouldBe "torch"
    jsonResult.inventory should contain("torch")
  }

  "remove_inventory_item tool" should "remove items correctly" in {
    GameTools.setInventory(List("sword", "shield"))

    val tool = GameTools.removeInventoryItemTool
    val result = tool.execute(ujson.Obj("item" -> "sword"))

    result should be a Symbol("right")
    val jsonResult = read[GameTools.InventoryModifyResult](result.getOrElse(fail("Expected success")))

    jsonResult.success shouldBe true
    jsonResult.item shouldBe "sword"
    jsonResult.inventory should not contain "sword"
    jsonResult.inventory should contain("shield")
  }
}
