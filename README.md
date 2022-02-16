# SearchBooks

SearchBooks is an app built with Jetpack Compose and architecture of MVVM + MVI.

This sample showcases:
- Books screen : the list of new books
- Search of books screen : the list of searched books
  - Support keyword for search
    - addition operation: android|java
    - subtraction operation : android-java
    - direct search : "android-java"
  - Support Infinite scroll 
    - when scroll meets the 5th item from the bottom, to load more items
- Support caching
  - Improved loading speed by caching (sqlite and memory)
    - the flow of to get list of new books
      - memory caching
      - database caching (sqlite-room)
      - remote data
    - the flow of to get details of a book
      - database caching (sqlite-room)
      - remote data
    - the search method has no cached

- Details of a book screen: the details of a selected book 
- Support android instrumented test

![sample_1](https://user-images.githubusercontent.com/12796737/154319317-1ce25dad-7b67-425e-b490-eb967f53266e.gif)
![sample_6](https://user-images.githubusercontent.com/12796737/154319393-5e7c75b5-0d47-47ce-afb6-1831d1e4b934.gif)
![sample_3](https://user-images.githubusercontent.com/12796737/154319375-ca95e7da-4ff6-44d6-9f0b-e808ba858105.gif)
![sample_7](https://user-images.githubusercontent.com/12796737/154319401-40b87f96-83d1-451b-9f89-14ab470b9e21.gif)
![sample_4](https://user-images.githubusercontent.com/12796737/154319343-a5d20601-4a49-477f-b91b-068551dea00a.gif)
![sample_5](https://user-images.githubusercontent.com/12796737/154319356-eeb33a2e-a57e-4b0a-b6ac-6bfe7d33e4e9.gif)

Full video : https://youtu.be/RB_gvO5JI5M

# todo
- [ ] jacoco coverage
- [ ] report badge
- [ ] CI with github actions
- [ ] CD with firebase

# Architecture
The project is layered with a View, Presentation, Model separation and presents a blend between MVVM and MVI.

Architecture layers:
* View 
* ViewModel 
* Model

![스크린샷 2022-02-16 오전 2 37 50](https://user-images.githubusercontent.com/12796737/154117778-154039e5-7125-4573-a03e-95498138e82a.png)

# Test
- android instrumented test
- android unit test

# API
- https://api.itbook.store/

# Dependencies

* UI
  * [Compose](https://developer.android.com/jetpack/compose) 
  * [Material design](https://material.io/design)
* Language
  * [Kotlin](https://kotlinlang.org/)
  * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) 
  * [Flow](https://developer.android.com/kotlin/flow)
* DI
  * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 
* Database
  * [Room](https://developer.android.com/topic/libraries/architecture/room)
* [Jetpack](https://developer.android.com/jetpack)
    * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) 
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* Network
  * [Retrofit](https://square.github.io/retrofit/)
  * [Coil](https://github.com/coil-kt/coil) 

# MAD Score

![madscore](https://user-images.githubusercontent.com/12796737/154118772-a39ea3af-9bfb-4c61-a636-21b34c0a14d2.png)
[details](https://madscorecard.withgoogle.com/scorecards/396195600/)
