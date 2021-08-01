package net.onefivefour.android.bitpot.data.modelconverter

import net.onefivefour.android.bitpot.data.extensions.toMD5
import net.onefivefour.android.bitpot.data.meta.dummies.DownloadDummies
import net.onefivefour.android.bitpot.data.model.converter.DownloadConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DownloadConverterTest {

    private lateinit var sut: DownloadConverter

    private val downloadDummy = DownloadDummies.getNetworkDownload()

    @Before
    fun setup() {
        sut = DownloadConverter()
    }

    @Test
    fun convert_simpleDownload_returnsConvertedDownload() {
        val result = sut.toAppModel(downloadDummy)

        val id = downloadDummy.name.toMD5()


        // the converter does not set the repoUuid
        Assert.assertEquals(id, result.id)
        Assert.assertEquals("", result.repoUuid)
        Assert.assertEquals("Download.name", result.name)
        Assert.assertEquals(1572278556L, result.createdOn.epochSecond)
        Assert.assertEquals(123L, result.fileSize)
        Assert.assertEquals(downloadDummy.links.self.href, result.downloadUrl)
        Assert.assertEquals(0f, result.downloadProgress)
    }
}