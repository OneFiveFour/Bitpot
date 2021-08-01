package net.onefivefour.android.bitpot.data.database.converter

import net.onefivefour.android.bitpot.data.model.RepositoryAvatar
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryAvatarConverterTest {

    private lateinit var sut: RepositoryAvatarConverter

    // fake repository avatar
    private val fakeLanguageRepositoryAvatar = RepositoryAvatar.Language(0, 0, 0, 0)
    private val fakeCustomRepositoryAvatar = RepositoryAvatar.Image("url")

    // fake serialized avatar
    private val fakeLanguageRepositoryAvatarString = fakeLanguageRepositoryAvatar.drawable.toString() +
            RepositoryAvatarConverter.CUSTOM_DELIMITER +
            fakeLanguageRepositoryAvatar.gradientFromColor.toString() +
            RepositoryAvatarConverter.CUSTOM_DELIMITER +
            fakeLanguageRepositoryAvatar.gradientToColor.toString() +
            RepositoryAvatarConverter.CUSTOM_DELIMITER +
            fakeLanguageRepositoryAvatar.textColor.toString()

    private val fakeCustomRepositoryAvatarString = fakeCustomRepositoryAvatar.url

    @Before
    fun setUp() {
        sut = RepositoryAvatarConverter()
    }

    @Test
    fun stringToRepositoryAvatar_languageAvatarString_returnsLanguageAvatar() {
        val result = sut.stringToRepositoryAvatar(fakeLanguageRepositoryAvatarString) as RepositoryAvatar.Language
        assertEquals(fakeLanguageRepositoryAvatar.drawable, result.drawable)
        assertEquals(fakeLanguageRepositoryAvatar.gradientFromColor, result.gradientFromColor)
        assertEquals(fakeLanguageRepositoryAvatar.gradientToColor, result.gradientToColor)
        assertEquals(fakeLanguageRepositoryAvatar.textColor, result.textColor)
    }

    @Test
    fun stringToRepositoryAvatar_customAvatarString_returnsCustomAvatar() {
        val result = sut.stringToRepositoryAvatar(fakeCustomRepositoryAvatarString) as RepositoryAvatar.Image
        assertEquals(fakeCustomRepositoryAvatar.url, result.url)
    }

    @Test
    fun repositoryAvatarToString_languageAvatar_returnsSerializedLanguageAvatar() {
        val result = sut.repositoryAvatarToString(fakeLanguageRepositoryAvatar)
        assertEquals(fakeLanguageRepositoryAvatarString, result)
    }

    @Test
    fun repositoryAvatarToString_customAvatar_returnsSerializedCustomAvatar() {
        val result = sut.repositoryAvatarToString(fakeCustomRepositoryAvatar)
        assertEquals(fakeCustomRepositoryAvatarString, result)
    }
}