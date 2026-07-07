package com.example.viergewinnt.bluetooth

import kotlin.concurrent.thread
import kotlin.random.Random

class FakeBluetoothHandler : BluetoothService{

    private var connected = false
    private var pendingColumn: Int? = null
    private var onMoveReceived: ((Int) -> Unit)? = null
    private val fakeColumnHeights = IntArray(7) { 0 }

    override fun startConnection() {
        thread {
           // Thread.sleep(3000)
            connected = true
        }
    }

    override fun connect(deviceAddress: String): Boolean {
        thread {
           // Thread.sleep(3000)
            connected = true

           // Thread.sleep(2000)

            val fakeMove = createRandomValidMove()
            pendingColumn = fakeMove

            if (fakeMove != null) {
                onMoveReceived?.invoke(fakeMove)
            }
        }
        return true
    }

    override fun disconnect() {
        connected = false
        pendingColumn = null
        fakeColumnHeights.fill(0)
    }

    override fun sendInformation(column: Int) {
        if (!connected) {
            return
        }

        thread {
           // Thread.sleep(2000)

            val fakeMove = createRandomValidMove()
            pendingColumn = fakeMove

            if (fakeMove != null) {
                onMoveReceived?.invoke(fakeMove)
            }
        }
    }

    private fun createRandomValidMove(): Int? {
        val validColumns = fakeColumnHeights.indices.filter { index ->
            fakeColumnHeights[index] < 6
        }

        if (validColumns.isEmpty()) {
            return null
        }

        val selectedColumn = validColumns.random(Random.Default)
        fakeColumnHeights[selectedColumn]++
        return selectedColumn
    }

    override fun receiveInformation(): Int? {
        val column = pendingColumn

        pendingColumn = null

        return column
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun setOnMoveReceived(listener: (Int) -> Unit) {
        onMoveReceived = listener
    }

}
