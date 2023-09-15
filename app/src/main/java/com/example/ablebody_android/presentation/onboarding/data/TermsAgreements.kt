package com.example.ablebody_android.presentation.onboarding.data

enum class TermsAgreements(val description: String, val isRequired: Boolean) {
    ServiceAgreement("서비스 이용약관 동의", true),
    PrivacyPolicy("개인정보 수집 및 이용 동의", true),
    ThirdPartySharingConsent("개인정보 제3자 제공 동의", true),
    MarketingInformationConsent("마케팅 정보 수신 동의(선택)", false)
}