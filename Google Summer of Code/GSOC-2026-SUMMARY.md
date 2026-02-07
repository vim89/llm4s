# Google Summer of Code 2026 - LLM4S Organization Summary

Welcome to the **LLM4S Google Summer of Code 2026** program! We're excited to offer **63 innovative projects** across multiple domains including AI agents, RAG systems, data pipelines, tooling, and applications. Our projects span from beginner-friendly to advanced challenges, providing opportunities for contributors at all skill levels.

---

## Quick Navigation

- [Summary Statistics](#summary-statistics)
- [All Projects Table](#all-projects-table)
- [Mentors & Contact Information](#mentors--contact-information)
- [Community Support Team](#community-support-team)
- [Project Statistics](#project-statistics)
- [How to Apply](#how-to-apply)
- [Project Repositories](#project-repositories)
- [Contact & Community](#contact--community)

---

## 📊 Summary Statistics

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

## 📋 All Projects Table

| # | Project Title | Mentors | Co-mentors | Difficulty | Hours |
|---|--------------|---------|------------|------------|-------|
| 1 | LLM4S - Human-in-the-Loop Agent Evaluation Framework | Rory Graves | Iyad | Hard | 350 |
| 2 | LLM4S - RAG Evaluation & Continuous Learning Pipeline | Kannupriya Kalra | Debarshi Kundu | Hard | 350 |
| 3 | LLM4S - Multi-Agent Planning & Goal Decomposition | Giovanni Ruggiero | Iyad | Hard | 350 |
| 4 | LLM4S - Agentic Memory & Long-Term Context Management | Atul S Khot | Vitthal Mirji | Medium | 350 |
| 5 | LLM4S - Output Grounding & Hallucination Detection | Vamshi Salagala | Rory Graves | Medium | 350 |
| 6 | LLM4S - Type-Safe Structured Generation & Schema Enforcement | Vitthal Mirji | Atul S Khot | Hard | 350 |
| 7 | LLM4S - Agent Arena: Multiplayer Agent Competition Platform | Dmitry Mamonov | Vamshi Salagala | Hard | 350 |
| 8 | LLM4S - Prompt Engineering Workbench | Prasad Pramod Shimpatwar | Rory Graves | Medium | 350 |
| 9 | LLM4S - Agent Time-Travel Debugger | Pritish Yuvraj | Kannupriya Kalra | Hard | 350 |
| 10 | LLM4S - Self-Documenting Agent (Dogfooding Assistant) | Satvik Kumar | Kannupriya Kalra | Medium | 350 |
| 11 | LLM4S - GraphRAG Integration (Knowledge Graph RAG) | Debarshi Kundu | Prasad Pramod Shimpatwar | Hard | 350 |
| 12 | LLM4S - Privacy Vault & PII Redaction Layer | Rory Graves | Iyad | Medium | 350 |
| 13 | LLM4S - Advanced Multimodal RAG (Image & Video Search) | Kannupriya Kalra | Debarshi Kundu | Hard | 350 |
| 14 | LLM4S – CI/CD Templates for Safe Model Deployments | Vamshi Salagala | Rory Graves | Medium | 350 |
| 15 | LLM4S - Edge Agents (Scala Native Runtime) | Vitthal Mirji | Atul S Khot | Hard | 350 |
| 16 | LLM4S - Collaborative Human-AI Editor (CRDTs) | Atul S Khot | Vitthal Mirji | Hard | 350 |
| 17 | LLM4S - Browser Agent (WASM / Scala.js) | Dmitry Mamonov | Vamshi Salagala | Hard | 350 |
| 18 | LLM4S - Java-to-Scala 3 Migration Agent | Prasad Pramod Shimpatwar | Rory Graves | Hard | 350 |
| 19 | LLM4S - llm4s-embedded (Zero-Setup RAG) | Pritish Yuvraj | Kannupriya Kalra | Medium | 350 |
| 20 | LLM4S - llm4s-semantic-chunker (Advanced splitting) | Satvik Kumar | Kannupriya Kalra | Medium | 350 |
| 21 | LLM Change-data-capture (CDC) for prompts/models | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Hard | 350 |
| 22 | TOON-powered token-efficient I/O for llm4s | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Medium | 175 |
| 23 | PII-first prompt firewall | TBD | TBD | Medium | 350 |
| 24 | Data pipelines & lineage core (v2 ETL) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Medium | 175 |
| 25 | Policy & catalog system (governance) | Vitthal Mirji | Atul S Khot; Kannupriya Kalra; Rory Graves | Hard | 350 |
| 26 | Trumbo/Screenwriting IDE - Script Structure Analysis Tool | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 27 | Trumbo/Screenwriting IDE - Automatic Script File Conversion to Fountain Format using LLM | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 28 | Trumbo/Screenwriting IDE - Script Tensions Analysis using LLM | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 29 | Trumbo/Screenwriting IDE - Script Scenes Dependencies Analysis and Visualisation | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 30 | Trumbo/Screenwriting IDE - Typical Screenwriting Mistakes Detection | Dmitry Mamonov | Rory Graves; Kannupriya Kalra | Medium | 175 |
| 31 | LLM4S - First-class Vertex AI provider for Scala | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 32 | LLM4S - Dataform + LLM4S "SQL engineer" assistant | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 350 |
| 33 | LLM4S - TOON boundary format for data+LLM pipelines on GCP | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 34 | LLM4S - TOON data contracts + CI enforcement (artifacted) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 35 | LLM4S - Data contracts + schema drift guardrails for BigQuery | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 36 | LLM4S - Lineage-first ingestion for Spark pipelines (Scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 37 | LLM4S - CDC-driven reindexing (fresh RAG from changing data) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 38 | LLM4S - High-throughput BigQuery Storage Write sink for telemetry | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 39 | LLM4S - Data-aware "LLM transform" operators (Scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 40 | LLM4S - BigQuery-native Vector RAG (no separate vector DB) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 350 |
| 41 | LLM4S - Prompt + agent observability to BigQuery | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 42 | LLM4S - Dataplex lineage for LLM pipelines (RAG + prompts) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 43 | LLM4S - PII-first ingestion (DLP + Dataplex policies) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 44 | LLM4S - RAG ingestion pipeline kit (Vertex AI Vector Search) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 45 | LLM4S - Streaming GenAI enrichment with Dataflow | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Hard | 350 |
| 46 | LLM4S - BigQuery → Maps Datasets builder (Scala) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 47 | LLM4S - Place ID Steward + refresh pipeline | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 48 | LLM4S - "Polite" Maps API client for Scala (quota/retry/backoff) | Vitthal Mirji; Rory Graves | Atul S Khot; Kannupriya Kalra | Medium | 175 |
| 49 | llm4s-tripper - Web UI with Real-time Progress Feedback | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 350 |
| 50 | llm4s-tripper - Multi-LLM Strategy with Persona Configuration | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 51 | llm4s-tripper - Parallel POI Research with Cats Effect | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 52 | llm4s-tripper - Budget Tracking & Expense Management | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 53 | llm4s-tripper - Calendar Integration (Google/Apple Calendar) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 54 | llm4s-tripper - Real-time Weather Integration | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 55 | llm4s-tripper - AI-Powered Packing List Generator | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 56 | llm4s-tripper - Trip Export & Sharing (PDF/Link) | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 57 | llm4s-tripper - Collaborative Trip Planning | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |
| 58 | llm4s-tripper - Visa & Travel Requirements Checker | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 59 | llm4s-tripper - Voice Interface for Trip Planning | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |
| 60 | llm4s-tripper - Carbon Footprint Calculator & Eco-Routing | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 175 |
| 61 | llm4s-tripper - Accessibility-Aware Trip Planning | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Medium | 350 |
| 62 | llm4s-tripper - Cultural Etiquette & Local Tips Guide | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Easy | 175 |
| 63 | llm4s-tripper - Smart Disruption Handler & Rebooking Assistant | Kannupriya Kalra | Giovanni Ruggiero; Rory Graves | Hard | 350 |

---

## 👥 Mentors & Contact Information

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

4. **Atul S Khot**
   - LinkedIn: [linkedin.com/in/atul-s-khot-035b4b1](https://www.linkedin.com/in/atul-s-khot-035b4b1/)
   - Email: atulkhot@gmail.com
   - Focus: Memory systems, data engineering, functional programming

5. **Dmitry Mamonov**
   - LinkedIn: [linkedin.com/in/dmamonov](https://www.linkedin.com/in/dmamonov/)
   - Email: dmitry.s.mamonov@gmail.com
   - Focus: Browser agents, Trumbo screenwriting IDE, gaming platforms

6. **Vamshi Salagala**
   - LinkedIn: [linkedin.com/in/pavanvamsi3](https://www.linkedin.com/in/pavanvamsi3/)
   - Email: pavanvamsi3@gmail.com
   - Focus: Hallucination detection, CI/CD, agent evaluation

7. **Prasad Pramod Shimpatwar**
   - LinkedIn: [linkedin.com/in/prasad-pramod-shimpatwar](https://www.linkedin.com/in/prasad-pramod-shimpatwar/)
   - Email: prasadshimpatwar26@gmail.com
   - Focus: Prompt engineering, migration tools, GraphRAG

8. **Pritish Yuvraj**
   - LinkedIn: [linkedin.com/in/pritishyuvraj](https://www.linkedin.com/in/pritishyuvraj/)
   - Email: pritish.yuvi@gmail.com
   - Focus: Debugging tools, embedded systems

9. **Satvik Kumar**
   - LinkedIn: [linkedin.com/in/satvik-kumar](https://www.linkedin.com/in/satvik-kumar/)
   - Email: satvik.kumar.us@gmail.com
   - Focus: Self-documenting agents, semantic chunking

10. **Debarshi Kundu**
    - LinkedIn: [linkedin.com/in/debarshikundu](https://www.linkedin.com/in/debarshikundu/)
    - Email: debarshi.iiest@gmail.com
    - Focus: RAG evaluation, GraphRAG, multimodal systems

11. **Giovanni Ruggiero**
    - LinkedIn: [linkedin.com/in/giovanniruggiero](https://www.linkedin.com/in/giovanniruggiero/)
    - Email: giovanni.ruggiero@gmail.com
    - Focus: Multi-agent systems, planning, llm4s-tripper

12. **Iyad**
    - LinkedIn: [linkedin.com/in](https://www.linkedin.com/in)
    - Focus: Agent evaluation, multi-agent systems, security

---

## Community Support Team

Our community support team is here to help you throughout your GSoC journey:

- **Elvan** - Discord: `elvan_31441`
- **Gopi Trinadh** - Discord: `g3nadh_58439`
- **Subham** - Discord: `vi_shub`
- **Anshuman** - Discord: `anshuman23026`

Feel free to reach out to them on [Discord](https://discord.gg/YRdyYfEw) for general questions and community support!

---

## 📈 Project Statistics

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

## 📝 How to Apply

### Step-by-Step Application Guide

1. **Join the Community**
   - Join our [Discord server](https://discord.gg/YRdyYfEw)
   - Introduce yourself in the #introductions channel
   - Attend weekly LLM4S Dev Hours (Sundays 9am London time) - [Add to your calendar](https://api2.luma.com/ics/get?entity=calendar&id=cal-Zd9BLb5jbZewxLA) (adds all 2026 events)

2. **Register Your Interest**
   - Add your details in the [GSoC 2026 Aspirants Sheet](https://docs.google.com/spreadsheets/d/1GzQDQaFCp4IJz8WlMwiooVDk0JWPh6hvODWRzEZRHxY/edit?gid=0#gid=0)
   - This helps mentors track interested contributors and provide better support

3. **Explore Projects**
   - Review the [complete project list](./Project%20Ideas/2026.md)
   - Read through project descriptions, expected outcomes, and prerequisites
   - Identify 2-3 projects that match your skills and interests

4. **Make Small Contributions**
   - Star the [LLM4S repository](https://github.com/llm4s/llm4s)
   - Look for "good first issue" labels
   - Submit small PRs (documentation, bug fixes, tests)
   - Showcase your Scala knowledge and commitment

5. **Connect with Mentors**
   - Use Discord channels and weekly dev hours (do NOT DM mentors directly)
   - Ask crisp, well-researched questions
   - Show up regularly and participate actively
   - Demonstrate your understanding of the project

6. **Write Your Proposal**
   - Follow Google's [Writing a Proposal guide](https://google.github.io/gsocguides/student/writing-a-proposal)
   - Include:
     - Your background and relevant experience
     - Why you're interested in this specific project
     - Detailed implementation plan with timeline
     - Milestones and deliverables
   - Get feedback from mentors through Discord channels

7. **Submit Your Application**
   - Submit through the official GSoC portal when the application period opens
   - Ensure all required information is included
   - Continue engaging with the community while applications are reviewed

### Important Notes

- **Priority for Beginners**: GSoC is geared towards beginners and those at the start of their careers
- **Learning Experience**: This is not a freelance job - it's a learning opportunity
- **Disadvantaged Backgrounds**: We prioritize contributors who would otherwise not have such opportunities
- **No Direct DMs**: Always use public channels until the program is officially announced

---

## 🔗 Project Repositories

### Main Repositories

1. **[LLM4S Core](https://github.com/llm4s/llm4s)**
   - Main framework repository
   - Projects: 1-25, 31-48
   - Agents, RAG, data pipelines, GCP integrations

2. **[llm4s-tripper](https://github.com/llm4s/llm4s-tripper)**
   - AI-powered travel planning application
   - Projects: 49-63
   - Real-world demonstration of LLM4S capabilities

3. **[Trumbo Screenwriting IDE](https://github.com/dmamonov/trumbo)**
   - AI-assisted screenwriting tool
   - Projects: 26-30
   - Script analysis, formatting, and production workflows

### Related Resources

- **Documentation**: [llm4s.org](https://llm4s.org)
- **API Docs**: [llm4s.org/api](https://llm4s.org/api)
- **Examples**: [llm4s.org/examples](https://llm4s.org/examples)
- **Getting Started**: [llm4s.org/getting-started](https://llm4s.org/getting-started)

---

## 💬 Contact & Community

### Official Communication Channels

- **Discord**: [discord.gg/YRdyYfEw](https://discord.gg/YRdyYfEw) - Primary community hub
- **Weekly Dev Hours**: Sundays 9am London time - [Add to your calendar](https://api2.luma.com/ics/get?entity=calendar&id=cal-Zd9BLb5jbZewxLA) (adds all 2026 events)
- **GitHub Issues**: For technical discussions and bug reports
- **Email**: For official GSoC inquiries → kannupriyakalra@gmail.com or rory.graves@fieldmark.co.uk

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

### What to Expect

- **Response Time**: Discord questions typically answered within 24 hours
- **Dev Hours**: Live Q&A, project discussions, community building
- **Code Review**: Mentor feedback on PRs within 2-3 days
- **Supportive Community**: Friendly, inclusive environment for learning

---

## Why Join LLM4S for GSoC 2026?

### Learn Cutting-Edge Technologies
- Work with state-of-the-art LLM frameworks
- Master functional programming in Scala
- Gain experience with production AI systems

### Real-World Impact
- Contribute to active open-source projects
- Build tools used by developers worldwide
- See your work deployed in production environments

### Mentorship & Growth
- Learn from experienced Scala developers
- Guidance from AI/ML practitioners
- Career development and networking opportunities

### Diverse Project Portfolio
- 63 projects across multiple domains
- Choose based on your interests and skill level
- Flexibility to explore different areas

---

## 📚 Additional Resources

### Program Information
- [GSoC 2026 Rules](https://summerofcode.withgoogle.com/rules)
- [Terms and Conditions](https://summerofcode.withgoogle.com/terms/contributor)
- [GSoC Help Center](https://summerofcode.withgoogle.com/help)

### LLM4S Documentation
- [Installation Guide](https://llm4s.org/getting-started/installation)
- [First Example](https://llm4s.org/getting-started/first-example)
- [Testing Guide](https://llm4s.org/getting-started/testing-guide)
- [Configuration](https://llm4s.org/getting-started/configuration)

### Learning Resources
- [Scala Documentation](https://docs.scala-lang.org)
- [Functional Programming in Scala](https://www.scala-lang.org/documentation/)
- [LLM Fundamentals](https://llm4s.org/guide/)

---

## Next Steps

1. Star the [LLM4S repository](https://github.com/llm4s/llm4s)
2. Join our [Discord community](https://discord.gg/YRdyYfEw)
3. Read the [complete project ideas](./Project%20Ideas/2026.md)
4. Identify projects that match your interests
5. Make your first contribution
6. Attend weekly dev hours
7. Start drafting your proposal

---

**Last Updated**: February 2026  
**Status**: Active - Applications Open  
**Total Projects**: 63  
**Primary Language**: Scala  
**License**: Apache 2.0

For questions or support, join us on [Discord](https://discord.gg/YRdyYfEw) or reach out to our GSoC Org Admins:
- **Kannupriya Kalra** (Discord: `kannupriyakalra_46520`) - kannupriyakalra@gmail.com
- **Rory Graves** (Discord: `rorybot1`) - rory.graves@fieldmark.co.uk

**Good luck with your application! We look forward to working with you! 🚀**
