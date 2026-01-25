package org.llm4s.llmconnect.config

import org.llm4s.config.DefaultConfig
import org.llm4s.trace.TracingMode
import org.llm4s.util.Redaction

case class LangfuseConfig(
  url: String = DefaultConfig.DEFAULT_LANGFUSE_URL,
  publicKey: Option[String] = None,
  secretKey: Option[String] = None,
  env: String = DefaultConfig.DEFAULT_LANGFUSE_ENV,
  release: String = DefaultConfig.DEFAULT_LANGFUSE_RELEASE,
  version: String = DefaultConfig.DEFAULT_LANGFUSE_VERSION
) {
  override def toString: String =
    s"LangfuseConfig(url=$url, publicKey=${Redaction.secretOpt(publicKey)}, secretKey=${Redaction.secretOpt(secretKey)}, " +
      s"env=$env, release=$release, version=$version)"
}

case class TracingSettings(
  mode: TracingMode,
  langfuse: LangfuseConfig
)
