package com.smilehunter.ablebody.presentation.home

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.smilehunter.ablebody.presentation.home.my.ui.EditProfileRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.smilehunter.ablebody.presentation.my.coupon.ui.CouponRegisterRoute
import com.smilehunter.ablebody.presentation.my.coupon.ui.CouponRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.MyInfoEditScreenRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.MyInfoScreenRoute
import com.smilehunter.ablebody.presentation.my.myprofile.ui.MyProfileRoute
import com.smilehunter.ablebody.presentation.my.other.ui.OtherNormalUserRoute
import com.smilehunter.ablebody.presentation.my.report.ReportRoute
import com.smilehunter.ablebody.presentation.my.setting.ui.SettingScreen
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithdrawBeforeScreen
import com.smilehunter.ablebody.presentation.my.alarm.ui.AlarmRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.ChangePhoneNumberRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.InputCertificationNumberRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithDrawCompleteScreen
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithdrawScreenRoute
import com.smilehunter.ablebody.presentation.my.suggest.ui.SuggestRoute
import com.smilehunter.ablebody.presentation.onboarding.ui.IntroScreen

const val HomeRoute = "Home"

fun NavGraphBuilder.addHomeGraph(
    isBottomBarShow: (Boolean) -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit,
    settingOnClickRouteRequest: () -> Unit,
    onBackRequest: () -> Unit,
    suggestonClick: () -> Unit,
    myInfoOnClick: () -> Unit,
    alarmOnClick: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
    withDrawReasonOnClick: (String) -> Unit,
    coupononClick: () -> Unit,
    couponRegisterOnClick: () -> Unit,
    onReport: (String) -> Unit,
    withDrawButtonOnClick: () -> Unit,
    orderManagementOnClick: () -> Unit,
    onPositiveBtnClick: () -> Unit,
    certificationBtnOnClick: (String) -> Unit,
    onVerificationSuccess: () -> Unit,
    onProfileEditClick: () -> Unit
) {
    navigation(
        startDestination = NavigationItems.Brand.name,
        route = "Home",
    ) {
        nestedGraph()
        navigation(startDestination = "start", route = NavigationItems.My.name) {

            composable(route = "start") {
                MyProfileRoute(
                    settingOnClick = settingOnClickRouteRequest,
                    coupononClick = coupononClick,
                    orderManagementOnClick = orderManagementOnClick,
                    onProfileEditClick = onProfileEditClick
                )
                isBottomBarShow(true)
            }

            composable(route = "SettingScreen") {
                SettingScreen(
                    onBackRequest = onBackRequest,
                    suggestonClick = suggestonClick,
                    myInfoOnClick = myInfoOnClick,
                    alarmOnClick = alarmOnClick
                )
                isBottomBarShow(false)
            }

            composable(route = "SuggestScreen") {
                SuggestRoute(
                    onBackRequest = onBackRequest
                )
                isBottomBarShow(false)
            }


            composable(route = "AlarmScreen") {
                AlarmRoute(
                    onBackRequest = onBackRequest
                )
                isBottomBarShow(false)
            }

            composable(route = "WithdrawBeforeScreen") {
                WithdrawBeforeScreen(
                    onBackRequest = onBackRequest,
                    withDrawReasonOnClick = withDrawReasonOnClick
                )
                isBottomBarShow(false)
            }

            composable(route = "MyInfoScreenRoute") {
                MyInfoScreenRoute(
                    onBackRequest = onBackRequest,
                    withDrawOnClick = withDrawOnClick,
                    editButtonOnClick = editButtonOnClick
                )
                isBottomBarShow(false)
            }
            composable(route = "MyInfomationEditScreen") {
                MyInfoEditScreenRoute(
                    onBackRequest = onBackRequest,
                    onPositiveBtnClick = onPositiveBtnClick
                )
                isBottomBarShow(false)
            }

            composable(route = "WithDrawCompleteScreen") {
                Log.d("homeGraph", "신고버튼눌려짐")
                WithDrawCompleteScreen()
                isBottomBarShow(false)
            }

            //탈퇴 화면
            composable(route = "WithdrawScreenRoute/{draw_reason}",
                arguments = listOf(
                    navArgument("draw_reason") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val draw_reason = navBackStackEntry.arguments?.getString("draw_reason")

                WithdrawScreenRoute(
                    onBackRequest = onBackRequest,
                    drawReason = draw_reason!!,
                    withDrawButtonOnClick = withDrawButtonOnClick//{navController.navigate("WithDrawCompleteScreen")}
                )
                isBottomBarShow(false)
            }

            composable(route = "CouponRoute") {
                CouponRoute(
                    onBackRequest = onBackRequest,
                    couponRegisterOnClick = couponRegisterOnClick
                )
                isBottomBarShow(false)
            }

            composable(route = "CouponRegisterRoute") {
                CouponRegisterRoute(
                    onBackRequest = onBackRequest
                )
                isBottomBarShow(false)
            }

//            composable(route = "IntroScreen") {
//                IntroScreen(
//                )
//                isBottomBarShow(false)
//            }
//            composable(route = "OtherNormalUserRoute") {
//                OtherNormalUserRoute(
//                    onBackRequest = onBackRequest,
//                    onReport = onReport,
//                    id = "5920702"
//                )
//                isBottomBarShow(true)
//            }

            composable(route = "OtherNormalUserRoute/{uid}",
                arguments = listOf(
                    navArgument("uid") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val uid = navBackStackEntry.arguments?.getString("uid") ?: return@composable
                Log.d("보내는 다른 유저 프로필homegraph", uid)
                OtherNormalUserRoute(
                    onBackRequest = onBackRequest,
                    onReport = { onReport(uid) },
                    uid = uid
                )
                isBottomBarShow(true)
            }

        }

        composable(route = "ChangePhoneNumberScreen") {
            ChangePhoneNumberRoute(
                certificationBtnOnClick = certificationBtnOnClick
            )
            isBottomBarShow(false)
        }

        composable(route = "ReportRoute/{uid}",
            arguments = listOf(
                navArgument("uid") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val uid = navBackStackEntry.arguments?.getString("uid") ?: return@composable
            Log.d("신고 버튼 눌렀을 때 homegraph", uid)
            ReportRoute(
                onBackRequest = onBackRequest,
                uid = uid
            )
            isBottomBarShow(false)
        }

        composable(route = "InputCertificationNumberRoute/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            InputCertificationNumberRoute(
                onVerificationSuccess = onVerificationSuccess
            )
            isBottomBarShow(false)
        }
    }

}






