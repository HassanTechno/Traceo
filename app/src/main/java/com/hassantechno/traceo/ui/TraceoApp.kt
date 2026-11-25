package com.hassantechno.traceo.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.hassantechno.traceo.data.Service
import com.hassantechno.traceo.data.allServices
import com.hassantechno.traceo.data.popularServices
import com.hassantechno.traceo.data.serviceById
import com.hassantechno.traceo.phone.normalizePhone
import com.hassantechno.traceo.ui.components.TraceoBackdrop
import com.hassantechno.traceo.ui.components.TraceoBottomBar
import com.hassantechno.traceo.ui.screens.AboutScreen
import com.hassantechno.traceo.ui.screens.HomeScreen
import com.hassantechno.traceo.ui.screens.ServiceDetailScreen
import com.hassantechno.traceo.ui.screens.ServicesScreen
import com.hassantechno.traceo.util.LaunchResult
import com.hassantechno.traceo.util.copyToClipboard
import com.hassantechno.traceo.util.isServiceInstalled
import com.hassantechno.traceo.util.launchService
import com.hassantechno.traceo.util.openStore
import com.hassantechno.traceo.util.openUrl
import com.hassantechno.traceo.util.readClipboardText
import com.hassantechno.traceo.util.shareText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TraceoApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var tab by rememberSaveable { mutableIntStateOf(0) }
    var phoneInput by rememberSaveable { mutableStateOf("") }
    var normalizedPhone by rememberSaveable { mutableStateOf<String?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedServiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var query by rememberSaveable { mutableStateOf("") }
    var filterIndex by rememberSaveable { mutableIntStateOf(0) }
    var installedIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    val activePhone = remember(normalizedPhone) { normalizedPhone?.let(::normalizePhone) }
    val selectedService = remember(selectedServiceId) { serviceById(selectedServiceId) }
    val versionName = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.1.0"
        } catch (_: Exception) {
            "1.1.0"
        }
    }

    LaunchedEffect(tab, selectedServiceId) {
        installedIds = withContext(Dispatchers.IO) {
            allServices
                .filter { isServiceInstalled(context, it) }
                .map { it.id }
                .toSet()
        }
    }

    val showMessage: (String) -> Unit = { message ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    fun search() {
        val result = normalizePhone(phoneInput)
        if (result == null) {
            error = "شماره معتبر نیست. نمونه: ۰۹۱۲۳۴۵۶۷۸۹ یا +989123456789"
            return
        }
        error = null
        normalizedPhone = result.e164
        query = ""
        tab = 1
    }

    fun openService(service: Service) {
        when (launchService(context, service, activePhone)) {
            LaunchResult.OPENED_APP -> Unit
            LaunchResult.OPENED_BROWSER -> showMessage("برنامه ${service.name} نصب نیست؛ نسخه وب باز شد.")
            LaunchResult.OPENED_STORE -> showMessage("برنامه ${service.name} نصب نیست؛ فروشگاه باز شد.")
            LaunchResult.FAILED -> showMessage("بازکردن ${service.name} ممکن نشد.")
        }
    }

    BackHandler(enabled = selectedService != null) {
        selectedServiceId = null
    }
    BackHandler(enabled = selectedService == null && tab != 0) {
        tab = 0
    }

    TraceoBackdrop {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (selectedService == null) {
                    TraceoBottomBar(selectedTab = tab, onSelect = { tab = it })
                }
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when {
                    selectedService != null -> {
                        val service = selectedService
                        ServiceDetailScreen(
                            service = service,
                            phone = activePhone,
                            installed = service.id in installedIds,
                            onBack = { selectedServiceId = null },
                            onLaunch = { openService(service) },
                            onInstall = {
                                if (!openStore(context, service.packageName)) {
                                    showMessage("فروشگاه در دسترس نیست.")
                                }
                            },
                            onCopyPhone = {
                                activePhone?.let { phone ->
                                    if (copyToClipboard(context, "شماره", phone.formatted)) {
                                        showMessage("شماره کپی شد.")
                                    } else {
                                        showMessage("کپی شماره ممکن نشد.")
                                    }
                                }
                            },
                            onSharePhone = {
                                activePhone?.let { phone ->
                                    shareText(context, phone.formatted, "اشتراک‌گذاری شماره")
                                }
                            },
                            onRequestPhone = {
                                selectedServiceId = null
                                tab = 0
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    tab == 1 -> ServicesScreen(
                        phone = activePhone,
                        services = allServices,
                        installedIds = installedIds,
                        query = query,
                        onQueryChange = { query = it },
                        filterIndex = filterIndex,
                        onFilterChange = { filterIndex = it },
                        onServiceClick = { selectedServiceId = it.id },
                        onEditPhone = { tab = 0 },
                        modifier = Modifier.fillMaxSize(),
                    )

                    tab == 2 -> AboutScreen(
                        versionName = versionName,
                        onOpenSource = {
                            if (!openUrl(context, "https://github.com/HassanTechno/Traceo")) {
                                showMessage("مرورگر در دسترس نیست.")
                            }
                        },
                        onShareApp = {
                            shareText(
                                context,
                                "Traceo — شروع گفتگو بدون ذخیره شماره\nhttps://github.com/HassanTechno/Traceo",
                                "اشتراک‌گذاری Traceo",
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                    )

                    else -> HomeScreen(
                        phoneInput = phoneInput,
                        activePhone = activePhone,
                        error = error,
                        popularServices = popularServices,
                        installedIds = installedIds,
                        onPhoneChange = {
                            phoneInput = it
                            error = null
                        },
                        onPaste = {
                            val clip = readClipboardText(context)
                            if (clip == null) {
                                showMessage("کلیپ‌بورد خالی است.")
                            } else {
                                phoneInput = clip
                                error = null
                            }
                        },
                        onSearch = ::search,
                        onServiceClick = { selectedServiceId = it.id },
                        onShowAllServices = { tab = 1 },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
