package spiral.bit.dev.dailymood.ui.common.resolvers

//I decided to write this resolver because mlKit library gives me a float smile probability,
// and i want to use float exact values for future mp chart graphics, so, i wrote this resolver

class FaceMoodResolver {

    fun resolveEmotionType(moodValue: Float): Float {
        return moodValue * 2 - 1
    }
}