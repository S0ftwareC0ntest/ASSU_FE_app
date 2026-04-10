package com.ssu.assu.ui.auth

import android.util.Log
import com.ssu.assu.util.RetrofitResult
import org.json.JSONObject

object LoginErrorMessageMapper {
    
    /**
     * 로그인 관련 서버 에러를 사용자 친화적인 메시지로 변환
     * @param fail RetrofitResult.Fail 객체
     * @return 사용자에게 표시할 친화적인 메시지
     */
    fun getLoginErrorMessage(fail: RetrofitResult.Fail): String {
        val apiCodeField = fail.code
        val serverMessage = fail.message
        val resultMessage = fail.result ?: ""
        val httpStatusStr = fail.statusCode.toString()

        Log.d(
            "LoginErrorMessageMapper",
            "로그인 에러 변환 - apiCode='$apiCodeField', http=$httpStatusStr, message='$serverMessage', result='$resultMessage'"
        )

        val extractedData = extractErrorDataFromMessage(serverMessage)
        val extractedFromJson = extractedData.first
        val extractedResultMessage = extractedData.second
        val businessCode = resolveBusinessCode(apiCodeField, extractedFromJson)

        Log.d("LoginErrorMessageMapper", "businessCode='$businessCode'")

        val errorDetail = buildString {
            if (resultMessage.isNotBlank()) append(resultMessage)
            if (extractedResultMessage.isNotBlank()) {
                if (isNotEmpty()) append(' ')
                append(extractedResultMessage)
            }
        }

        return when {
            serverMessage.contains("네트워크") ||
                serverMessage.contains("network", ignoreCase = true) ||
                serverMessage.contains("offline", ignoreCase = true) ->
                "네트워크 연결을 확인해주세요."

            businessCode == "MEMBER_4001" ->
                "회원가입을 진행해주세요."

            businessCode == "MEMBER_4009" ->
                "이미 가입된 회원입니다."

            businessCode == "COMMON500" && (
                resultMessage.contains("Bad credentials", ignoreCase = true) ||
                    extractedResultMessage.contains("Bad credentials", ignoreCase = true)
                ) ->
                "비밀번호가 틀렸습니다."

            // 서버 DB 등에 구 전공/단과 enum 코드가 남아 있을 때 (예: Major.COM 제거 후)
            businessCode == "COMMON500" && isNoEnumConstantMajorOrDepartmentError(errorDetail) ->
                "저장된 학과 정보가 시스템과 맞지 않아 로그인할 수 없습니다. 관리자에게 문의해 주세요."

            businessCode == "COMMON500" ->
                "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."

            businessCode == "SSU4000" ->
                "숭실대학교 유세인트 SSO 로그인에 실패했습니다."

            businessCode == "SSU4001" ->
                "숭실대학교 유세인트 포털 접근에 실패했습니다."

            businessCode == "SSU4002" ->
                "숭실대학교 유세인트 포털 정보 확인에 실패했습니다."

            httpStatusStr == "400" ->
                "잘못된 요청입니다. 입력 정보를 확인해주세요."

            httpStatusStr == "401" ->
                "인증에 실패했습니다."

            httpStatusStr == "403" ->
                "접근 권한이 없습니다."

            httpStatusStr == "404" ->
                "요청한 정보를 찾을 수 없습니다."

            httpStatusStr == "409" ->
                "이미 가입된 회원입니다."

            httpStatusStr == "500" ->
                "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."

            serverMessage.isNotBlank() && !serverMessage.startsWith("{") ->
                serverMessage.trim()

            else -> {
                Log.w(
                    "LoginErrorMessageMapper",
                    "매핑되지 않은 로그인 에러: http=$httpStatusStr, business=$businessCode, message=$serverMessage"
                )
                "로그인에 실패했습니다. 잠시 후 다시 시도해주세요."
            }
        }
    }

