package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.request.ChangePhoneNumberRequest
import com.smilehunter.ablebody.data.dto.request.EditProfile
import com.smilehunter.ablebody.network.utils.TestRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File

class UserApiUnitTest {
    private val networkService: NetworkService = TestRetrofit.getInstance()

    @Test
    fun getMyUserData() {
        val response = runBlocking { networkService.getMyUserData() }
        println(response)
    }

    @Test
    fun getUserData() {
        val response = runBlocking { networkService.getUserData(uid = "5920702") }
        println(response)
    }

    @Test
    fun editUserData() {
        val path = System.getProperty("user.dir")
        val imageFile = File("$path\\src\\test\\java\\com\\smilehunter\\ablebody\\resources\\orange.png")

        val profile = EditProfile(
            nickname = "able_android",
            name = "ablebody",
            height = 180,
            weight = 90,
            job = "애블바디 입니다",
            introduction = "애블바디 입니다",
            defaultProfileImage = 4
        )

        val response = runBlocking {
            networkService.editProfile(
                profile = profile,
                profileImage = imageFile
            )
        }

        println(response)
    }

    @Test
    fun getMyBoard() {
        val response = runBlocking { networkService.getMyBoard() }
        println(response)
    }

    @Test
    fun getUserAdConsent() {
        val response = runBlocking { networkService.getUserAdConsent() }
        println(response)
    }

    @Test
    fun acceptUserAdConsent() {
        val response = runBlocking { networkService.acceptUserAdConsent(true) }
        println(response)
    }

    @Test
    fun changePhoneNumber() {
        val requestBody = ChangePhoneNumberRequest(phoneConfirmId = 1, verifyingCode = "6843")
        val response = runBlocking { networkService.changePhoneNumber(requestBody) }
        println(response)
    }

    @Test
    fun resignUser() {
        val response = runBlocking { networkService.resignUser("테스트를 위해 탈퇴합니다") }
        println(response)
    }

    @Test
    fun suggestion() {
        val response = runBlocking { networkService.suggestion("애블바디가 성장하는 그날까지!") }
        println(response)
    }
}