package com.example.viergewinnt.bluetooth

interface BluetoothService {


    fun startConnection()

    fun connect(deviceAddress: String): Boolean

    fun disconnect()

    fun sendInformation(column: Int)

    fun receiveInformation(): Int?

    fun isConnected(): Boolean

    fun setOnMoveReceived(listener: (Int) -> Unit)

}