package spiral.bit.dev.dailymood.ui.common.resolvers

class SurveyResolver {

    fun resolveSurvey(scores: IntArray): Float {
        val scoresList = scores.toList()
        val depressionSectionItems = scoresList.subList(0, 7).sum()
        val neurosisSectionItems = scoresList.subList(7, 14).sum()
        val socialPhobiaSectionItems = scoresList.subList(14, 16).sum()
        val astheniaSectionItems = scoresList.subList(16, 18).sum()
        val insomniaSectionItems = scoresList.subList(18, 20).sum()

        val summaryScores =
            depressionSectionItems.plus(neurosisSectionItems).plus(socialPhobiaSectionItems)
                .plus(astheniaSectionItems).plus(insomniaSectionItems)

        return when {
            summaryScores > 36 -> -0.7F
            summaryScores > 30 -> -0.3F
            summaryScores > 25 -> 0.3F
            summaryScores > 20 -> 0.7F
            summaryScores < 15 -> 1.0F
            else -> 0F
        }
    }
}