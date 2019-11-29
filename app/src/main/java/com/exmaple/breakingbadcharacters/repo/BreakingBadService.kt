package com.exmaple.breakingbadcharacters.repo

import com.exmaple.breakingbadcharacters.data.Character
import retrofit2.Call
import retrofit2.http.GET

interface BreakingBadService {
    @GET("characters")
    fun getCharacters(): Call<List<Character>>

}