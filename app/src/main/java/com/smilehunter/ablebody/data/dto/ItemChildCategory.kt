package com.smilehunter.ablebody.data.dto

enum class ItemChildCategory(
    val string: String,
    val parentCategory: ItemParentCategory,
    val gender: ItemGender
) {
    SHORT_SLEEVE("숏슬리브",
        ItemParentCategory.TOP,
        ItemGender.UNISEX
    ),
    LONG_SLEEVE("롱슬리브",
        ItemParentCategory.TOP,
        ItemGender.UNISEX
    ),
    SLEEVELESS("슬리브리스",
        ItemParentCategory.TOP,
        ItemGender.UNISEX
    ),
    SWEAT_HOODIE("스웻&후디",
        ItemParentCategory.TOP,
        ItemGender.UNISEX
    ),
    PADTOP("패드탑",
        ItemParentCategory.TOP,
        ItemGender.FEMALE
    ),
    SHORTS("쇼츠",
        ItemParentCategory.BOTTOM,
        ItemGender.UNISEX
    ),
    PANTS("팬츠",
        ItemParentCategory.BOTTOM,
        ItemGender.UNISEX
    ),
    LEGGINGS("레깅스",
        ItemParentCategory.BOTTOM,
        ItemGender.UNISEX
    ),
    ZIP_UP("집업",
        ItemParentCategory.OUTER,
        ItemGender.UNISEX
    ),
    WINDBREAK("바람막이",
        ItemParentCategory.OUTER,
        ItemGender.UNISEX
    ),
    CARDIGAN("가디건",
        ItemParentCategory.OUTER,
        ItemGender.FEMALE
    ),
    HEADWEAR("모자",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    ),
    GUARD("보호대",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    ),
    STRAP("스트랩",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    ),
    BELT("벨트",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    ),
    SHOES("신발",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    ),
    ETC("ETC",
        ItemParentCategory.ACC,
        ItemGender.UNISEX
    )
}
