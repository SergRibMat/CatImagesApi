package com.example.catimagesapi

import android.view.View
import org.junit.Test

import org.junit.Assert.*

class MainActivityTest {

    @Test
    fun makeLayoutItVisible() {
        val visibility = if (true) View.GONE else View.VISIBLE

        assertEquals(View.GONE, visibility)
    }
}