package com.bff.wespot.common.util

object AppVersionUtils {
    fun versionCompare(appVersion: String, compareVersion: String): VersionCompareResult {
        val compareVersionSplit = compareVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val appVersionSplit = appVersion.split(".").map { it.toIntOrNull() ?: 0 }

        val maxLength = maxOf(compareVersionSplit.size, appVersionSplit.size)

        // 패치 버전이 빠지는 경우를 대비하여, 빈 버전은 0으로 채운다.
        val extendedCompareVersion = compareVersionSplit + List(maxLength - compareVersionSplit.size) { 0 }
        val extendedAppVersion = appVersionSplit + List(maxLength - appVersionSplit.size) { 0 }

        for (i in 0 until maxLength) {
            when {
                extendedAppVersion[i] < extendedCompareVersion[i] -> {
                    return when (i) {
                        0 -> VersionCompareResult.MAJOR_VERSION_UPDATE
                        1 -> VersionCompareResult.MINOR_VERSION_UPDATE
                        2 -> VersionCompareResult.PATCH_VERSION_UPDATE
                        else -> VersionCompareResult.LATEST_VERSION
                    }
                }
                extendedAppVersion[i] > extendedCompareVersion[i] -> {
                    return VersionCompareResult.LATEST_VERSION
                }
            }
        }

        return VersionCompareResult.LATEST_VERSION
    }

    /**
     * @property [MAJOR_VERSION_UPDATE] 현재 버전이 최신 버전의 메이저 버전이 낮음.
     * @property [MINOR_VERSION_UPDATE] 현재 버전이 최신 버전의 마이너 버전이 낮음.
     * @property [PATCH_VERSION_UPDATE] 현재 버전이 최신 버전의 패치 버전이 낮음.
     * @property [LATEST_VERSION] 현재 버전이 최신 버전임.
     */
    enum class VersionCompareResult {
        MAJOR_VERSION_UPDATE,
        MINOR_VERSION_UPDATE,
        PATCH_VERSION_UPDATE,
        LATEST_VERSION,
    }
}
