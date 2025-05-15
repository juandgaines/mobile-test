package com.juandgaines.seedqrvalidator.scanner.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.juandgaines.seedqrvalidator.R

@Composable
fun PermissionRationaleDialogs(
    showCameraRationale: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {

    if(showCameraRationale){
        val title = stringResource(R.string.camera_permission_required)


        val message = stringResource(R.string.camera_rationale)


        PermissionRationaleDialog(
            title = title,
            message = message,
            confirmButtonText = stringResource(R.string.grant_permissions),
            dismissButtonText = stringResource(R.string.not_now),
            onDismiss = {
                onDismiss()
            },
            onAccept = {
                onAccept()
            }
        )
    }
}

@Composable
private fun PermissionRationaleDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onAccept) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(dismissButtonText)
            }
        }
    )
}
