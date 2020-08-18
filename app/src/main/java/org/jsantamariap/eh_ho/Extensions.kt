package org.jsantamariap.eh_ho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

// clasica
/*
fun AppCompatActivity.isFirsTimeCreated(savedInstanceState: Bundle?): Boolean {
    return savedInstanceState == null
}
*/
// funcion inline
fun AppCompatActivity.isFirsTimeCreated(savedInstanceState: Bundle?): Boolean =
    savedInstanceState == null

fun ViewGroup.inflate(idLayout: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(idLayout, this, attachToRoot)
