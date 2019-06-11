package com.mlsdev.mlsdevstore.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mlsdev.mlsdevstore.data.model.authentication.AppAccessToken
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesManagerTest {

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var editor: SharedPreferences.Editor

    lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var data: Any? = null
    private val emptyJsonObject = "{\"expiration_date\":0}"
    private val emptyJsonArray = "[{}]"
    private val keyObject = "key_object"
    private val keyArray = "key_array"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(sharedPreferences.getString(keyObject, null)).thenReturn(emptyJsonObject)
        `when`(sharedPreferences.getString(keyArray, null)).thenReturn(emptyJsonArray)
        `when`(editor.putString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(editor)
        `when`(editor.remove(ArgumentMatchers.anyString())).thenReturn(editor)
        sharedPreferencesManager = SharedPreferencesManager(Gson(), sharedPreferences)
        data = AppAccessToken()
    }

    @Test
    fun testSave() {
        sharedPreferencesManager.save(Key.APPLICATION_ACCESS_TOKEN, data!!)
        verify<SharedPreferences>(sharedPreferences, times(1)).edit()
        verify<SharedPreferences.Editor>(editor, atLeastOnce()).putString(Key.APPLICATION_ACCESS_TOKEN, emptyJsonObject)
        verify<SharedPreferences.Editor>(editor, atLeastOnce()).apply()
    }

    @Test
    fun testRemove() {
        sharedPreferencesManager.remove(Key.APPLICATION_ACCESS_TOKEN)
        verify(sharedPreferences, times(1)).edit()
        verify(editor, times(1)).remove(Key.APPLICATION_ACCESS_TOKEN)
        verify(editor, times(1)).apply()
    }

    @Test
    fun testGetByClass() {
        sharedPreferencesManager[keyObject, AppAccessToken::class.java]
        verify<SharedPreferences>(sharedPreferences, times(1)).getString(keyObject, null)
    }

    @Test
    fun testGetByType() {
        val list = object : TypeToken<List<Any>>() {

        }
        sharedPreferencesManager.get<Any>(keyArray, list.type)
        verify<SharedPreferences>(sharedPreferences, times(1)).getString(keyArray, null)
    }

}
