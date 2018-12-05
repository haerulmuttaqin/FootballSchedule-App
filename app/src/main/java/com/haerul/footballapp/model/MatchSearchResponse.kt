package com.haerul.footballapp.model

import com.google.gson.annotations.SerializedName

data class MatchSearchResponse(
        @field:SerializedName("event")
        val events: MutableList<Match>
)