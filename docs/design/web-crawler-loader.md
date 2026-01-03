# WebCrawlerLoader Design Specification

**Status:** Proposed
**Author:** RAG in a Box Team
**Date:** 2026-01-03

## Overview

Add a `WebCrawlerLoader` to llm4s that discovers and loads documents by crawling from seed URLs. This extends the existing `DocumentLoader` abstraction to support automated web content discovery.

## Motivation

The existing `UrlLoader` requires explicit URLs to be provided. For documentation sites, knowledge bases, and other web content, users need a crawler that:

- Starts from seed URLs
- Automatically discovers linked pages
- Respects domain boundaries and robots.txt
- Converts HTML to clean text for RAG ingestion

## API Design

### CrawlerConfig

```scala
package org.llm4s.rag.loader

/**
 * Configuration for web crawling.
 *
 * @param maxDepth Maximum link depth to follow (0 = seed URLs only)
 * @param maxPages Maximum total pages to crawl
 * @param followPatterns URL patterns to follow (glob syntax)
 * @param excludePatterns URL patterns to exclude
 * @param respectRobotsTxt Whether to respect robots.txt
 * @param delayMs Delay between requests (rate limiting)
 * @param timeoutMs Request timeout
 * @param userAgent User agent string
 */
final case class CrawlerConfig(
  maxDepth: Int = 3,
  maxPages: Int = 1000,
  followPatterns: Seq[String] = Seq("*"),
  excludePatterns: Seq[String] = Seq.empty,
  respectRobotsTxt: Boolean = true,
  delayMs: Int = 500,
  timeoutMs: Int = 30000,
  userAgent: String = "LLM4S-Crawler/1.0"
)
```

### WebCrawlerLoader

```scala
package org.llm4s.rag.loader

/**
 * Load documents by crawling from seed URLs.
 *
 * Features:
 * - Breadth-first link discovery
 * - Domain/pattern restrictions
 * - robots.txt support
 * - Rate limiting
 * - HTML to text conversion
 * - Deduplication by URL
 *
 * @param seedUrls Starting URLs to crawl from
 * @param config Crawler configuration
 * @param metadata Additional metadata for all documents
 */
final case class WebCrawlerLoader(
  seedUrls: Seq[String],
  config: CrawlerConfig = CrawlerConfig(),
  metadata: Map[String, String] = Map.empty
) extends DocumentLoader {

  def load(): Iterator[LoadResult] = {
    // Implementation details below
    ???
  }

  override def estimatedCount: Option[Int] = None // Unknown until crawl completes

  def description: String = s"WebCrawlerLoader(${seedUrls.size} seeds, depth=${config.maxDepth})"

  /** Add a seed URL */
  def withSeed(url: String): WebCrawlerLoader = copy(seedUrls = seedUrls :+ url)

  /** Add metadata */
  def withMetadata(m: Map[String, String]): WebCrawlerLoader =
    copy(metadata = metadata ++ m)

  /** Set max depth */
  def withMaxDepth(depth: Int): WebCrawlerLoader =
    copy(config = config.copy(maxDepth = depth))

  /** Set max pages */
  def withMaxPages(pages: Int): WebCrawlerLoader =
    copy(config = config.copy(maxPages = pages))

  /** Set follow patterns */
  def withFollowPatterns(patterns: String*): WebCrawlerLoader =
    copy(config = config.copy(followPatterns = patterns))

  /** Set exclude patterns */
  def withExcludePatterns(patterns: String*): WebCrawlerLoader =
    copy(config = config.copy(excludePatterns = patterns))

  /** Set rate limit delay */
  def withDelay(ms: Int): WebCrawlerLoader =
    copy(config = config.copy(delayMs = ms))
}

object WebCrawlerLoader {
  def apply(url: String): WebCrawlerLoader = WebCrawlerLoader(Seq(url))
  def apply(urls: String*): WebCrawlerLoader = WebCrawlerLoader(urls.toSeq)
}
```

## Implementation Notes

### 1. Link Extraction

Use JSoup for HTML parsing and link extraction:

```scala
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

def extractLinks(html: String, baseUrl: String): Seq[String] = {
  val doc = Jsoup.parse(html, baseUrl)
  doc.select("a[href]").asScala
    .map(_.attr("abs:href"))
    .filter(_.startsWith("http"))
    .toSeq
}
```

### 2. URL Normalization

Normalize URLs to avoid duplicates:

```scala
def normalizeUrl(url: String): String = {
  val uri = new URI(url)
  new URI(
    uri.getScheme.toLowerCase,
    uri.getHost.toLowerCase,
    uri.getPath.stripSuffix("/"),
    null  // Remove fragment
  ).toString
}
```

### 3. robots.txt Support

Parse and cache robots.txt for each domain:

