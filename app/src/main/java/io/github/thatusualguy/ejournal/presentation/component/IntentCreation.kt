package io.github.thatusualguy.ejournal.presentation.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun phone(contactValue: String, ctx: Context) {
    val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactValue"))
    try {
        ctx.startActivity(i)
    } catch (s: SecurityException) {
        Toast.makeText(ctx, "Непредвиденная ошибка при звонке", Toast.LENGTH_LONG)
            .show()
    }
}

fun email(contactValue: String, ctx: Context) {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.fromParts("mailto", contactValue, null)
    )

    try {
        ctx.startActivity(Intent.createChooser(intent, null))
    } catch (s: SecurityException) {
        Toast.makeText(ctx, "Непредвиденная ошибка при звонке", Toast.LENGTH_LONG)
            .show()
    }
}

fun website(contactValue: String, ctx: Context) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(contactValue))
    ctx.startActivity(Intent.createChooser(i, null))
}