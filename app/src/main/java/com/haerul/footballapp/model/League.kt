package com.haerul.footballapp.model

data class League(val idLeague: String?, val strLeague: String?) {
    override fun toString(): String {
        return strLeague.toString()
    }
}