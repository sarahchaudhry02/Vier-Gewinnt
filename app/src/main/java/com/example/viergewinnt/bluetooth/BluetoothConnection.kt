package com.example.viergewinnt.bluetooth


interface BluetoothConnection {

    fun startConnection()

    fun connect(deviceAddress: String): Boolean

    fun disconnect()

    fun sendInformation()

    fun receiveInformation()

    fun isConnected(): Boolean





}