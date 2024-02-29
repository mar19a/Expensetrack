package com.example.expensetrack.ui.screens.shared

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.example.expensetrack.utils.getPath

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageSelect(
    type: String = "*",
    onImageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val path = context.getPath(uri) ?: return@rememberLauncherForActivityResult
                onImageSelected(path)
            }
        }
    )

    @Composable
    fun LaunchGallery() {
        SideEffect {
            launcher.launch("image/$type")
        }
    }

    @Composable
    fun RequestGallery() = Dialog(
        onDismissRequest = { /*TODO*/ },
        content = {
            Text("Gallery Permissions Required")
        }
    )

    val permissionStates = rememberMultiplePermissionsState(
        permissions = buildList {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                add(Manifest.permission.ACCESS_MEDIA_LOCATION)
        },
    )
    PermissionsRequired(
        multiplePermissionsState = permissionStates,
        permissionsNotGrantedContent = {
            LaunchedEffect(key1 = Unit) {
                permissionStates.launchMultiplePermissionRequest()
            }
            RequestGallery()
        },
        permissionsNotAvailableContent = {
            RequestGallery()
        },
        content = {
            LaunchGallery()
        }
    )
}