package spiral.bit.dev.dailymood.ui.base.extensions

fun <T: Any, R: Any, S: Any> safeLet(p1: T?, p2: R?, block: (T, R) -> S?): S? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
