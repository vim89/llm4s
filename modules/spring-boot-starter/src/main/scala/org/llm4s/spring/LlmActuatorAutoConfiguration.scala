package org.llm4s.spring

import org.llm4s.javaapi.JLlmClient
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.{
  ConditionalOnBean,
  ConditionalOnClass,
  ConditionalOnMissingBean
}
import org.springframework.context.annotation.{ Bean, Configuration }

@AutoConfiguration(after = Array(classOf[Llm4sAutoConfiguration]))
@Configuration
@ConditionalOnClass(name = Array("org.springframework.boot.actuate.health.HealthIndicator"))
@ConditionalOnBean(Array(classOf[JLlmClient]))
class LlmActuatorAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  def llmHealthIndicator(client: JLlmClient): LlmHealthIndicator =
    new LlmHealthIndicator(client)
}
