# Movies

A simple Android application to browse popular movies, built with modern Android development practices.

## Description

The Movies App allows users to discover currently popular movies. It fetches data from The Movie Database (TMDB) API and displays it in a user-friendly interface. The app is built using Kotlin and showcases the use of modern Android Jetpack libraries and architectural patterns.

**Key Features:**
*   Browse a paginated list of popular movies.
*   View movie posters.
*   [TBA]

## Technologies Used

*   **Language:** Kotlin
*   **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles (Repository pattern)
*   **UI:** Jetpack Compose for declarative UI
*   **Asynchronous Programming:** Kotlin Coroutines & Flow
*   **Dependency Injection:** Hilt
*   **Networking:** Retrofit & OkHttp
*   **Image Loading:** Coil
*   **Pagination:** Paging 3
*   **Local Storage:** Room Persistence Library
*   **Static Analysis:** Detekt & Ktlint

## Screenshot
<img src="https://github.com/user-attachments/assets/92956ddf-1c7d-485b-8786-d2dd29410002" alt="Current App Screenshot" width="400"/>


## Setup & Build

To build and run this project:
1.  **Clone the repository:**
2.  API Key:•This project requires an API key from The Movie Database [The Movie Database (TMDB)](https://www.themoviedb.org/documentation/api)..•Sign up for an account and obtain an API key.
* You'll need to add your API key to the project. Create a `local.properties` file in the root of the project (if it doesn't exist) and add the following line, replacing "YOUR_ACTUAL_API_KEY_HERE" with the key you obtained:

*   (Ensure `local.properties` is listed in your `.gitignore` file to prevent committing your key.)
3.  **Open in Android Studio:**
    *   Open Android Studio.
    *   Select "Open an Existing Project" and navigate to the cloned repository directory.
4.  **Sync Gradle:** Let Android Studio sync the project and download dependencies.
5.  **Run the app:** Select a target device/emulator and click the "Run" button.

## Next Steps / Future Plans

Here are some of the proposed features and improvements planned for the future:

*   **Movie Details Screen:** Implement a screen to show more details for a selected movie (e.g., overview, release date, rating, cast, trailers).
*   **Search Functionality:** Allow users to search for movies by title.
*   **Offline Caching:** Improve offline capabilities by caching data using Room, allowing users to browse previously loaded movies even without an internet connection.
*   **UI/UX Enhancements:** Refine animations, transitions, and overall visual appeal.
*   **Testing:** Expand unit and UI test coverage.
*   **Error Handling:** More robust error handling and user feedback for network issues or API errors.
*   **Accessibility Improvements:** Ensure the app is fully accessible.
*   **Technical Improvements:** Use clean architecture and MAD priciples.

## Collaboroation
This project is a test project for me to practice my skills, improve and grow. Feel free to open Issues if you notice some points of improvement, I'm happy to learn!
