package org.jsantamariap.eh_ho.login

import android.os.Bundle
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
