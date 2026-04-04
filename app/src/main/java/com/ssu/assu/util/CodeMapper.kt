package com.ssu.assu.util

fun String.toDepartmentName(): String {
    return when (this) {
        // IT대학
        "SOFTWARE" -> "소프트웨어학부"
        "GLOBAL_MEDIA" -> "글로벌미디어학부"
        "COMPUTER_SCIENCE" -> "컴퓨터학부"
        "ELECTRONIC_ENGINEERING" -> "전자정보공학부"
        "AI_CONVERGENCE" -> "AI융합학부"
        "DIGITAL_MEDIA" -> "디지털미디어학과"
        // AI대학
        "AI_SOFTWARE" -> "AI소프트웨어학부"
        "INFORMATION_SECURITY" -> "정보보호학과"
        // 자유전공학부
        "LIBERAL_STUDIES" -> "자유전공학부"
        else -> this
    }
}

fun String.toEnrollmentStatus(): String {
    return when (this) {
        "ENROLLED" -> "재학생"
        "LEAVE" -> "휴학생"
        "GRADUATED" -> "졸업생"
        else -> this
    }
}
