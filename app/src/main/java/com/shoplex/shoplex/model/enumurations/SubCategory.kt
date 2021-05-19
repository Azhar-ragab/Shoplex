package com.shoplex.shoplex.model.enumurations

interface SubCategory{

}

enum class SubFashion: SubCategory{
    MEN,
    WOMEN,
    KIDS
}

enum class SubHealth: SubCategory{
    HAIRCARE,
    PERFUME,
    MAKE_UP
}

enum class SubPhone: SubCategory{
    PHONES,
    TABLETS,
    I_PAD
}

enum class SubElectronic: SubCategory{
    TVS,
    AUDIO,
    SMART_WATCHES,
    CAMERAS,
    OTHERS
}

enum class SubAccessors: SubCategory{
    JEWELLERY,
    WATCHES,
    BELTS,
    PHONE_CASES,
    CABLES,
    CHARGES,
    SELFIE_STICK
}

enum class SubBook: SubCategory{
    ART_AND_HUMANITIES,
    FICTION,
    ENTERTAINMENT,
    SCIENCE_AND_TECHNOLOGY,
    EDUCATION
}

