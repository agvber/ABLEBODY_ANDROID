package com.smilehunter.ablebody.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.smilehunter.ablebody.UserInfoPreferences
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.request.ChangePhoneNumberRequest
import com.smilehunter.ablebody.data.dto.request.EditProfile
import com.smilehunter.ablebody.data.dto.response.AddCouponResponse
import com.smilehunter.ablebody.data.dto.response.GetAddressResponse
import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.GetMyBoardResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.data.result.FileTooLargeException
import com.smilehunter.ablebody.datastore.DataStoreService
import com.smilehunter.ablebody.model.LocalUserInfoData
import com.smilehunter.ablebody.network.NetworkService
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkService: NetworkService,
    private val dataStoreService: DataStoreService
): UserRepository {

    override suspend fun getMyUserData(): UserDataResponse = networkService.getMyUserData()

    override suspend fun getUserData(uid: String): UserDataResponse = networkService.getUserData(uid)

    override val localUserInfoData: Flow<LocalUserInfoData>
        get() = dataStoreService.getUserInfoData()
            .onEach { userInfoDataStore ->
                if (userInfoDataStore.uid.isEmpty()) {
                    getMyUserData().data?.apply {
                        updateLocalUserInfo {
                            it.copy(
                                uid = uid,
                                name = name,
                                nickname = nickname,
                                gender = when (gender) {
                                    Gender.MALE -> LocalUserInfoData.Gender.MALE
                                    Gender.FEMALE -> LocalUserInfoData.Gender.FEMALE
                                },
                                profileImageUrl = profileUrl
                            )
                        }
                    }
                }
            }
            .map { userInfoDataStore ->
                userInfoDataStore.asExternalModel()
            }

    override suspend fun updateLocalUserInfo(localUserInfoData: (LocalUserInfoData) -> LocalUserInfoData) {
        dataStoreService.updateUserInfoData {
            val data = localUserInfoData(it.asExternalModel())
            it.toBuilder()
                .setUid(data.uid)
                .setName(data.name)
                .setNickname(data.nickname)
                .setGender(
                    when(data.gender) {
                        LocalUserInfoData.Gender.MALE -> com.smilehunter.ablebody.Gender.MALE
                        LocalUserInfoData.Gender.FEMALE -> com.smilehunter.ablebody.Gender.FEMALE
                        LocalUserInfoData.Gender.UNRECOGNIZED -> com.smilehunter.ablebody.Gender.UNRECOGNIZED
                    }
                )
                .setProfileImageUrl(data.profileImageUrl)
                .build()
        }
    }

    override suspend fun editProfile(
        editProfile: EditProfile,
        profileImageInputStream: InputStream?
    ): UserDataResponse = withContext(ioDispatcher) {
        if (profileImageInputStream == null) {
            return@withContext networkService.editProfile(profile = editProfile, profileImage = null)
        }
        val temp = File.createTempFile("image_compress_file", ".jpeg")

        FileOutputStream(temp).buffered().use {
            val fileSize = profileImageInputStream.available()
            val bitmap = BitmapFactory.decodeStream(profileImageInputStream)

            val quality = when  {
                fileSize < 1000000 -> 75
                fileSize < 2000000 -> 40
                fileSize < 4000000 -> 20
                else -> 1
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
            bitmap.recycle()
        }

        if (temp.length() > 1000000) {
            throw FileTooLargeException()
        }

        val response = networkService.editProfile(profile = editProfile, profileImage = temp)
        temp.delete()
        temp.deleteOnExit()
        return@withContext response
    }

    override suspend fun getCouponBags(): GetCouponBagsResponse = networkService.getCouponBags()
    override suspend fun addCouponByCouponCode(couponCode: String): AddCouponResponse =
        networkService.addCouponByCouponCode(couponCode)

    override suspend fun getMyAddress(): GetAddressResponse = networkService.getAddress()

    override suspend fun addMyAddress(
        name: String,
        phoneNumber: String,
        roadAddress: String,
        roadDetailAddress: String,
        zipCode: String,
        deliveryRequestMessage: String,
    ) {
        networkService.addAddress(
            receiverName = name,
            phoneNum = phoneNumber,
            addressInfo = roadAddress,
            detailAddress = roadDetailAddress,
            zipCode = zipCode,
            deliveryRequest = deliveryRequestMessage
        )
    }

    override suspend fun editMyAddress(
        name: String,
        phoneNumber: String,
        roadAddress: String,
        roadDetailAddress: String,
        zipCode: String,
        deliveryRequestMessage: String,
    ) {
        networkService.editAddress(
            receiverName = name,
            phoneNum = phoneNumber,
            addressInfo = roadAddress,
            detailAddress = roadDetailAddress,
            zipCode = zipCode,
            deliveryRequest = deliveryRequestMessage
        )
    }

    override suspend fun getMyBoard(uid: String?, page: Int, size: Int): GetMyBoardResponse {
        return networkService.getMyBoard(uid = uid, page = page, size = size)
    }

    override suspend fun getUserAdConsent(): Boolean {
        return networkService.getUserAdConsent().data!!
    }

    override suspend fun acceptUserAdConsent(accept: Boolean): String {
        return networkService.acceptUserAdConsent(accept).data!!
    }

    override suspend fun changePhoneNumber(changePhoneNumberRequest: ChangePhoneNumberRequest): UserDataResponse {
        return networkService.changePhoneNumber(changePhoneNumberRequest)
    }

    override suspend fun resignUser(reason: String): String {
        return networkService.resignUser(reason).data!!
    }


    override suspend fun suggestApp(text: String) {
        networkService.suggestion(content = text)
    }
}

private fun UserInfoPreferences.asExternalModel() =
    LocalUserInfoData(
        uid = uid,
        name = name,
        nickname = nickname,
        gender = when (gender) {
            com.smilehunter.ablebody.Gender.MALE -> LocalUserInfoData.Gender.MALE
            com.smilehunter.ablebody.Gender.FEMALE -> LocalUserInfoData.Gender.FEMALE
            else -> LocalUserInfoData.Gender.UNRECOGNIZED
        },
        profileImageUrl = profileImageUrl
    )