# LLM4S

A toolkit for writing LLM based applications in Scala. Including both basic few shot calling, tool calling and agentic style.
Harnessing the power of types and Scala we can provide a robust framework for all levels of applications.


# Random Todos

## React loop

## Tool calling handling
Tool calling is a critical integration - make it as simple as possible
### Tool sig generation

e.g. ScalaMeta based tool definition generator

Take a method e.g. 

```scala

/** My tool does some funky things with a & b...
 * @param a: The first thing
 * @param b: The second thing
 */
 def my_tool(a: Int, b: String): ToolResponse = {
 ...
 }
```
Use ScalaMeta to extract the method params / types, doc comment etc and generate an openai tool definition from it.
see: https://github.com/scalameta/scalameta/pull/692
https://github.com/scalameta/scalameta/issues/556

### Tool call mapping
  From the tool call request - map the parameters and invoke the method.

This could be code generation - or reflective?
 Or code generation from the scalameta parse to mean that a lLM request to call a tool 
 can be mapped to the given tool calling arguments.
  
### Docker container based execution

A lot of tools need to be run in a protected environment (e.g. a docker container to prevent
accidental leakage/accidents when for example the LLM tries to do an 'rm -rf /' or similar (even by accident))

