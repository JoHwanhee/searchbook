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


![1_2](https://user-images.githubusercontent.com/12796737/153768587-80aa421b-844c-492d-a1f4-1c484850f66f.gif)
![1_5](https://user-images.githubusercontent.com/12796737/153768595-dddb555d-bdc7-43e0-843b-6ff533e8062c.gif)
![1_4](https://user-images.githubusercontent.com/12796737/153768593-ab1e56d0-4df1-4227-83e8-46c26151794b.gif)
![1_3](https://user-images.githubusercontent.com/12796737/153768592-e701bc2f-f691-4596-a1cb-26fb9e4cc7e6.gif)


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
