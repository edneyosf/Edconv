package edneyosf.edconv.core.windows

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.PointerType
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.BOOL
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinNT.HRESULT
import com.sun.jna.win32.W32APIOptions

fun HWND.setWindowAttribute(attribute: DwmAttribute, value: Boolean) {
    val def = BOOL(value)
    val reference = WinDef.BOOLByReference(def)

    Dwm.DwmSetWindowAttribute(
        hwnd = this,
        attribute = attribute.value,
        value = reference,
        valueSize = BOOL.SIZE
    )
}

private object Dwm : DwmApi by Native.load("dwmapi", DwmApi::class.java, W32APIOptions.DEFAULT_OPTIONS)

private interface DwmApi : Library {
    fun DwmSetWindowAttribute(hwnd: HWND, attribute: Int, value: PointerType?, valueSize: Int): HRESULT
}