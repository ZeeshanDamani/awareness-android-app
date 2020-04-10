package com.corona.awareness.model

import com.corona.awareness.network.model.Recommendation

object RecommendationMapper {
    fun map(recommendation: Recommendation): String {
        return when (recommendation) {
            Recommendation.RECOMMENDATION_SOCIAL_DISTANCING -> "Social distancing is recommended"
            Recommendation.RECOMMEND_DIAGNOSTIC_TEST -> "Self-quarantine is recommended"
            Recommendation.SELF_QUARANTINE -> "Diagnostic test is recommended"
        }
    }
}