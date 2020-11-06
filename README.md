# GitUserSearch-Android

#### [MVVMi](https://github.com/magewr/MVVMi-Android) 아키텍쳐를 이용한 간단한 앱

2개의 뷰모델에서 하나의 인터렉터를 사용함으로 이벤트 수신 핸들링을 별도의 추가 로직 없이 처리

인터렉터는 API, Local 용으로 나뉘어지며 인터페이스로 제공

뷰모델은 각각 자신이 필요한 기능의 API, Local 인터렉터를 사용, 인터페이스로 참조

뷰모델, 인터렉터 레이어 유닛테스트
