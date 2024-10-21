# ✨ WeSpot - 우리가 연결되는 공간 ✨

<p align="center"><img src="https://github.com/user-attachments/assets/4e890aef-4fca-4bf3-91e2-b30d26fcc873"></p>


<p align="middle">
  <a href='https://play.google.com/store/apps/details?id=com.bff.wespot.real'>
    <img width="200px;"; alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/>
  </a>
</p>


```
누군가 너를 좋아하고 있어!, 우리가 연결되는 공간 WeSpot

WeSpot은 투표와 쪽지의 두 가지 기능으로 구성된 10대의 소통 활성화를 위한 SNS 앱입니다.
```

</br></br>

## 🌟 Tech Stack 🌟

| Kotlin | Kotlin 2.0.0v, Coroutines, Flow |
|:---|:---|
| Architecture | MVI(Orbit), CleanArchitecture |
| Network | Ktor, Kotlin Serialization, Paging3 |
| Local | Protobuf DataStore, DataStorePreference |
| UI | Compose, ComposeDestination |
| Dependency Management | Gradle Convention Plugin(build-logic), VersionCatalogs |
| Dependency Injection | Dagger Hilt (KSP) |
| Lint | KtLint, DeteKt |
| Third Party | Material3, Lottie, Coil, Crashlytics, Analytics, CloudMessaging |
| Collaborate Tool | Github Action, Jira, Discord, Postman, Figma |

</br></br>

## Structure

### Clean Architecture Structure

<img src="https://github.com/user-attachments/assets/28c5fbd6-744c-4571-9597-700e7c147b1b" width="900" alter="image"/>

- Data Layer: Data Module에 해당되는 부분으로 데이터를 가져오고 저장 및 검색을 로직을 담당.
- Domain Layer: Domain Module에 해당되는 부분으로 앱의 비즈니스 로직을 담당.
- Presentation Layer: feature, common-ui에 해당되는 부분으로 MVVM 패턴을 활용해 UI 관련 로직을 담당합니다.

## ⭐ Dependency Graph ⭐

<p align="center"><img src="module_graph.svg"></p>


<img src="https://github.com/user-attachments/assets/1fa10ea8-b679-437d-a614-1d71937f3760" width="900" alter="image"/>

- App Module
    - Application
    - MainActivity
    - Navigation

- Data Module
    - Data-Local, Data-Remote로 나눠짐
    - 데이터를 검색, 저장, 가져오는 모듈
    - RepositoryImpl
    - Datasource, Database, RemoteConfig Datasource
    - DTO

- Domain Module
    - 순수 Java/Kotlin 모듈로 비즈니스 로직을 포함하고 있는 모듈
    - Repository Interface
    - Model

- Common-ui
    - feature모듈에서 중복으로 쓸 수 있는 UI 관련 로직이 있는 모듈
    - Reusable Component
    - Theme
 
- Common
    - Extension
    - Util

- analytics
    - Firebase Analytics

- Network
    - Ktor Request

- feature
    - Screen
    - ViewModel,
    - State

</br></br>


## 💫 Contributors 💫
<table>
  <tr>
    <td align="center"><a href="http://github.com/jeongjaino"><img src="https://avatars.githubusercontent.com/u/77484719?v=4" width="200px;" alt=""/><br /><sub><b>JinHo Jeong</sub></a><br /><a href="https://github.com/YAPP-Github/24th-App-Team-1-Android/commits/main?author=jeongjaino" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/flash159483"><img src="https://avatars.githubusercontent.com/u/123813671?v=4" width="200px;" alt=""/><br /><sub><b>Jung Seungwon</b></sub></a><br /><a href="https://github.com/YAPP-Github/24th-App-Team-1-Android/commits/main?author=flash159483" title="Code">💻</a></td>
    <td align="center"><img src="https://github.com/YAPP-Github/24th-App-Team-1-Android/assets/77484719/885117e7-5809-4630-9f37-ede855cdc8c8" width="200px;" alt=""/><br /><sub><b>BFF BOT 1</b></sub><br /><sub><b>Made By JUCIY</b></b></sub></td>
    <td align="center"><img src="https://github.com/YAPP-Github/24th-App-Team-1-Android/assets/77484719/611fc015-8f91-4645-90c9-2ae9e86d9e7b" width="200px;" alt=""/><br /><sub><b>BFF BOT 2</b></sub><br /><sub><b>Made By JUCIY</b></b></sub></td>
  </tr>
  <tr>
    <td align="center"><code>🍻제이콥🍻</td>
    <td align="center"><code>🍩브라우니🍩</td>
    <td align="center"><code>🥕짜치는 당근🥕</td>
    <td align="center"><code>🥕당근워크👟</td>
  </tr>
</table>
