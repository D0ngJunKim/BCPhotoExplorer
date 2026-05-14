# BC Photo Explorer
https://github.com/D0ngJunKim/BCPhotoExplorer

## 기술 스택
- Kotlin
- Jetpack Compose
- Paging 3
- Hilt
- Retrofit, OkHttp
- Room
- Coil
- Kotlin Serialization
- KSP
- Gradle Convention Plugin

## 프로젝트 구조
```text
BCPhotoExplorer
├── ExplorerApp
│   └── 앱 진입점, Application, Activity
├── Feature
│   └── 화면, ViewModel, UseCase, Repository, 로컬 저장소
├── Environment
│   ├── DesignSystem
│   ├── Navigation
│   └── Network
└── BuildPlugins
    └── 공통 Gradle convention plugin
```

과제 제출 편의를 위해 Unsplash 요청에 필요한 키 주입 지점은 네트워크 인터셉터에 포함되어 있습니다.
실제 운영 앱이라면 `local.properties`, CI Secret, Remote Config 등으로 빌드/배포 환경에 맞게 분리하는 것이 적절하다고 판단했습니다.


## 프로젝트 차별점
필수 요구사항인 사진 목록, 상세, 좋아요/로컬 저장 기능은 기본 전제로 두고,
구현 과정에서 추가로 신경 쓴 설계와 완성도 포인트를 중심으로 정리했습니다.

### 1. 멀티 모듈 아키텍처 설계

과제 규모에서 과도하게 모듈을 나누기보다, 변경 이유와 재사용 가능성을 기준으로 경계를 나눴습니다.

- `ExplorerApp`: 앱 진입점, Activity, Application, 전역 Scaffold
- `Feature`: 화면, ViewModel, UseCase, Repository 구현
- `Environment:Network`: Retrofit, PagingSource, 네트워크 상태 감지, 공통 DataSource
- `Environment:Navigation`: 타입 기반 Navigation과 KSP Route Registry
- `Environment:DesignSystem`: 공통 Compose 컴포넌트와 디자인 토큰
- `BuildPlugins`: Android/Compose/Hilt 공통 Gradle Convention Plugin

### 2. KSP 기반 타입 안전 Navigation

Navigation route를 문자열로 직접 관리하지 않고,
`@Serializable` Route 클래스와 `@MainContainer`, `@OverlayContainer` 어노테이션으로 선언합니다. 
KSP가 Route Registry를 생성해 NavHost에 등록합니다.

- Route 등록 누락과 문자열 route 오타 가능성 감소
- Route 클래스가 `IRoute` 구현체인지 KSP 단계에서 검증
- start route 중복 선언 검증
- 직렬화 가능한 복합 타입을 navigation argument로 전달
- Main/Overlay NavHost를 분리해 화면 계층 확장 가능

### 3. 앱 사용성 및 이미지 로딩 체감 품질 개선

갤러리 스타일의 앱에서 이미지 로딩 경험은 매우 중요한 요소로 판단하여,
단순 로딩 인디케이터보다 사진이 곧 표시될 것이라는 시각적 맥락을 먼저 제공하는 방향으로 구성했습니다.

- BlurHash를 직접 디코딩해 이미지 로딩 전 Placeholder로 사용하고, 이미지 로딩 완료 시 Alpha Crossfade 적용
- 상세 이미지 클릭 시 전체 화면 뷰어로 진입하도록 처리
- 좋아요 상태 변경 시 로딩 인디케이터, 햅틱 피드백, 애니메이션 적용
- 네트워크 복구 시 실패 상태의 목록을 자동으로 재시도
- 모바일 2열, 태블릿 4열 처리로 멀티 디바이스 대응
- 원본 고해상도 이미지를 저장할 때 사용 가능한 메모리 예산 기준으로 샘플링하여 OOM 가능성 감소

### 4. Gradle Convention Plugin으로 빌드 설정 중복 제거

`BuildPlugins`에 공통 Android 설정, Compose 설정, Hilt 설정을 Convention Plugin으로 분리했습니다.

- 모든 모듈의 `compileSdk`, `minSdk`, Java/Kotlin 타겟을 한 곳에서 관리
- Application/Library/UI/Hilt 설정을 조합형 Plugin으로 재사용성이 높고 모듈 확장에 유리
- 모듈 추가 시 build.gradle.kts가 짧아지고 설정 누락 가능성 감소
- `settings.gradle.kts`에서 하위 모듈을 재귀적으로 include
