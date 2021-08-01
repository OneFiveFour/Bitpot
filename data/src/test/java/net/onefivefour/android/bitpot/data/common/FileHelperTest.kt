package net.onefivefour.android.bitpot.data.common

import android.content.Context
import net.onefivefour.android.bitpot.data.di.dataModuleTesting
import net.onefivefour.android.bitpot.data.meta.dummies.DownloadDummies
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FileHelperTest : KoinTest {

    private val context: Context by inject()

    companion object {

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

    @Before
    fun setup() {
        FileHelper.setDownloadPath(context)
    }


    @Test
    fun getDownloadFile_returnsCorrectFile() {
        val download = DownloadDummies.getAppDownload()

        val downloadFile = FileHelper.getDownloadFile(download)
        val downloadDirectory = context.getExternalFilesDir(null)?.path

        assertEquals(true, downloadFile.path.endsWith("downloadfile.zip"))
        assertEquals(true, downloadFile.path.startsWith(downloadDirectory!!))
    }

    @Test
    fun getDownloadDestination_returnsCorrectFile() {
        val download = DownloadDummies.getAppDownload()

        val downloadFile = FileHelper.getDownloadDestination(download.downloadUrl)
        val downloadDirectory = context.getExternalFilesDir(null)?.path

        assertEquals(true, downloadFile.path.endsWith("downloadfile.zip"))
        assertEquals(true, downloadFile.path.startsWith(downloadDirectory!!))
    }

    @Test
    fun downloadExists_itExists_returnsTrue() {
        val download = DownloadDummies.getAppDownload()

        // create file
        val downloadFile = FileHelper.getDownloadDestination(download.downloadUrl)
        downloadFile.createNewFile()

        // call sut
        val itExists = FileHelper.downloadExists(download)

        assertEquals(true, itExists)

        // clean up!
        downloadFile.delete()
    }

    @Test
    fun downloadExists_itDoesNotExist_returnsFalse() {
        val download = DownloadDummies.getAppDownload()

        val itExists = FileHelper.downloadExists(download)

        assertEquals(false, itExists)
    }
}