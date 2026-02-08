# Google Summer of Code 2026 - LLM4S Organization Summary

Welcome to the **LLM4S Google Summer of Code 2026** program! We're excited to offer **63 innovative projects** across multiple domains including AI agents, RAG systems, data pipelines, tooling, and applications. Our projects span from beginner-friendly to advanced challenges, providing opportunities for contributors at all skill levels.

---

## Quick Navigation

- [Summary Statistics](#summary-statistics)
- [All Projects Table](#all-projects-table)
- [Mentors & Contact Information](#mentors--contact-information)
- [Project Statistics](#project-statistics)
- [Project Repositories](#project-repositories)
- [Contact & Community](#contact--community)

---

## Summary Statistics

- **Total Projects**: 63
- **Project Sizes**: 
  - Large (350 hours): 52 projects
  - Medium (175 hours): 11 projects
- **Difficulty Levels**:
  - Easy: 3 projects
  - Medium: 30 projects
  - Hard: 30 projects
- **Primary Technology**: Scala, LLMs, AI/ML
- **Main Repositories**: LLM4S, llm4s-tripper, Trumbo

---

## All Projects Table

| # | Project Title | Mentors | Co-mentors | Difficulty | Hours |
|---|--------------|---------|------------|------------|-------|
| 1 | [LLM4S - Human-in-the-Loop Agent Evaluation Framework](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#1-llm4s---human-in-the-loop-agent-evaluation-framework) | Rory Graves | Iyad | Hard | 350 |
| 2 | [LLM4S - RAG Evaluation & Continuous Learning Pipeline](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#2-llm4s---rag-evaluation--continuous-learning-pipeline) | Kannupriya Kalra | Debarshi Kundu | Hard | 350 |
| 3 | [LLM4S - Multi-Agent Planning & Goal Decomposition](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#3-llm4s---multi-agent-planning--goal-decomposition) | Giovanni Ruggiero | Iyad | Hard | 350 |
| 4 | [LLM4S - Agentic Memory & Long-Term Context Management](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#4-llm4s---agentic-memory--long-term-context-management) | Atul S Khot | Vitthal Mirji | Medium | 350 |
| 5 | [LLM4S - Output Grounding & Hallucination Detection](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#5-llm4s---output-grounding--hallucination-detection) | Vamshi Salagala | Rory Graves | Medium | 350 |
| 6 | [LLM4S - Type-Safe Structured Generation & Schema Enforcement](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#6-llm4s---type-safe-structured-generation--schema-enforcement) | Vitthal Mirji | Atul S Khot | Hard | 350 |
| 7 | [LLM4S - Agent Arena: Multiplayer Agent Competition Platform](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#7-llm4s---agent-arena-multiplayer-agent-competition-platform) | Dmitry Mamonov | Vamshi Salagala | Hard | 350 |
| 8 | [LLM4S - Prompt Engineering Workbench](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#8-llm4s---prompt-engineering-workbench) | Prasad Pramod Shimpatwar | Rory Graves | Medium | 350 |
| 9 | [LLM4S - Agent Time-Travel Debugger](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#9-llm4s---agent-time-travel-debugger) | Pritish Yuvraj | Kannupriya Kalra | Hard | 350 |
| 10 | [LLM4S - Self-Documenting Agent (Dogfooding Assistant)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#10-llm4s---self-documenting-agent-dogfooding-assistant) | Satvik Kumar | Kannupriya Kalra | Medium | 350 |
| 11 | [LLM4S - GraphRAG Integration (Knowledge Graph RAG)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#11-llm4s---graphrag-integration-knowledge-graph-rag) | Debarshi Kundu | Prasad Pramod Shimpatwar | Hard | 350 |
| 12 | [LLM4S - Privacy Vault & PII Redaction Layer](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#12-llm4s---privacy-vault--pii-redaction-layer) | Rory Graves | Iyad | Medium | 350 |
| 13 | [LLM4S - Advanced Multimodal RAG (Image & Video Search)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#13-llm4s---advanced-multimodal-rag-image--video-search) | Kannupriya Kalra | Debarshi Kundu | Hard | 350 |
| 14 | [LLM4S – CI/CD Templates for Safe Model Deployments](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#14-llm4s--cicd-templates-for-safe-model-deployments) | Vamshi Salagala | Rory Graves | Medium | 350 |
| 15 | [LLM4S - Edge Agents (Scala Native Runtime)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#15-llm4s---edge-agents-scala-native-runtime) | Vitthal Mirji | Atul S Khot | Hard | 350 |
| 16 | [LLM4S - Collaborative Human-AI Editor (CRDTs)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#16-llm4s---collaborative-human-ai-editor-crdts) | Atul S Khot | Vitthal Mirji | Hard | 350 |
| 17 | [LLM4S - Browser Agent (WASM / Scala.js)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#17-llm4s---browser-agent-wasm--scalajs) | Dmitry Mamonov | Vamshi Salagala | Hard | 350 |
| 18 | [LLM4S - Java-to-Scala 3 Migration Agent](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#18-llm4s---java-to-scala-3-migration-agent) | Prasad Pramod Shimpatwar | Rory Graves | Hard | 350 |
| 19 | [LLM4S - llm4s-embedded (Zero-Setup RAG)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#19-llm4s---llm4s-embedded-zero-setup-rag) | Pritish Yuvraj | Kannupriya Kalra | Medium | 350 |
| 20 | [LLM4S - llm4s-semantic-chunker (Advanced splitting)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#20-llm4s---llm4s-semantic-chunker-advanced-splitting) | Satvik Kumar | Kannupriya Kalra | Medium | 350 |
| 21 | [LLM Change-data-capture (CDC) for prompts/models](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#21-llm-change-data-capture-cdc-for-promptsmodels) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Hard | 350 |
| 22 | [TOON-powered token-efficient I/O for llm4s](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#22-toon-powered-token-efficient-io-for-llm4s) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Medium | 175 |
| 23 | [PII-first prompt firewall](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#23-pii-first-prompt-firewall) | TBD | TBD | Medium | 350 |
| 24 | [Data pipelines & lineage core (v2 ETL)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#24-data-pipelines--lineage-core-v2-etl) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Medium | 175 |
| 25 | [Policy & catalog system (governance)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#25-policy--catalog-system-governance) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Hard | 350 |
| 26 | [LLM4S - First-class Vertex AI provider for Scala](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#31-llm4s---first-class-vertex-ai-provider-for-scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 27 | [LLM4S - Dataform + LLM4S "SQL engineer" assistant](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#32-llm4s---dataform--llm4s-sql-engineer-assistant) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 350 |
| 28 | [LLM4S - TOON boundary format for data+LLM pipelines on GCP](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#33-llm4s---toon-boundary-format-for-datallm-pipelines-on-gcp) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 29 | [LLM4S - TOON data contracts + CI enforcement (artifacted)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#34-llm4s---toon-data-contracts--ci-enforcement-artifacted) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 30 | [LLM4S - Data contracts + schema drift guardrails for BigQuery](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#35-llm4s---data-contracts--schema-drift-guardrails-for-bigquery) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 31 | [LLM4S - Lineage-first ingestion for Spark pipelines (Scala)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#36-llm4s---lineage-first-ingestion-for-spark-pipelines-scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 32 | [LLM4S - CDC-driven reindexing (fresh RAG from changing data)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#37-llm4s---cdc-driven-reindexing-fresh-rag-from-changing-data) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 33 | [LLM4S - High-throughput BigQuery Storage Write sink for telemetry](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#38-llm4s---high-throughput-bigquery-storage-write-sink-for-telemetry) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 34 | [LLM4S - Data-aware "LLM transform" operators (Scala)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#39-llm4s---data-aware-llm-transform-operators-scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 35 | [LLM4S - BigQuery-native Vector RAG (no separate vector DB)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#40-llm4s---bigquery-native-vector-rag-no-separate-vector-db) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 350 |
| 36 | [LLM4S - Prompt + agent observability to BigQuery](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#41-llm4s---prompt--agent-observability-to-bigquery) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 37 | [LLM4S - Dataplex lineage for LLM pipelines (RAG + prompts)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#42-llm4s---dataplex-lineage-for-llm-pipelines-rag--prompts) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 38 | [LLM4S - PII-first ingestion (DLP + Dataplex policies)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#43-llm4s---pii-first-ingestion-dlp--dataplex-policies) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 39 | [LLM4S - RAG ingestion pipeline kit (Vertex AI Vector Search)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#44-llm4s---rag-ingestion-pipeline-kit-vertex-ai-vector-search) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 40 | [LLM4S - Streaming GenAI enrichment with Dataflow](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#45-llm4s---streaming-genai-enrichment-with-dataflow) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 41 | [LLM4S - BigQuery → Maps Datasets builder (Scala)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#46-llm4s---bigquery--maps-datasets-builder-scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 42 | [LLM4S - Place ID Steward + refresh pipeline](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#47-llm4s---place-id-steward--refresh-pipeline) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 43 | [LLM4S - "Polite" Maps API client for Scala (quota/retry/backoff)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#48-llm4s---polite-maps-api-client-for-scala-quotaretrybackoff) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 44 | [Trumbo/Screenwriting IDE - Script Structure Analysis Tool](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#26-trumboscreenwriting-ide---script-structure-analysis-tool) | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 45 | [Trumbo/Screenwriting IDE - Automatic Script File Conversion to Fountain Format using LLM](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#27-trumboscreenwriting-ide---automatic-script-file-conversion-to-fountain-format-using-llm) | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 46 | [Trumbo/Screenwriting IDE - Script Tensions Analysis using LLM](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#28-trumboscreenwriting-ide---script-tensions-analysis-using-llm) | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 47 | [Trumbo/Screenwriting IDE - Script Scenes Dependencies Analysis and Visualisation](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#29-trumboscreenwriting-ide---script-scenes-dependencies-analysis-and-visualisation) | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 48 | [Trumbo/Screenwriting IDE - Typical Screenwriting Mistakes Detection](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#30-trumboscreenwriting-ide---typical-screenwriting-mistakes-detection) | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 49 | [llm4s-tripper - Web UI with Real-time Progress Feedback](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---web-ui-with-real-time-progress-feedback) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 350 |
| 50 | [llm4s-tripper - Multi-LLM Strategy with Persona Configuration](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---multi-llm-strategy-with-persona-configuration) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 51 | [llm4s-tripper - Parallel POI Research with Cats Effect](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---parallel-poi-research-with-cats-effect) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 52 | [llm4s-tripper - Budget Tracking & Expense Management](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---budget-tracking--expense-management) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 53 | [llm4s-tripper - Calendar Integration (Google/Apple Calendar)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---calendar-integration-googleapple-calendar) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 54 | [llm4s-tripper - Real-time Weather Integration](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---real-time-weather-integration) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 55 | [llm4s-tripper - AI-Powered Packing List Generator](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---ai-powered-packing-list-generator) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 56 | [llm4s-tripper - Trip Export & Sharing (PDF/Link)](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---trip-export--sharing-pdflink) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 57 | [llm4s-tripper - Collaborative Trip Planning](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---collaborative-trip-planning) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |
| 58 | [llm4s-tripper - Visa & Travel Requirements Checker](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---visa--travel-requirements-checker) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 59 | [llm4s-tripper - Voice Interface for Trip Planning](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---voice-interface-for-trip-planning) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |
| 60 | [llm4s-tripper - Carbon Footprint Calculator & Eco-Routing](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---carbon-footprint-calculator--eco-routing) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 61 | [llm4s-tripper - Accessibility-Aware Trip Planning](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---accessibility-aware-trip-planning) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 350 |
| 62 | [llm4s-tripper - Cultural Etiquette & Local Tips Guide](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---cultural-etiquette--local-tips-guide) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 63 | [llm4s-tripper - Smart Disruption Handler & Rebooking Assistant](https://github.com/llm4s/llm4s/blob/main/Google%20Summer%20of%20Code/Project%20Ideas/2026.md#llm4s-tripper---smart-disruption-handler--rebooking-assistant) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |

---

## Mentors & Contact Information

Our mentors are experienced developers and researchers passionate about LLMs, Scala, and AI systems. Below is the complete list of mentors with their contact details:

### Primary Mentors

1. **Kannupriya Kalra** *(GSoC Org Admin)*
   - LinkedIn: [linkedin.com/in/kannupriyakalra](https://www.linkedin.com/in/kannupriyakalra/)
   - Email: kannupriyakalra@gmail.com
   - Discord: `kannupriyakalra_46520`
   - Focus: RAG systems, evaluation, llm4s-tripper projects

2. **Rory Graves** *(GSoC Org Admin)*
   - LinkedIn: [linkedin.com/in/roryjgraves](https://www.linkedin.com/in/roryjgraves/)
   - Email: rory.graves@fieldmark.co.uk
   - Discord: `rorybot1`
   - Focus: Agent frameworks, core infrastructure, security

3. **Vitthal Mirji**
   - LinkedIn: [linkedin.com/in/vitthal10](https://www.linkedin.com/in/vitthal10/)
   - Email: vitthalmirji@gmail.com
   - Focus: Data pipelines, GCP integrations, type systems

4. **Giovanni Ruggiero**
   - LinkedIn: [linkedin.com/in/giovanniruggiero](https://www.linkedin.com/in/giovanniruggiero/)
   - Email: giovanni.ruggiero@gmail.com
   - Focus: Multi-agent systems, planning, llm4s-tripper

5. **Iyad**
   - LinkedIn: [linkedin.com/in](https://www.linkedin.com/in)
   - Focus: Agent evaluation, multi-agent systems, security

6. **Atul S Khot**
   - LinkedIn: [linkedin.com/in/atul-s-khot-035b4b1](https://www.linkedin.com/in/atul-s-khot-035b4b1/)
   - Email: atulkhot@gmail.com
   - Focus: Memory systems, data engineering, functional programming

7. **Dmitry Mamonov**
   - LinkedIn: [linkedin.com/in/dmamonov](https://www.linkedin.com/in/dmamonov/)
   - Email: dmitry.s.mamonov@gmail.com
   - Focus: Browser agents, Trumbo screenwriting IDE, gaming platforms

8. **Vamshi Salagala**
   - LinkedIn: [linkedin.com/in/pavanvamsi3](https://www.linkedin.com/in/pavanvamsi3/)
   - Email: pavanvamsi3@gmail.com
   - Focus: Hallucination detection, CI/CD, agent evaluation

9. **Prasad Pramod Shimpatwar**
   - LinkedIn: [linkedin.com/in/prasad-pramod-shimpatwar](https://www.linkedin.com/in/prasad-pramod-shimpatwar/)
   - Email: prasadshimpatwar26@gmail.com
   - Focus: Prompt engineering, migration tools, GraphRAG

10. **Pritish Yuvraj**
    - LinkedIn: [linkedin.com/in/pritishyuvraj](https://www.linkedin.com/in/pritishyuvraj/)
    - Email: pritish.yuvi@gmail.com
    - Focus: Debugging tools, embedded systems

11. **Satvik Kumar**
    - LinkedIn: [linkedin.com/in/satvik-kumar](https://www.linkedin.com/in/satvik-kumar/)
    - Email: satvik.kumar.us@gmail.com
    - Focus: Self-documenting agents, semantic chunking

12. **Debarshi Kundu**
    - LinkedIn: [linkedin.com/in/debarshikundu](https://www.linkedin.com/in/debarshikundu/)
    - Email: debarshi.iiest@gmail.com
    - Focus: RAG evaluation, GraphRAG, multimodal systems

---

## Project Statistics

### By Difficulty Level

| Difficulty | Count | Percentage |
|------------|-------|------------|
| Easy | 3 | 4.8% |
| Medium | 30 | 47.6% |
| Hard | 30 | 47.6% |

### By Project Duration

| Duration | Count | Percentage |
|----------|-------|------------|
| 175 hours (Medium) | 11 | 17.5% |
| 350 hours (Large) | 52 | 82.5% |

### By Focus Area

| Focus Area | Project Count |
|------------|---------------|
| **Core LLM4S Framework** | 20 projects |
| **RAG & Vector Search** | 8 projects |
| **Data Pipelines & Engineering** | 18 projects |
| **llm4s-tripper (Travel Planning)** | 15 projects |
| **Trumbo/Screenwriting IDE** | 5 projects |
| **Tooling & Developer Experience** | 7 projects |

### GCP/Enterprise Integration Projects

18 projects focus on Google Cloud Platform integrations including:
- Vertex AI, BigQuery, Dataflow, Dataplex
- Data lineage, governance, and observability
- Maps API and location-based services

---

## Project Repositories

### Main Repositories

1. **[LLM4S Core](https://github.com/llm4s/llm4s)**
   - Main framework repository
   - Projects: 1-43 (all LLM4S projects grouped together)
   - Agents, RAG, data pipelines, GCP integrations

2. **Trumbo Screenwriting IDE**
   - AI-assisted screenwriting tool
   - Projects: 44-48
   - Script analysis, formatting, and production workflows

3. **llm4s-tripper**
   - AI-powered travel planning application
   - Projects: 49-63
   - Real-world demonstration of LLM4S capabilities

### Related Resources

- **Documentation**: [llm4s.org](https://llm4s.org)
- **API Docs**: [llm4s.org/api](https://llm4s.org/api)
- **Examples**: [llm4s.org/examples](https://llm4s.org/examples)
- **Getting Started**: [llm4s.org/getting-started](https://llm4s.org/getting-started)

---

## Contact & Community

### GSoC Organization Admins

**Kannupriya Kalra**
- Role: GSoC Org Admin
- LinkedIn: [linkedin.com/in/kannupriyakalra](https://www.linkedin.com/in/kannupriyakalra/)
- Email: kannupriyakalra@gmail.com
- Discord: `kannupriyakalra_46520`

**Rory Graves**
- Role: GSoC Org Admin
- LinkedIn: [linkedin.com/in/roryjgraves](https://www.linkedin.com/in/roryjgraves/)
- Email: rory.graves@fieldmark.co.uk
- Discord: `rorybot1`
