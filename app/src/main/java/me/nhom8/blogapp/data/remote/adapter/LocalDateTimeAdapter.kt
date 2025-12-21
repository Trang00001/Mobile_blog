package me.nhom8.blogapp.data.remote.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
// *** THAY ĐỔI TẠI ĐÂY ***
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME // <-- SỬ DỤNG FORMAT LOCAL

@RequiresApi(Build.VERSION_CODES.O)
// Đổi giá trị mặc định của formatter
class LocalDateTimeAdapter(private var formatter: DateTimeFormatter = ISO_LOCAL_DATE_TIME) {
    @FromJson
    fun fromJson(value: String): LocalDateTime? {
        // Vẫn còn rủi ro về độ chính xác (microseconds)
        try {
            return LocalDateTime.parse(value, formatter)
        } catch (e: Exception) {
            // Thêm logic xử lý lỗi (ví dụ: cắt bớt độ chính xác)
            // Nếu lỗi xảy ra do microsecond, ta thử parse lại mà không cần formatter:
            try {
                return LocalDateTime.parse(value) // Thử parse mặc định
            } catch (e2: Exception) {
                // Hoặc nếu backend trả về Z, thì dùng ISO_DATE_TIME (không có ZONED)
                return null // Hoặc ném lỗi, tùy vào yêu cầu của ứng dụng
            }
        }
    }

    @ToJson
    fun toJson(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }
}