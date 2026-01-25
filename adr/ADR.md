## Introduction
An architecture decision record (ADR) is a document that captures an important architecture decision made along with its context and consequences. This document highlights the major decisions made in this project. This will not only be limited to architecture decisions but every major decision.

## Architecture Decision Records (ADR)

## 001. Architecture Pattern: Clean Architecture + MVVM
**Status:** Accepted
**Decision:** 
**Rationale:** 

## 002. Data Fetching: Paging 3 with RemoteMediator
**Status:** 
**Decision:** 
**Rationale:** Provides seamless infinite scrolling and handles the complexity of merging network data into the Room database.

## 003. Dependency Injection: Hilt
**Status:** Accepted
**Decision:** 
**Rationale:**

## 004. Network Layer & API Management
**Status:** Accepted
**Decision:** Use Retrofit with OkHttp and a custom `AuthInterceptor` for TMDB API communication.
**Rationale:** Retrofit is the industry standard for Android networking. OkHttp allows for robust interceptor logic (adding the API key to every request) and custom timeout configurations, ensuring a reliable connection.

## 005. Error Handling Strategy
**Status:** Accepted
**Decision:** Implement a centralized `AppError` sealed class to represent various failure states (Network, Server, Unknown).
**Rationale:** This decouples the UI from raw exceptions. By mapping network results to `AppError` in the Data layer (e.g., in `RemoteMediator`), the UI can react with specific messages or retry logic without knowing the underlying implementation details.

## 006. Code Quality & Static Analysis
**Status:** Accepted
**Decision:** Integrate Detekt for static code analysis.
**Rationale:** Maintains code consistency and prevents common pitfalls (like unused parameters or complex functions) early in the development cycle. It ensures the codebase adheres to the "Senior Developer" standards defined in `AGENTS.md`.

---

### Questions to Guide Your ADR
Use these questions to evaluate every major decision before documenting it:

- **What problem are we solving?** (e.g., "Handling 10,000+ movies without crashing the UI.")
- **What were the alternatives?** (e.g., "Why Paging 3 instead of custom scroll listeners?")
- **What is the "Cost of Change"?** (If we change the database later, how much of the app breaks?)
- **How does this impact the user?** (Does it enable offline support? Smoother scrolling?)
- **How does this impact the developer?** (Does it make testing easier or harder?)
