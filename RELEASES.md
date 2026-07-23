# Release Process

LLM4S follows [Semantic Versioning](https://semver.org/) (`MAJOR.MINOR.PATCH`), e.g. `v0.3.4`.

## How a Release Is Cut

1. A maintainer pushes a tag matching `v[0-9]*` (e.g. `v0.3.5`) to `main`.
2. This triggers the [Release workflow](.github/workflows/release.yml), which:
   - Runs the full CI suite ([`ci.yml`](.github/workflows/ci.yml)) and blocks the release if it fails.
   - Publishes artifacts to [Sonatype Central Portal](https://central.sonatype.com/) via `sbt ci-release`, using [sbt-dynver](https://github.com/sbt/sbt-dynver) to derive the version from the tag and GPG-signing artifacts.
   - Builds and pushes the workspace-runner Docker image to [GitHub Container Registry](https://github.com/llm4s/llm4s/pkgs/container/workspace-runner), tagged with both the release version and `latest`.

## Versioning

- Version numbers are derived automatically from git tags via `sbt-dynver` — there is no manually maintained version file.
- Published artifacts are cross-built for Scala 2.13 and 3.x (`sbt +publish` semantics via `sbt-ci-release`).

## Release Notes

Release notes are maintained as GitHub Releases against each tag: https://github.com/llm4s/llm4s/releases
