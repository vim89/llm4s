# Zero-Parameter Tool Fix

## Issue

When an LLM calls a zero-parameter tool (like `list_inventory`) with `null` arguments, the tool execution fails with:

```
Tool call 'list_inventory' received null arguments - expected an object with required parameters
```

This occurs even though zero-parameter tools have no required parameters and should accept `null` as equivalent to an empty object `{}`.

## Root Cause

In `ToolFunction.execute()` method (`modules/core/src/main/scala/org/llm4s/toolapi/ToolFunction.scala`), when `args` is `ujson.Null`, it immediately returns `ToolCallError.NullArguments` without checking if the tool actually has any required parameters.

## Solution

Modified `ToolFunction` to:

1. **Added helper method** `hasRequiredParameters` to check if a tool has any required parameters by inspecting the schema
2. **Updated `execute()` method** to treat `null` arguments as `ujson.Obj()` for zero-parameter tools
3. **Updated `executeEnhanced()` method** with the same logic for consistency

### Code Changes

File: `modules/core/src/main/scala/org/llm4s/toolapi/ToolFunction.scala`

```scala
/**
 * Helper to check if this tool has any required parameters
 */
private def hasRequiredParameters: Boolean = schema match {
  case objSchema: ObjectSchema[_] =>
    objSchema.properties.exists(_.required)
  case _ =>
    // Non-object schemas are considered to have required parameters
    true
}

def execute(args: ujson.Value): Either[ToolCallError, ujson.Value] =
  args match {
    case ujson.Null =>
      // For zero-parameter tools, treat null as empty object
      if (!hasRequiredParameters) {
        val extractor = SafeParameterExtractor(ujson.Obj())
        handler(extractor) match {
          case Right(result) => Right(writeJs(result))
          case Left(error)   => Left(ToolCallError.HandlerError(name, error))
        }
      } else {
        Left(ToolCallError.NullArguments(name))
      }
    case _ =>
      // Normal execution path
      ...
  }
```

## Behavior

### Before Fix
- Zero-parameter tool with `null` arguments → **ERROR**
- Zero-parameter tool with `{}` arguments → ✓ Success
- Tool with required parameters and `null` arguments → **ERROR**

### After Fix
- Zero-parameter tool with `null` arguments → ✓ Success (treated as `{}`)
- Zero-parameter tool with `{}` arguments → ✓ Success
- Tool with required parameters and `null` arguments → **ERROR** (correct behavior)

## Test Coverage

Created comprehensive test suite in `modules/core/src/test/scala/org/llm4s/toolapi/ZeroParameterToolTest.scala`:

1. ✓ Zero-parameter tool works with empty object arguments
2. ✓ Zero-parameter tool accepts null arguments and treats them as empty object
3. ✓ Zero-parameter tool schema generates correct JSON schema (no required params)
4. ✓ Multiple zero-parameter tools work correctly
5. ✓ Zero-parameter tool handler doesn't need to extract parameters
6. ✓ Tools with required parameters still reject null arguments

All 27 tool API tests pass, confirming no regressions.

## Example

```scala
// Define a zero-parameter tool
val listInventoryTool = ToolBuilder[Map[String, Any], InventoryResult](
  "list_inventory",
  "Returns the current inventory items",
  Schema.`object`[Map[String, Any]]("Tool with no parameters")  // No .withProperty() calls
).withHandler { _ =>
  Right(InventoryResult(List("sword", "shield", "potion"), 3))
}.build()

// Now works with null arguments
val result = listInventoryTool.execute(ujson.Null)
// Returns: Right({"items":["sword","shield","potion"],"count":3})
```

## Verification

Run tests to verify:
```bash
sbt "project core" "testOnly org.llm4s.toolapi.ZeroParameterToolTest"
sbt "project core" "testOnly org.llm4s.toolapi.*"
```

All tests should pass.
