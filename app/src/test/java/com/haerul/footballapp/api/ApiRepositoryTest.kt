package com.haerul.footballapp.api

import org.junit.Test
import org.mockito.Mockito

class ApiRepositoryTest {

    @Test
    fun testDoRequest() {
        val apiRepository = Mockito.mock(ApiRepository::class.java)
        val url = TheSportDBApi.getLastMatch("4328")
        apiRepository.doRequest(url)
        Mockito.verify(apiRepository).doRequest(url)
    }
}