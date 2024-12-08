package com.bff.wespot.common.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppVersionUtilsTest {
    @Test
    fun `메이저 버전 업데이트 테스트`() {
        val result = AppVersionUtils.versionCompare("1.1.13", "2.3.2")
        assertEquals(AppVersionUtils.VersionCompareResult.MAJOR_VERSION_UPDATE, result)
    }

    @Test
    fun `마이너 버전 업데이트 테스트`() {
        val result = AppVersionUtils.versionCompare("1.2.23", "1.3.0")
        assertEquals(AppVersionUtils.VersionCompareResult.MINOR_VERSION_UPDATE, result)
    }

    @Test
    fun `패치 버전 업데이트 테스트`() {
        val result = AppVersionUtils.versionCompare("1.2.3", "1.2.4")
        assertEquals(AppVersionUtils.VersionCompareResult.PATCH_VERSION_UPDATE, result)
    }

    @Test
    fun `최신 버전 업데이트 테스트`() {
        val result = AppVersionUtils.versionCompare("1.2.3", "1.2.3")
        assertEquals(AppVersionUtils.VersionCompareResult.LATEST_VERSION, result)
    }

    @Test
    fun `앱 버전이 더 높은 경우 테스트 `() {
        val result = AppVersionUtils.versionCompare("2.3.1", "1.2.3")
        assertEquals(AppVersionUtils.VersionCompareResult.LATEST_VERSION, result)
    }

    @Test
    fun `앱 버전 중 패치 버전이 생략된 경우 테스트`() {
        val result = AppVersionUtils.versionCompare("1.2", "1.2.1")
        assertEquals(AppVersionUtils.VersionCompareResult.PATCH_VERSION_UPDATE, result)
    }

    @Test
    fun `비교 버전 중 패치 버전이 생략된 경우 테스트`() {
        val result = AppVersionUtils.versionCompare("1.2.1", "1.2")
        assertEquals(AppVersionUtils.VersionCompareResult.LATEST_VERSION, result)
    }
}
