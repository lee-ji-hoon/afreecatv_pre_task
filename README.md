# 아프리카 2023년 신입/경력 공개채용 Android 앱 개발 사전 과제

![image](https://user-images.githubusercontent.com/53300830/211145215-063be3b4-25cd-497f-b8af-958543e6796f.png)

### 실행 방법
API KEY를 local.properties 에서 관리
- local.properties에 `client_id=af_mobilelab_dev_e0f147f6c034776add2142b425e81777` 추가 후 실행

### 구현 모습

|방송 목록|상세 화면|새로고침|
|---|---|---|
|![목록](https://user-images.githubusercontent.com/53300830/211177537-6c63bef0-2189-4795-9833-c7df7f11e882.gif)|![상세화면](https://user-images.githubusercontent.com/53300830/211176939-4edeed5b-df16-4d24-a8ec-164b2c9d434d.gif)|![새로고침](https://user-images.githubusercontent.com/53300830/211177530-5d36dd07-f3f8-45ca-970c-6879f193a703.gif)|

|Configuration Changed|NetWork Error|방송목록 존재하지 않을 때|
|---|---|---|
|![컨피규레이션체인지](https://user-images.githubusercontent.com/53300830/211177335-530e019e-26fc-4cc5-9421-947b88be1403.gif)|![네트워크끊겼을때](https://user-images.githubusercontent.com/53300830/211177299-00543d2a-6949-4fed-892c-325a15834235.gif)|<img src="https://user-images.githubusercontent.com/53300830/211177352-f9b940cd-9613-458c-93b4-5a63fa5cccb9.jpg" height="700">|

|Popup Menu|가로 모드|
|---|---|
|![popup메뉴](https://user-images.githubusercontent.com/53300830/211178035-1a8fd28e-fdd4-4f74-959a-8f376319fcd9.gif)|![ezgif com-gif-maker](https://user-images.githubusercontent.com/53300830/211178292-a11f6d57-9051-491f-b596-ea5a895f86cd.gif)|

## 📋 구조

### 개발 환경

- Language : Kotlin
- minSdk : 23
- targetSdk : 31

### 사용한 라이브러리

- ViewPager2 + Coil + Lottie
- JetPack Navigation
- Retrofit2 + OkHttp3 + Moshi
- Coroutine + Flow + TestCoroutine
- Mockk + Truth + turbine
- SwipeRefreshLayout

## ✋ 멀티 모듈 

### 클린 아키텍처 적용

<img src="https://user-images.githubusercontent.com/53300830/211145345-4a32b7e6-82d6-4c15-9fe2-e77f7293ed44.png" width="600" height="300">

## ✍️ UnitTest 작성

- mockk 객체와 TestDispacther를 활용
- Flow를 테스트하기 위해서 turebin 라이브러리 사용


### Test용 Dispatcher 및 Mockk 객체 생성

- Dispatcher를 Main으로 초기화
- 사용할 UseCase mockk 객체로 생성

```kotlin

@OptIn(ExperimentalCoroutinesApi::class)
class BroadViewModelTest {
    private val fetchBroadListUseCase: FetchBroadListUseCase = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

### 성공 테스트 

- coEvery를 활용해서 mockk 객체의 반환 값 지정
- 성공 할 때의 UiState Event와 PageNumber가 증가가 정상적으로 이뤄졌는지 확인
- 예상한 대로 모든 Event가 소비 되는지 확인하기 위해서 cancelAndConsumeRemainingEvents를 활용

```kotlin
@Test
@DisplayName("[성공] 카테고리를 받아서 방송 리스트를 갖고 오는데 성공하면 UiState는 Loading이였다가 Success되고 pageNumber는 증가한다.")
fun fetchBroadSuccess() = runTest {
    // given
    coEvery {
        fetchBroadListUseCase(TEST_CATEGORY, 1)
    } returns ResultWrapper.Success(testBroad)

    // when
    val prevPageNum = viewModel.pageNumber
    viewModel.fetchBroadList(TEST_CATEGORY)

    // then
    viewModel.uiState.test {
        assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
            Event.Item(UiState.Loading),
            Event.Item(UiState.Success(Unit))
        )
        assertThat(prevPageNum + 1).isEqualTo(viewModel.pageNumber)
    }
}
```

### 실패 테스트

- 실패한 경우 mockk 객체 반환 값 지정
- UiStae 상태가 Failed이고 PageNumber가 증가하지 않았는지 확인

```kotlin
@Test
@DisplayName("[실패] 방송 리스트를 갖고 오는데 네트워크 오류가 난다면 UiState는 Failed가 되고 pageNumber는 증가하지 않는다.")
fun fetchBroadFailedNetWorkError() = runTest {
    // given
    coEvery {
        fetchBroadListUseCase(TEST_CATEGORY, 1)
    } returns ResultWrapper.Failed(ErrorData.Network)

    // when
    val prevPageNum = viewModel.pageNumber
    viewModel.fetchBroadList(TEST_CATEGORY)

    // then
    viewModel.uiState.test {
        assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
            Event.Item(UiState.Loading),
            Event.Item(UiState.Failure(NETWORK_ERROR_STRING_RES)) // 네트워크 에러 StringRes 값
        )
        assertThat(prevPageNum).isEqualTo(viewModel.pageNumber)
    }
}
```

|HomeViewModelTest|BroadViewModelTest|
|---|---|
|<img width="315" alt="image" src="https://user-images.githubusercontent.com/53300830/211145761-ea614dbb-7d92-43fa-a2f2-ddecc48c6b2b.png">|<img width="313" alt="image" src="https://user-images.githubusercontent.com/53300830/211145754-f159ab78-78f9-4ce3-a70d-8caca6e0bf69.png">|


## 🤔 성능 관련해서 고민했던 내용

### RecylcerView 페이지 요청 시점

- 무조건 맨 아래에 도착했을 때 요청하는 것은 비효율적이라고 판단
- 10개를 예시로 들면 약 6개가 보였을 때 미리 요청을 하게 되면 유저는 로딩 되는 화면을 짧게 볼 수 있을 것이라고 판단
- `RecyclerView의 addOnScrollListener` 을 활용해서 구현

```kotlin
private fun initRecyclerViewScrollListener(
        recyclerView: RecyclerView,
        fetch: (() -> Unit),
        pagingFetchCount: Int = DEFAULT_PAGING_FETCH_COUNT
) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager =
                LinearLayoutManager::class.java.cast(recyclerView.layoutManager) ?: return
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

            if (endScrolled(lastVisible, totalItemCount)) {
                fetch.invoke()
            }
        }

        private fun endScrolled(lastVisible: Int, totalItemCount: Int) =
            lastVisible >= totalItemCount - pagingFetchCount
    })
}
```

- 내가 원하는 위치만큼 RecyclerView가 내려왔을 때 fetch 람다 실행 (ViewModel에게 새 데이터 요청)
- 유저의 로딩 시간 단축
- 중복 실행 방지를 위해서 실행 중인 Job이 있는지 확인하고 만약 없다면 요청 하는 방식으로 구현

```kotlin
private var job: Job? = null