```scala
class RobotsTxtCache {
  private val cache = mutable.Map[String, RobotsTxt]()

  def isAllowed(url: String, userAgent: String): Boolean = {
    val domain = new URI(url).getHost
    val robots = cache.getOrElseUpdate(domain, fetchRobotsTxt(domain))
    robots.isAllowed(url, userAgent)
  }
}
```

### 4. Content Extraction

Convert HTML to clean text, preserving structure hints:

```scala
def extractContent(html: String, url: String): Document = {
  val doc = Jsoup.parse(html, url)

  // Remove non-content elements
  doc.select("script, style, nav, header, footer, aside").remove()

  // Extract title and main content
  val title = doc.title()
  val content = doc.body().text()

  Document(
    id = url,
    content = content,
    metadata = Map(
      "title" -> title,
      "url" -> url,
      "source" -> "web-crawler"
    ),
    hints = Some(DocumentHints.prose)
  )
}
```

### 5. Breadth-First Crawling

Use BFS to ensure shallow pages are crawled first:

```scala
def load(): Iterator[LoadResult] = new Iterator[LoadResult] {
  private val visited = mutable.Set[String]()
  private val queue = mutable.Queue[(String, Int)]() // (url, depth)
  private var pageCount = 0

  // Initialize with seed URLs
  seedUrls.foreach(url => queue.enqueue((normalizeUrl(url), 0)))

  def hasNext: Boolean =
    queue.nonEmpty && pageCount < config.maxPages

  def next(): LoadResult = {
    val (url, depth) = queue.dequeue()
    pageCount += 1

    // Rate limiting
    Thread.sleep(config.delayMs)

    fetchAndProcess(url, depth)
  }

  private def fetchAndProcess(url: String, depth: Int): LoadResult = {
    // Fetch page, extract content, discover links
    // Add new links to queue if depth < maxDepth
    ???
  }
}
```

### 6. Pattern Matching

Support glob patterns for URL filtering:

```scala
def matchesPattern(url: String, patterns: Seq[String]): Boolean = {
  patterns.exists { pattern =>
    val regex = pattern
      .replace(".", "\\.")
      .replace("*", ".*")
    url.matches(regex)
  }
}

def shouldFollow(url: String): Boolean = {
  val matchesFollow = config.followPatterns.isEmpty ||
    matchesPattern(url, config.followPatterns)
  val matchesExclude = matchesPattern(url, config.excludePatterns)

  matchesFollow && !matchesExclude
}
```

## Dependencies

Add to `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.17.2"  // HTML parsing
)
```

## Usage Examples

### Basic Usage

```scala
import org.llm4s.rag.loader._

// Crawl a documentation site
val loader = WebCrawlerLoader("https://docs.example.com")
  .withMaxDepth(3)
  .withMaxPages(500)

val rag = RAG.builder()
  .withDocuments(loader)
  .build()
```

### With Patterns

```scala
// Only crawl specific paths
val loader = WebCrawlerLoader("https://docs.example.com")
  .withFollowPatterns("docs.example.com/guide/*", "docs.example.com/api/*")
  .withExcludePatterns("*/changelog/*", "*/archive/*")
  .withMaxDepth(5)
```

### Combining with Other Loaders

```scala
val webDocs = WebCrawlerLoader("https://docs.example.com")
val localDocs = DirectoryLoader("./docs")

val combined = webDocs ++ localDocs

val rag = RAG.builder()
  .withDocuments(combined)
  .build()
```

## Testing Strategy

1. **Unit Tests**
   - URL normalization
   - Pattern matching
   - Link extraction from HTML

2. **Integration Tests**
   - Mock HTTP server for controlled crawling
   - robots.txt parsing and respect
   - Depth limiting
   - Page count limiting

3. **Sample Application**
   - Add to `modules/samples` with a real-world crawl example

## File Structure

```
modules/core/src/main/scala/org/llm4s/rag/loader/
├── CrawlerConfig.scala
├── WebCrawlerLoader.scala
└── internal/
    ├── RobotsTxtParser.scala
    ├── HtmlContentExtractor.scala
    └── UrlNormalizer.scala

modules/core/src/test/scala/org/llm4s/rag/loader/
├── WebCrawlerLoaderSpec.scala
├── CrawlerConfigSpec.scala
└── internal/
    ├── RobotsTxtParserSpec.scala
    └── UrlNormalizerSpec.scala
```

## Open Questions

1. **Async/Concurrent Crawling**: Should we support concurrent requests for faster crawling? This would complicate the Iterator-based API.

2. **Sitemap Support**: Should we parse sitemap.xml for more efficient discovery?

3. **JavaScript Rendering**: Should we support JavaScript-heavy sites (would require headless browser)?

## Related Work

- Existing `UrlLoader` - loads explicit URLs
- Existing `DirectoryLoader` - loads from filesystem
- `HTTPTool` - agent tool for HTTP requests (different use case)
