package com.magewr.gitusersearch.viewmodel

import com.magewr.gitusersearch.DummyDataFactory
import com.magewr.gitusersearch.RxTest
import com.magewr.gitusersearch.commons.PER_PAGE
import com.magewr.gitusersearch.interactors.favoriteusers.FavoriteUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.LocalUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersInteractor
import com.magewr.gitusersearch.interactors.searchusers.SearchUsersParam
import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import com.magewr.gitusersearch.ui.main.model.Users
import com.magewr.gitusersearch.ui.main.viewmodel.MainViewModel
import com.magewr.gitusersearch.ui.main.viewmodel.UserListViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class UserListViewModelTest: RxTest() {
    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Mock lateinit var apiInteractor: SearchUsersInteractor
    @Mock lateinit var localInteractor: LocalUsersInteractor
    @Mock lateinit var favoriteInteractor: FavoriteUsersInteractor

    private lateinit var testViewModel: UserListViewModel
    private lateinit var usersResult: SearchUsersResultModel

    @Before
    fun setUp() {
        usersResult = DummyDataFactory.getResultModel()
    }

    /**
     * API인터렉터가 있는 뷰모델은 EditText에 글자를 입력하면 API요청하여 결과를 Output에 전달해야 한다.
     */
    @Test
    fun getUserListTest() {
        val param =
            SearchUsersParam(
                "magewr",
                null,
                null,
                1,
                PER_PAGE
            )
        Mockito.`when`(apiInteractor.getSearchUsers(param)).thenReturn(Single.just(usersResult))
        Mockito.`when`(favoriteInteractor.subject).thenReturn(PublishSubject.create())

        testViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    apiInteractor,
                    favoriteInteractor
                )
            )

        val testObserver = TestObserver<SearchUsersResultModel>()
        testViewModel.output.getUsersResult.subscribe(testObserver)
        testViewModel.input.queryChanged.onNext("magewr")

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result.items[0].login, `is`("magewr"))
    }

    /**
     * API 인터렉터가 있는 뷰모델은 EditText에 빈 값을 입력하면 실제 요청은 이루어지지 않아야 한다.
     */
    @Test
    fun getUserListEmptyTest() {
        val param =
            SearchUsersParam(
                "",
                null,
                null,
                1,
                PER_PAGE
            )
        Mockito.`when`(apiInteractor.getSearchUsers(param)).thenReturn(Single.never())
        Mockito.`when`(favoriteInteractor.subject).thenReturn(PublishSubject.create())

        testViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    apiInteractor,
                    favoriteInteractor
                )
            )

        val testObserver = TestObserver<SearchUsersResultModel>()
        testViewModel.output.getUsersResult.subscribe(testObserver)
        testViewModel.input.queryChanged.onNext("")

        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    /**
     * Local 인터렉터가 있는 뷰모델은 EditText에 글자를 입력하면 Local에서 결과를 Output에 전달해야 한다.
     */
    @Test
    fun getLocalUserListTest() {
        val param =
            SearchUsersParam(
                "magewr",
                null,
                null,
                1,
                PER_PAGE
            )
        Mockito.`when`(localInteractor.getSearchUsers(param)).thenReturn(Single.just(usersResult))
        Mockito.`when`(favoriteInteractor.subject).thenReturn(PublishSubject.create())

        testViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    localInteractor,
                    favoriteInteractor
                )
            )

        val testObserver = TestObserver<SearchUsersResultModel>()
        testViewModel.output.getUsersResult.subscribe(testObserver)
        testViewModel.input.queryChanged.onNext("magewr")

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result.items[0].login, `is`("magewr"))
    }

    /**
     * Local 인터렉터가 있는 뷰모델은 EditText에 빈 값을 입력해도 전체 결과값이 output에 전달되어야 한다.
     */
    @Test
    fun getLocalUserListEmptyTest() {
        val param =
            SearchUsersParam(
                "",
                null,
                null,
                1,
                PER_PAGE
            )
        Mockito.`when`(localInteractor.getSearchUsers(param)).thenReturn(Single.just(usersResult))
        Mockito.`when`(favoriteInteractor.subject).thenReturn(PublishSubject.create())

        testViewModel =
            UserListViewModel(
                UserListViewModel.Dependency(
                    localInteractor,
                    favoriteInteractor
                )
            )

        val testObserver = TestObserver<SearchUsersResultModel>()
        testViewModel.output.getUsersResult.subscribe(testObserver)
        testViewModel.input.queryChanged.onNext("")

        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        Assert.assertThat(result.items[0].login, `is`("magewr"))
    }

}