    /** 이미 가입(중복 회원가입) 응답이면 회원가입 플로우를 종료하고 로그인 화면으로 보낸다. */
    fun isDuplicateMemberSignupFailure(fail: RetrofitResult.Fail): Boolean {
        if (fail.statusCode == 409) return true
        return businessCodeFor(fail) == "MEMBER_4009"
    }

    /**
     * [RetrofitResult.Error] 등으로 떨어졌을 때, 예외 메시지가 JSON 에러 본문이면 [RetrofitResult.Fail]로 복원한다.
     */
    fun syntheticFailFromThrowable(t: Throwable): RetrofitResult.Fail? {
        var current: Throwable? = t
        while (current != null) {
            failFromJsonMessage(current.message)?.let { return it }
            current = current.cause
        }
        return null
    }

    private fun businessCodeFor(fail: RetrofitResult.Fail): String {
        val extracted = extractErrorDataFromMessage(fail.message)
        return resolveBusinessCode(fail.code, extracted.first)
    }

    private fun failFromJsonMessage(raw: String?): RetrofitResult.Fail? {
        val trimmed = raw?.trim() ?: return null
        if (!trimmed.startsWith("{")) return null
        return try {
            val jo = JSONObject(trimmed)
            val bizCode = jo.optString("code", "")
            val httpFromBiz = bizCode.toIntOrNull()?.takeIf { it in 100..599 }
            val httpFromField = jo.optInt("status", -1).takeIf { it in 100..599 }
            val statusCode = httpFromBiz ?: httpFromField ?: -1
            RetrofitResult.Fail(
                statusCode = statusCode,
                code = bizCode.ifEmpty { "UNKNOWN" },
                message = trimmed,
                result = jo.optString("result", "").takeIf { it.isNotEmpty() }
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * BaseResponse 실패 시 [RetrofitResult.Fail.code]에 비즈니스 코드(MEMBER_4001 등)가 온다.
     * 본문이 JSON인 HttpException 등은 메시지에서 추출한 코드를 사용한다.
     */
    private fun isNoEnumConstantMajorOrDepartmentError(detail: String): Boolean {
        if (detail.isBlank()) return false
        val d = detail.lowercase()
        return d.contains("no enum constant") &&
            (d.contains("major.") || d.contains(".major") || d.contains("department.") || d.contains(".department"))
    }

    private fun resolveBusinessCode(apiCodeField: String, extractedFromMessage: String): String {
        val api = apiCodeField.trim()
        if (api.startsWith("MEMBER_") || api.startsWith("SSU") || api.startsWith("COMMON")) return api
        val fromJson = extractedFromMessage.trim()
        if (fromJson.startsWith("MEMBER_") || fromJson.startsWith("SSU") || fromJson.startsWith("COMMON")) {
            return fromJson
        }
        return fromJson.ifBlank { api }
    }
    
    /**
     * 서버 메시지에서 실제 에러 코드와 결과 메시지를 추출
     * @param serverMessage 서버에서 받은 메시지 (JSON 형태일 수 있음)
     * @return Pair<에러코드, 결과메시지>
     */
    private fun extractErrorDataFromMessage(serverMessage: String): Pair<String, String> {
        return try {
            // JSON 형태의 메시지인지 확인
            if (serverMessage.startsWith("{") && serverMessage.endsWith("}")) {
                val jsonObject = JSONObject(serverMessage)
                val errorCode = jsonObject.optString("code", "")
                val resultMessage = jsonObject.optString("result", "")
                Log.d("LoginErrorMessageMapper", "JSON에서 추출된 에러 코드: '$errorCode', 결과: '$resultMessage'")
                Pair(errorCode, resultMessage)
            } else {
                // JSON이 아닌 경우 원본 메시지 반환
                Log.d("LoginErrorMessageMapper", "JSON이 아닌 메시지: '$serverMessage'")
                Pair(serverMessage, "")
            }
        } catch (e: Exception) {
            Log.w("LoginErrorMessageMapper", "JSON 파싱 실패: ${e.message}")
            Pair(serverMessage, "")
        }
    }
}
