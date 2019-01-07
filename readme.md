## Demo

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

## Release Build
`keystore.properties` root 위치에 파일 생성 후 아래 내용을 추가한다.
```html
storePassword=test123
keyPassword=test123
keyAlias=flcikr
storeFile=../flickr.keystore
``` 
