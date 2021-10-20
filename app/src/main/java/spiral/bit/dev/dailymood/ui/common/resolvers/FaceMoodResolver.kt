package spiral.bit.dev.dailymood.ui.common.resolvers

//I decided to write this resolver because mlKit library gives me a float smile probability,
// and i want to use float exact values for future mp chart graphics, so, i wrote this resolver

class FaceMoodResolver {

    private val notDefinedEmotionType = 0.1F

    fun resolveEmotionType(moodValue: Float): Float {
        return when (moodValue) {
            in 0.0F..0.2F -> -1.0F // ANGRY EMOTION TYPE
            in 0.3F..0.4F -> -0.5F // SAD EMOTION TYPE
            in 0.5F..0.7F -> 0F    // NEUTRAL EMOTION TYPE
            in 0.7F..0.8F -> 0.5F  // GOOD EMOTION TYPE
            in 0.8F..1.0F -> 1.0F  // GREAT EMOTION TYPE
            else -> notDefinedEmotionType
        }
    }
}