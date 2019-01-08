## Demo
![Demo](/documents/demo.png)

## Download an apk
[flickr.apk](https://github.com/kimtaesu/FlickrAndroid/raw/feature/readme/apk/app-release.apk)

## Languages, libraries and tools used

* [Kotlin](https://kotlinlang.org/)
* [Databinding](https://developer.android.com/topic/libraries/data-binding/?hl=ko)
* [Jetpack](https://developer.android.com/jetpack/)
    * LifeCycles
    * LiveData
    * Room 
    * ViewModel 
* [Dagger2](https://github.com/google/dagger)
* [Mockito](https://github.com/mockito/mockito)
* Robolectric
* Leakcanary
* Glide

## Architecture
![Demo](/documents/architecture2.png)

### UI
[FlickrSearchFragment](/app/src/main/java/com/hucet/flickr/view/search/FlickrSearchFragment.kt) `onViewCreated` 에서  [FlickrSearchViewModel](/app/src/main/java/com/hucet/flickr/view/search/FlickrSearchViewModel.kt) `results` observe 합니다. 

`Loading`, `Success`, `Error` 에 따라 UI 를 구현합니다.
```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.results.observe(this, Observer {
            Timber.i("status: [${it.status}]\ndata: [${it.data?.size}]\nerror: [${it.message}]")
            when {
                it.status == Status.LOADING -> {
                    ...
                }
                it.data != null -> {
                    ...
                }
                it.message != null -> {
                   ...
                }
            }
        })
    }
```
### Presentation
안드로이드 프레임워크에 깊게 의존하지 않으며 사용자 인터페이스가 어떻게 표현될지를 담당합니다.
현재 프로젝트에서는 `Loading`, `Succes`, `error` 의 데이터로 표현하고 있습니다.

[FlickrSearchViewModel](/app/src/main/java/com/hucet/flickr/view/search/FlickrSearchViewModel.kt) 의 `results` 는 Query로 조회되는 데이터가 변경되었을 경우 변경된 데이터를 전달합니다.

[FlickrSearchViewModel](/app/src/main/java/com/hucet/flickr/view/search/FlickrSearchViewModel.kt) 의 3가지 함수가 있습니다. 
* `fun search(keyword: String)` // Parameter keyword의 첫 페이지의 데이터를 가져옵니다.
* `fun refresh()` // 현재 Keyword의 첫 페이지 데이터를 Network 로 부터 가져옵니다. 
* `fun loadNextPage()` // 현재 Keyword의 다음 페이지 데이터를 Network로 부터 가져옵니다. 

Test Codes: 
* [NextPageHandlerTest](/app/src/test/java/com/hucet/flickr/view/search/NextPageHandlerTest.kt) // `refresh` 와 `loadNextPage` 검증
* [FlickrSearchViewModelTest](/app/src/test/java/com/hucet/flickr/view/search/FlickrSearchViewModelTest.kt)  // `search` 검증
* [FlickrSearchViewModelRefreshTest](/app/src/test/java/com/hucet/flickr/view/search/FlickrSearchViewModelRefreshTest.kt)   // `refresh` 검증

### Data 
DataSources(Network, Local DB)를 가지고 있으며 조건에 따라 DataSoruces 로 데이터를 가져오고 DB에 저장합니다.

[PhotoRepository](/app/src/main/java/com/hucet/flickr/repository/PhotoRepository.kt) 의 2가지 함수가 있습니다. 
* `fun searchPhotos(keyword: String): LiveData<Resource<List<Photo>>>` // Parameter keyword의 첫 페이지 데이터를 검색하고 
그 결과로 LiveData를 반환합니다.
* `fun searchNextPhotos(keyword: String): LiveData<Resource<Boolean>>` // Parameter keyword의 다음 페이지 데이터를 검색하고 Local DB에 저장합니다. 다음 페이지 존재 유무의 LiveData를 반환합니다. (Next page 가 존재한다면 true, 존재하지 않으면 false)

Test Codes: 
[PhotoRepositoryTest](/app/src/test/java/com/hucet/flickr/repository/PhotoRepositoryTest.kt) // `searchPhotos`, `searchNextPhotos` 검증 

#### Data layer의 데이터를 가져오는 조건
![Demo](/documents/data_fetch.png)

### DataSources

#### FlickrApi
[FlickrApi](/app/src/main/java/com/hucet/flickr/api/FlickrApi.kt) Network로 부터 데이터를 가져옵니다. 

#### FlickrDatabase

[FlickrDatabase](/app/src/main/java/com/hucet/flickr/db/FlickrDatabase.kt) Local Db 입니다. 

[FlickrDao](/app/src/main/java/com/hucet/flickr/db/dao/FlickrDao.kt)

##### Scheme
![](/documents/flickr.png)

##### Queries
```
@Query("SELECT * FROM photos WHERE photo_id in (:photoIds) ORDER BY dateTaken DESC")
abstract fun searchPhotosByIds(photoIds: List<Long>): LiveData<List<Photo>>

@Insert(onConflict = OnConflictStrategy.REPLACE)
abstract fun insertPhotos(items: List<Photo>): List<Long>

@Insert(onConflict = OnConflictStrategy.REPLACE)
abstract fun insertSearchResult(photoSearchResult: PhotoSearchResult)

@Query("SELECT * FROM photo_search_results WHERE keyword = :keyword LIMIT 1")
abstract fun searchResult(keyword: String): LiveData<PhotoSearchResult>

@Query("SELECT * FROM photo_search_results WHERE keyword = :keyword  LIMIT 1")
abstract fun unitSearchResult(keyword: String): PhotoSearchResult?
```


