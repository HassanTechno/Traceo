package com.hassantechno.traceo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hassantechno.traceo.data.Service
import com.hassantechno.traceo.data.ServiceCategory
import com.hassantechno.traceo.phone.PhoneNumber
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
fun ServiceDetailScreen(
    service: Service,
    phone: PhoneNumber?,
    installed: Boolean,
    onBack: () -> Unit,
    onLaunch: () -> Unit,
    onInstall: () -> Unit,
    onCopyPhone: () -> Unit,
    onSharePhone: () -> Unit,
    onRequestPhone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = Color(service.accent)
    val needsPhone = service.supportsPhoneLink && phone == null

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "بازگشت", tint = TraceoCyan)
            }
            Text("جزئیات سرویس", color = TraceoText, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            TraceoBadge(
                if (service.category == ServiceCategory.IRANIAN) "ایرانی" else "بین‌المللی",
                accent,
            )
        }
        Spacer(Modifier.height(18.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(accent.copy(alpha = 0.30f), TraceoSurface),
                        ),
                        RoundedCornerShape(34.dp),
                    )
                    .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(34.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(service.iconRes),
                    contentDescription = service.name,
                    modifier = Modifier.size(76.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(service.name, color = TraceoText, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                service.description,
                color = TraceoMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp,
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TraceoBadge(
                    if (installed) "نصب شده" else "نصب نیست",
                    if (installed) TraceoSuccess else TraceoWarning,
                    icon = if (installed) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                )
                TraceoBadge(
                    if (service.supportsPhoneLink) "لینک مستقیم شماره" else "بدون لینک مستقیم",
                    if (service.supportsPhoneLink) accent else TraceoMuted,
                )
            }
        }
        Spacer(Modifier.height(22.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TraceoSurface, RoundedCornerShape(20.dp))
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Phone, null, tint = TraceoCyan, modifier = Modifier.size(17.dp))
                Spacer(Modifier.width(7.dp))
                Text("شماره", color = TraceoMuted, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                if (phone != null) {
                    TextButton(onClick = onCopyPhone) {
                        Text("کپی", color = TraceoCyan, fontSize = 11.sp)
                    }
                    TextButton(onClick = onSharePhone) {
                        Text("اشتراک", color = TraceoCyan, fontSize = 11.sp)
                    }
                }
            }
            Text(
                phone?.formatted ?: "هنوز شماره‌ای وارد نشده",
                color = if (phone == null) TraceoMuted else TraceoText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            if (phone == null) {
                TextButton(
                    onClick = onRequestPhone,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                ) {
                    Text("وارد کردن شماره در صفحه اصلی", color = TraceoCyan, fontSize = 11.sp)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        InfoCard(
            icon = if (service.supportsPhoneLink) Icons.Filled.CheckCircle else Icons.Filled.Warning,
            title = if (service.supportsPhoneLink) "این سرویس لینک مستقیم دارد" else "بازکردن برنامه",
            body = service.note ?: if (service.supportsPhoneLink) {
                "با زدن دکمه، چت با این شماره در ${service.name} باز می‌شود."
            } else {
                "Traceo فقط ${service.name} را باز می‌کند؛ جست‌وجوی شماره را داخل خود برنامه انجام بده."
            },
            accent = if (service.supportsPhoneLink) TraceoSuccess else TraceoWarning,
            modifier = Modifier.padding(top = 2.dp),
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = if (needsPhone) onRequestPhone else onLaunch,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (needsPhone) TraceoWarning else accent,
                contentColor = if (needsPhone) Color(0xFF1A1300) else Color(service.accentContent),
            ),
        ) {
            Text(
                when {
                    needsPhone -> "اول شماره را وارد کن"
                    service.supportsPhoneLink -> "شروع گفتگو در ${service.name}"
                    else -> "باز کردن ${service.name}"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
        if (!installed) {
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onInstall,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text("نصب ${service.name}", color = TraceoCyan, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Lock, null, tint = TraceoMuted, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(5.dp))
            Text(
                "Traceo وجود حساب را تأیید نمی‌کند و شماره را ذخیره یا ارسال نمی‌کند.",
                color = TraceoMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
            )
        }
        Spacer(Modifier.height(28.dp))
    }
}
