package com.example.viergewinnt.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedQueue

@SuppressLint("MissingPermission")
class BluetoothHandler(
    private val bluetoothAdapter: BluetoothAdapter?
) : BluetoothService {

    private val appUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null
    private val receivedColumns = ConcurrentLinkedQueue<Int>()
    private var onMoveReceived: ((Int) -> Unit)? = null

    @Volatile
    private var connected: Boolean = false

    override fun startConnection() {
        Thread {
            try {
                serverSocket = bluetoothAdapter
                    ?.listenUsingRfcommWithServiceRecord("VierGewinnt", appUuid)

                socket = serverSocket?.accept()
                connected = socket?.isConnected == true
                serverSocket?.close()

                listenForIncomingMoves()
            } catch (exception: IOException) {
                connected = false
                closeConnection()
            }
        }.start()
    }

    override fun connect(deviceAddress: String): Boolean {
        return try {
            val device: BluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress)
                ?: return false

            bluetoothAdapter.cancelDiscovery()

            socket = device.createRfcommSocketToServiceRecord(appUuid)
            socket?.connect()
            connected = socket?.isConnected == true

            listenForIncomingMoves()

            connected
        } catch (exception: IOException) {
            connected = false
            closeConnection()
            false
        } catch (exception: SecurityException) {
            connected = false
            closeConnection()
            false
        }
    }

    override fun disconnect() {
        connected = false
        receivedColumns.clear()
        closeConnection()
    }

    override fun sendInformation(column: Int) {
        if (!connected) return

        try {
            socket?.outputStream?.write(column)
            socket?.outputStream?.flush()
        } catch (exception: IOException) {
            connected = false
            closeConnection()
        }
    }

    override fun receiveInformation(): Int? {
        return receivedColumns.poll()
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun setOnMoveReceived(listener: (Int) -> Unit) {
        onMoveReceived = listener
    }

    private fun listenForIncomingMoves() {
        Thread {
            val inputStream = socket?.inputStream ?: return@Thread

            while (connected) {
                try {
                    val column = inputStream.read()

                    if (column == -1) {
                        connected = false
                        closeConnection()
                        break
                    }

                    receivedColumns.add(column)
                    onMoveReceived?.invoke(column)
                } catch (exception: IOException) {
                    connected = false
                    closeConnection()
                    break
                }
            }
        }.start()
    }

    private fun closeConnection() {
        try {
            serverSocket?.close()
        } catch (_: IOException) {
        }

        try {
            socket?.close()
        } catch (_: IOException) {
        }

        serverSocket = null
        socket = null
    }
}