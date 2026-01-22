# Senior Android Developer Best Practices & Guidelines

This document outlines the architectural standards and development practices for this project. As a senior developer or AI agent, adhere to these principles to maintain a robust, scalable, and maintainable codebase.

## 1. Architecture
- **Clean Architecture:** Maintain a strict separation of concerns between Layers: `Data`, `Domain`, and `UI`.
- **UI Patterns:** Use **MVVM** (Model-View-ViewModel) or **MVI** (Model-View-Intent) for predictable state management.
- **Unidirectional Data Flow (UDF):** State should flow down, and events should flow up.
- **Domain Layer:** Business logic should reside in `UseCases` within the domain layer. Keep them reusable and focused.

## 2. UI & Compose
- **Jetpack Compose:** Use Compose for all new UI. Follow the "Stateless Composable" pattern by hoisting state.
- **Theme:** Use the project's `MaterialTheme` (e.g., `MoviesTheme`) for consistent styling. Avoid hardcoded colors or dimensions.
- **Previews:** Provide `@Preview` for different UI states (Loading, Success, Error, Empty) and configurations (Dark Mode).

## 3. Dependency Injection
- **Hilt:** Use Hilt for DI. Scope dependencies correctly (e.g., `@Singleton`, `@ViewModelScoped`).
- **Constructor Injection:** Prefer constructor injection over field injection whenever possible.

## 4. Asynchronous Programming
- **Coroutines & Flow:** Use Kotlin Coroutines for background tasks and `Flow` (specifically `StateFlow` and `SharedFlow`) for reactive data streams.
- **Dispatchers:** Always inject `Dispatchers` to facilitate easier testing. Never hardcode `Dispatchers.IO`.

## 5. Data Handling
- **Repository Pattern:** Use Repositories as the single source of truth for data. They should mediate between different data sources (Network, Local DB, Cache).
- **Paging 3:** For large lists, use the Paging library with `RemoteMediator` for seamless offline support.
- **Room:** Use Room for local persistence. Ensure migrations are handled or schemas are exported if necessary.

## 6. Networking
- **Retrofit:** Use Retrofit for REST APIs. Ensure proper error handling using a result wrapper or custom exceptions.
- **Serialization:** Use `Kotlinx Serialization` for JSON parsing.

## 7. Testing
- **Unit Testing:** Aim for high coverage in `Domain` and `ViewModel` layers. Use `MockK` or `Turbine` (for Flows).
- **UI Testing:** Use Compose Testing library for verifying UI components.
- **Integration Testing:** Test the interaction between Repository and Data Sources.

## 8. Code Quality
- **Kotlin First:** Leverage Kotlin's idiomatic features (extension functions, sealed classes, null safety).
- **Detekt/Lint:** Adhere to the project's static analysis rules. Fix all warnings before submitting PRs.
- **Documentation:** Document complex business logic and public APIs. Keep comments meaningful.

## 9. Modularization
- Organize the project by features where applicable (e.g., `:feature:home`, `:feature:details`, `:core:data`).
- Minimize dependencies between feature modules to reduce build times and improve encapsulation.

## 10. Agent Context & Discovery
- **Recent Files:** Always check the user's recently edited files to understand the current work context and maintain consistency with ongoing changes.
- **Tool Usage:** Utilize `find_files`, `code_search`, and `list_files` to discover relevant components before suggesting changes.
- **Symbol Resolution:** Use `resolve_symbol` and `find_usages` to understand how specific classes or functions are integrated across the project.
- **Build Status:** Verify changes by running `gradle_build` or checking for errors with `analyze_current_file`.
