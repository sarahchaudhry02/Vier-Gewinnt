package com.example.viergewinnt.bluetooth


import org.junit.Assert.assertTrue
import org.junit.Test
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.junit.Assert.assertEquals
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import org.junit.Assert.assertNull

class BluetoothHandlerTest {

    @Test
    fun connect_validDeviceAddress_establishesConnection() {
        val bluetoothAdapter = mock<BluetoothAdapter>()
        val bluetoothDevice = mock<BluetoothDevice>()
        val bluetoothSocket = mock<BluetoothSocket>()

        whenever(bluetoothAdapter.getRemoteDevice("00:11:22:33:44:55"))
            .thenReturn(bluetoothDevice)

        whenever(bluetoothDevice.createRfcommSocketToServiceRecord(any()))
            .thenReturn(bluetoothSocket)

        whenever(bluetoothSocket.isConnected)
            .thenReturn(true)

        val bluetoothHandler = BluetoothHandler(bluetoothAdapter)

        val result = bluetoothHandler.connect("00:11:22:33:44:55")

        assertTrue(result)
        verify(bluetoothSocket).connect()
    }

    @Test
    fun sendInformation_connectedSocket_sendsColumn() {
        val bluetoothAdapter = mock<BluetoothAdapter>()
        val bluetoothDevice = mock<BluetoothDevice>()
        val bluetoothSocket = mock<BluetoothSocket>()
        val outputStream = ByteArrayOutputStream()

        whenever(bluetoothAdapter.getRemoteDevice("00:11:22:33:44:55"))
            .thenReturn(bluetoothDevice)

        whenever(bluetoothDevice.createRfcommSocketToServiceRecord(any()))
            .thenReturn(bluetoothSocket)

        whenever(bluetoothSocket.isConnected)
            .thenReturn(true)

        whenever(bluetoothSocket.outputStream)
            .thenReturn(outputStream)

        val bluetoothHandler = BluetoothHandler(bluetoothAdapter)
        bluetoothHandler.connect("00:11:22:33:44:55")

        bluetoothHandler.sendInformation(3)

        assertEquals(3, outputStream.toByteArray().first().toInt())
    }


    @Test
    fun receiveInformation_incomingMove_returnsReceivedColumn() {
        val bluetoothAdapter = mock<BluetoothAdapter>()
        val bluetoothDevice = mock<BluetoothDevice>()
        val bluetoothSocket = mock<BluetoothSocket>()

        whenever(bluetoothAdapter.getRemoteDevice("00:11:22:33:44:55"))
            .thenReturn(bluetoothDevice)

        whenever(bluetoothDevice.createRfcommSocketToServiceRecord(any()))
            .thenReturn(bluetoothSocket)

        whenever(bluetoothSocket.isConnected)
            .thenReturn(true)

        whenever(bluetoothSocket.inputStream)
            .thenReturn(ByteArrayInputStream(byteArrayOf(4)))

        val bluetoothHandler = BluetoothHandler(bluetoothAdapter)
        bluetoothHandler.connect("00:11:22:33:44:55")

        Thread.sleep(100)

        assertEquals(4, bluetoothHandler.receiveInformation())
    }

    @Test
    fun receiveInformation_emptyMessage_returnsNull() {
        val bluetoothAdapter = mock<BluetoothAdapter>()
        val bluetoothDevice = mock<BluetoothDevice>()
        val bluetoothSocket = mock<BluetoothSocket>()

        whenever(bluetoothAdapter.getRemoteDevice("00:11:22:33:44:55"))
            .thenReturn(bluetoothDevice)

        whenever(bluetoothDevice.createRfcommSocketToServiceRecord(any()))
            .thenReturn(bluetoothSocket)

        whenever(bluetoothSocket.isConnected)
            .thenReturn(true)

        whenever(bluetoothSocket.inputStream)
            .thenReturn(ByteArrayInputStream(byteArrayOf()))

        val bluetoothHandler = BluetoothHandler(bluetoothAdapter)
        bluetoothHandler.connect("00:11:22:33:44:55")

        Thread.sleep(100)

        assertNull(bluetoothHandler.receiveInformation())
    }





}




