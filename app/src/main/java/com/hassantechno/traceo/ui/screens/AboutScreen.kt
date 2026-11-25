package com.hassantechno.traceo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hassantechno.traceo.ui.components.BrandMark
import com.hassantechno.traceo.ui.components.InfoCard
import com.hassantechno.traceo.ui.components.TraceoBadge
import com.hassantechno.traceo.ui.theme.TraceoAccent
import com.hassantechno.traceo.ui.theme.TraceoCyan
import com.hassantechno.traceo.ui.theme.TraceoMuted
import com.hassantechno.traceo.ui.theme.TraceoSuccess
import com.hassantechno.traceo.ui.theme.TraceoSurface
import com.hassantechno.traceo.ui.theme.TraceoText
import com.hassantechno.traceo.ui.theme.TraceoWarning

@Composable
fun AboutScreen(
    versionName: String,
    onOpenSource: () -> Unit,
    onShareApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(22.dp))
        BrandMark(78.dp)
        Spacer(Modifier.height(14.dp))
        Text("Traceo", color = TraceoText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text("یک شماره، یک انتخاب، شروع گفتگو", color = TraceoMuted, fontSize = 12.sp)
        Spacer(Modifier.height(10.dp))
        TraceoBadge("نسخه $versionName", TraceoCyan)
        Spacer(Modifier.height(22.dp))
        Text(
            "Traceo برای وقتی ساخته شده که شماره‌ای داری و می‌خواهی بدون ذخیره آن در مخاطبین، گفتگو را در پیام‌رسان دلخواهت شروع کنی.",
            color = TraceoMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )
        Spacer(Modifier.height(22.dp))
        InfoCard(
            icon = Icons.Filled.Lock,
            title = "حریم خصوصی",
            body = "شماره فقط روی دستگاه پردازش می‌شود. Traceo سرور ندارد، شماره را ذخیره نمی‌کند و آن را جایی ارسال نمی‌کند.",
            accent = TraceoSuccess,
        )
        Spacer(Modifier.height(10.dp))
        InfoCard(
            icon = Icons.Filled.Warning,
            title = "محدودیت‌ها",
            body = "Traceo وجود حساب را در پیام‌رسان‌ها بررسی نمی‌کند؛ چون بیشتر سرویس‌ها API عمومی قابل‌اتکا برای این کار ندارند. بازشدن لینک به معنی وجود حساب نیست.",
            accent = TraceoWarning,
        )
        Spacer(Modifier.height(10.dp))
        InfoCard(
            icon = Icons.Filled.CheckCircle,
            title = "لینک مستقیم",
            body = "واتساپ، تلگرام، سیگنال و وایبر امکان شروع چت با شماره را می‌دهند. بقیه سرویس‌ها فقط برنامه را باز می‌کنند.",
            accent = TraceoAccent,
        )
        Spacer(Modifier.height(10.dp))
        InfoCard(
            icon = Icons.Filled.Info,
            title = "ساخته‌شده با",
            body = "Kotlin، Jetpack Compose و Material 3. بدون کتابخانه تبلیغاتی یا ردیاب.",
            accent = TraceoCyan,
        )
        Spacer(Modifier.height(18.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TraceoSurface, RoundedCornerShape(22.dp))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(22.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("پروژه باز است", color = TraceoText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(
                "کد Traceo روی GitHub منتشر شده است. می‌توانی سورس را ببینی، ایده بدهی یا نسخه خودت را بسازی.",
                color = TraceoMuted,
                fontSize = 11.sp,
                lineHeight = 18.sp,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onOpenSource,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TraceoAccent),
                ) {
                    Text("مشاهده سورس", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = onShareApp, shape = RoundedCornerShape(14.dp)) {
                    Text("اشتراک‌گذاری", color = TraceoCyan, fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Traceo شماره را به سرور نمی‌فرستد.",
            color = TraceoMuted,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
    }
}
