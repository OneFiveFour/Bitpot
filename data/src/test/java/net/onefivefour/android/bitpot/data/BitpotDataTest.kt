package net.onefivefour.android.bitpot.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.onefivefour.android.bitpot.data.database.BitpotDatabase
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.CoroutineTestRule
import net.onefivefour.android.bitpot.data.meta.dummies.RepositoryDummies
import net.onefivefour.android.bitpot.data.meta.dummies.WorkspaceDummies
import net.onefivefour.android.bitpot.network.ISharedPrefsNetwork
import net.onefivefour.android.bitpot.network.retrofit.AuthApi
import net.openid.appauth.AuthState
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.io.File
import net.onefivefour.android.bitpot.data.model.ConnectionState as AppConnectionState
import net.onefivefour.android.bitpot.network.model.api.ConnectionState as NetworkConnectionState

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class BitpotDataTest : KoinTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Force tests to be executed synchronously

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    companion object {
        const val FAKE_ACCESS_TOKEN = "FAKE_ACCESS_TOKEN"
        const val FAKE_FILE_PATH = "FAKE_FILE_PATH"

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            startKoin { modules(dataModuleTesting) }
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            stopKoin()
        }
    }

    private lateinit var sut: BitpotData

    private val workspaceDummy = WorkspaceDummies.getAppWorkspace()
    private val sharedPrefsNetwork: ISharedPrefsNetwork by inject()
    private val sharedPrefsData: ISharedPrefsData by inject()
    private val database: BitpotDatabase by inject()

    @Before
    fun setup() {
        sut = BitpotData
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun hasAccessToken_withoutAccessToken_returnsFalse() {
        // remove possible previous auth states
        sut.setAuthState(AuthState())

        val hasAccessToken = sut.hasAccessToken()

        assertEquals(false, hasAccessToken)
    }

    @Test
    fun hasAccessToken_withAccessToken_returnsTrue() {
        val authStateMock = mockk<AuthState>()
        every { authStateMock.accessToken } returns FAKE_ACCESS_TOKEN

        sut.setAuthState(authStateMock)
        val hasAccessToken = sut.hasAccessToken()

        assertEquals(true, hasAccessToken)
    }

    @Test
    fun getClientId_returnsAuthApiClientId() {
        val clientId = sut.getClientId()

        assertEquals(AuthApi.CLIENT_ID, clientId)
    }

    @Test
    fun getClientSecret_returnsAuthApiSecret() {
        val clientSecret = sut.getClientSecret()

        assertEquals(AuthApi.CLIENT_SECRET, clientSecret)
    }

    @Test
    fun logout_withUsernameAndAccessToken_deletesUsernameAndAccessToken() {
        coroutineTestRule.testDispatcher.runBlockingTest {
            // setup shared prefs
            val authStateMock = mockk<AuthState>()
            every { authStateMock.accessToken } returns FAKE_ACCESS_TOKEN
            sut.setAuthState(authStateMock)
            sut.setSelectedWorkspaceUuid(workspaceDummy.uuid)

            // setup database
            database.repositories().insert(listOf(RepositoryDummies.getSimpleAppRepository()))

            sut.logout()

            assertEquals(null, sharedPrefsNetwork.getAuthState()!!.accessToken)
            verify { database.clearAllTables() }
        }
    }

    @Test
    fun getSelectedWorkspaceUuid_returnsWorkspaceUuidFromSharedPrefs() {
        sharedPrefsData.selectedWorkspaceUuid = workspaceDummy.uuid

        assertEquals(workspaceDummy.uuid, sut.getSelectedWorkspaceUuid())
    }

    @Test
    fun getSelectedWorkspaceUuidLiveData_returnsWorkspaceFromSharedPrefs() {
        sharedPrefsData.selectedWorkspaceUuid = workspaceDummy.uuid

        sut.getSelectedWorkspaceUuidLiveData().test().assertValue(workspaceDummy.uuid)
    }

    @Test
    fun setAuthState_storesAuthStateInSharedPrefs() {
        val authStateMock = mockk<AuthState>()
        sut.setAuthState(authStateMock)

        assertEquals(authStateMock, sharedPrefsNetwork.getAuthState())
    }

    @Test
    fun getClientId_returnsClientId() {
        assertEquals(AuthApi.CLIENT_ID, sut.getClientId())
    }

    @Test
    fun getClientSecret_returnsClientSecret() {
        assertEquals(AuthApi.CLIENT_SECRET, sut.getClientSecret())
    }

    @Test
    fun setCacheDir_storesCachePathInSharedPrefs() {
        val cacheMock = mockk<File>()
        every { cacheMock.path } returns FAKE_FILE_PATH

        sut.setCacheDir(cacheMock)

        assertEquals(FAKE_FILE_PATH, sharedPrefsNetwork.cacheDir)
    }

    @Test
    fun setConnectionState_neverCalled_unknownStateStored() {
        assertEquals(NetworkConnectionState.UNKNOWN, sharedPrefsNetwork.connectionState)
    }

    @Test
    fun setConnectionState_isConnected_storeStateInSharedPrefs() {
        val connectionState = AppConnectionState.IS_CONNECTED

        sut.setConnectionState(connectionState)

        assertEquals(NetworkConnectionState.IS_CONNECTED, sharedPrefsNetwork.connectionState)
    }

    @Test
    fun setConnectionState_isDisConnected_storeStateInSharedPrefs() {
        val connectionState = AppConnectionState.IS_DISCONNECTED

        sut.setConnectionState(connectionState)

        assertEquals(NetworkConnectionState.IS_DISCONNECTED, sharedPrefsNetwork.connectionState)
    }

    @Test
    fun setSelectedWorkspaceUuid_storesWorkspaceInSharedPrefs() {
        sut.setSelectedWorkspaceUuid(workspaceDummy.uuid)

        assertEquals(workspaceDummy.uuid, sharedPrefsData.selectedWorkspaceUuid)
    }
}