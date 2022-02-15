# SearchBooks

SearchBooks is an app built with Jetpack Compose and architecture of MVVM + MVI.

This sample showcases:
- Books Screen : the list of New Books
- Search of books Screen : the list of searched books
  - Support keyword for search
    - addition operation: android|java
    - subtraction operation : android-java
    - direct search : "android-java"
- Details of a book : the details of a selected book 

![1_2](https://user-images.githubusercontent.com/12796737/153768587-80aa421b-844c-492d-a1f4-1c484850f66f.gif)
![1_5](https://user-images.githubusercontent.com/12796737/153768595-dddb555d-bdc7-43e0-843b-6ff533e8062c.gif)
![1_4](https://user-images.githubusercontent.com/12796737/153768593-ab1e56d0-4df1-4227-83e8-46c26151794b.gif)
![1_3](https://user-images.githubusercontent.com/12796737/153768592-e701bc2f-f691-4596-a1cb-26fb9e4cc7e6.gif)



# Architecture
The project is layered with a View, Presentation, Model separation and presents a blend between MVVM and MVI.

Architecture layers:
* View 
* ViewModel 
* Model

![](./app-architecture.png)


# API
- https://api.itbook.store/

# Dependencies

* UI
    * [Compose](https://developer.android.com/jetpack/compose) declarative UI framework
    * [Material design](https://material.io/design)

* Tech/Tools
    * [Kotlin](https://kotlinlang.org/) 100% coverage
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://developer.android.com/kotlin/flow) for async operations
    * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection
    * [Jetpack](https://developer.android.com/jetpack)
        * [Compose](https://developer.android.com/jetpack/compose)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for navigation between composables
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that stores, exposes and manages UI state
        * [Room](https://developer.android.com/topic/libraries/architecture/room) for local database
    * [Retrofit](https://square.github.io/retrofit/) for networking
    * [Coil](https://github.com/coil-kt/coil) for image loading

# MAD Score

![](./madscore.png) 
[details](https://madscorecard.withgoogle.com/scorecards/396195600/)
