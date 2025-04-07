# LLM4S Development Guidelines

## Build & Test Commands
```bash
# Build the project
sbt compile

# Run a specific sample
sbt "samples/runMain org.llm4s.samples.basic.BasicLLMCallingExample"

# Run tests
sbt test

# Run a single test
sbt "testOnly org.llm4s.shared.WorkspaceAgentInterfaceTest"

# Format code
sbt scalafmtAll
```

## Code Style Guidelines
- **Formatting**: Follow `.scalafmt.conf` settings (120 char line length)
- **Imports**: Use curly braces for imports (`import { x, y }`)
- **Error Handling**: Use `Either[LLMError, T]` for operations that may fail
- **Types**: Prefer immutable data structures and pure functions
- **Naming**: Use camelCase for variables/methods, PascalCase for classes/objects
- **Documentation**: Use Asterisk style (`/** ... */`) for ScalaDoc comments
- **Code Organization**: Keep consistent with existing package structure
- **Functional Style**: Prefer pattern matching over if/else statements