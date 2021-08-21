package spiral.bit.dev.dailymood.helpers

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

fun Context.hasPermissions(permission: String) = ActivityCompat.checkSelfPermission(
    this, permission
) == PackageManager.PERMISSION_GRANTED

fun Context.showToast(msg: Int) =
    Toast.makeText(this, msg, Toast.LENGTH_LONG)