fun fetchBroadList(categoryName: String) {
    if (job != null && job?.isActive == true) return
    job = viewModelScope.launch {
        // 요청 코드 생략    
    }
}
```

### SafeApi 모듈 추가

- API 요청간에 중복 코드를 제거를 하기 위해서 구현
- 모듈로 분리해서 추후에 Remote / Local 모듈까지 분리 됐을 때 관심사 분리 생각
- ErrorHandling의 역할까지 포함하기 위해서 Handling 클래스 생성

```kotlin
interface ErrorHandler {

    suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): ResultWrapper<ResultType>

    fun getError(throwable: Throwable): ErrorData
}
```

```kotlin
abstract class ErrorHandlerImpl : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorData {
        return when (throwable) {
            is UnknownHostException,
            is SocketException,
            is NoInternetException -> ErrorData.Network
            // 생략
            else -> ErrorData.Unknown(message = "${throwable.message}")
        }
    }
}
```

```kotlin
class SafeApi : ErrorHandlerImpl() {

    override suspend fun <ResultType, RequestType> getSafe(
        remoteFetch: suspend () -> Response<RequestType>,
        mapping: (RequestType) -> ResultType
    ): ResultWrapper<ResultType> = handleResponse({ remoteFetch() }, mapping)
    
    private suspend fun <RequestType, ResultType> handleResponse(
        call: suspend () -> Response<RequestType>,
        converter: (RequestType) -> ResultType
    ): ResultWrapper<ResultType> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    return ResultWrapper.Success(
                        data = converter(it),
                        code = response.code()
                    )
                }
            }
            return ResultWrapper.Failed(
                error = ErrorData.Api(response.errorBody()?.string())
            )
        } catch (t: Throwable) {
            ResultWrapper.Failed(getError(t))
        }
    }
}
```

- 에러가 발생한다면 catch에서 어떤 에러인지 값 받아와서 State 변경
- 성공이라면 ResultWrapper.Success<T> 상태로 반환

> API 요청은 아래와 같이 이뤄집니다.

```kotlin
override suspend fun fetchBrandCategoryList(): ResultWrapper<List<BroadCategory>> =
    safeApi.getSafe(
        remoteFetch = { apiService.fetchBroadCategoryList() },
        mapping = { response ->
            response.categoryList.map {
                ConvertMapper<BroadCategoryData, BroadCategory>()(
                    it
                )
            }
        }
    )
```

- 어떤 요청을 할지 remoteFetch에 넣어주기만 하면 내부에서 ResultWrapper의 형태로 값 반환
- API 요청 코드 중복 최소화

