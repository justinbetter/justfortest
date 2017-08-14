package com.example.justfortest.statemachine

import android.bluetooth.BluetoothDevice
import android.os.Message
import com.tuyou.tsd.common.statemachine.IState
import com.tuyou.tsd.common.statemachine.State
import com.tuyou.tsd.common.statemachine.StateMachine

/**
 * Created by XMD on 2017/6/30.
 */
class BluetoothStateMachine : StateMachine {


    companion object {
        public val MESSAGE_TURNING_ON = 1
        public val MESSAGE_TURNED_ON = 2
        public val MESSAGE_TURNING_OFF = 3
        public val MESSAGE_TURNED_OFF = 4
        public val MESSAGE_CONNECTING_DEVICE = 5
        public val MESSAGE_CONNECTED_DEVICE = 6
        public val MESSAGE_DISCONNECTING_DEVICE = 7
        public val MESSAGE_DISCONNECTED_DEVICE = 8
        public val MESSAGE_READY = 9
        public val MESSAGE_DIALING = 10
        public val MESSAGE_INCALL = 11
        public val MESSAGE_INCOMMINGCALL = 12
        public val MESSAGE_TERMINATINGCALL = 13
        public val MESSAGE_TERMINATEDCALL = 14
        public val MESSAGE_CONNECTED_FAIL = 15
        public val MESSAGE_REMOTE_DISCONNECTED = 16


        private val MESSAGE_REQUEST = 100

        //----------------------------------------------------------------
        private val MESSAGE_OUTER_DEVICE_CONNECTED = 101
        private val MESSAGE_OUTER_DEVICE_DISCONNECTED = 102
    }

    private var aDefault = Default()
    private var turningOn = TurningOn()
    private var turnedOn = TurnedOn()
    private var turningOff = TurningOff()
    private var turnedOff = TurnedOff()
    private var connectingDevice = ConnectingDevice()
    private var connectedDevice = ConnectedDevice()
    private var disconnectingDevice = DisconnectingDevice()
    private var disconnectedDevice = DisconnectedDevice()
    private var ready = Ready()
    private var call = Call()
    private var dialing = Dialing()
    private var inCall = InCall()
    private var inComingCall = InComingCall()
    private var terminatingCall = TerminatingCall()
    private var terminatedCall = TerminatedCall()
    private var connectedFail = ConnectedFail()
    private var remoteDisconnected = RemoteDisconnected()
    private var tryAutoConnect = TryAutoConnect()

    private var stateListener: StateListener

    private var transitionMap = HashMap<Int, BaseState>()

    init {
        transitionMap.put(MESSAGE_TURNING_ON, turningOn)
        transitionMap.put(MESSAGE_TURNED_ON, turnedOn)
        transitionMap.put(MESSAGE_TURNING_OFF, turningOff)
        transitionMap.put(MESSAGE_TURNED_OFF, turnedOff)
        transitionMap.put(MESSAGE_CONNECTING_DEVICE, connectingDevice)
        transitionMap.put(MESSAGE_CONNECTED_DEVICE, connectedDevice)
        transitionMap.put(MESSAGE_DISCONNECTING_DEVICE, disconnectingDevice)
        transitionMap.put(MESSAGE_DISCONNECTED_DEVICE, disconnectedDevice)
        transitionMap.put(MESSAGE_READY, ready)
        transitionMap.put(MESSAGE_DIALING, dialing)
        transitionMap.put(MESSAGE_INCALL, inCall)
        transitionMap.put(MESSAGE_INCOMMINGCALL, inComingCall)
        transitionMap.put(MESSAGE_TERMINATINGCALL, terminatingCall)
        transitionMap.put(MESSAGE_TERMINATEDCALL, terminatedCall)
        transitionMap.put(MESSAGE_CONNECTED_FAIL, connectedFail)
        transitionMap.put(MESSAGE_REMOTE_DISCONNECTED, remoteDisconnected)
    }

    /**
     *                                          Default
     *            ________________________________|________________________________________________
     *           |                     |                                                          |
     *     TurningOn               TurnedOn                                                  TurnedOff
     *                    ____________|________________________________________________________________________________
     *                   |                   |               |                   |                     |              |
     *            ConnectingDevice    ConnectedDevice   DisconnectedDevice  RemoteDisconnected   ConnectedFail    TurningOff
     *                                  ______|___________                      |
     *                                 |                 |                      |
     *                              Ready      DisconnectingDevice       TryAutoConnect
     *                                |
     *                                |
     *                              Call
     *             __________________|_______________________________________
     *            |            |            |              |                |
     *          Dialing      InCall     IncomingCall  TerminatingCall   TerminatedCall
     *
     */
    constructor(stateListener: StateListener) : super("BluetoothStateMachine") {
        this.stateListener = stateListener
        addState(aDefault)
        addState(turningOn, aDefault)
        addState(turnedOn, aDefault)
        addState(connectingDevice, turnedOn)
        addState(connectedFail, turnedOn)
        addState(remoteDisconnected, turnedOn)
        addState(tryAutoConnect, remoteDisconnected)
        addState(connectedDevice, turnedOn)
        addState(ready, connectedDevice)
        addState(call, ready)
        addState(dialing, call)
        addState(inCall, call)
        addState(inComingCall, call)
        addState(terminatingCall, call)
        addState(terminatedCall, call)
        addState(disconnectingDevice, connectedDevice)
        addState(disconnectedDevice, turnedOn)
        addState(turningOff, turnedOn)
        addState(turnedOff, aDefault)
        setInitialState(aDefault)
    }


    interface StateListener {
        fun handleRequest(msg: Message?): Boolean
        fun notHandleRequest(msg: Message?)
        fun handleEnter(state: BaseState)
        fun handleExit(state: BaseState)
    }

    //--------------------------------------------------------------------------------------------------
    fun sendTurningOnMessage() = sendMessage(MESSAGE_TURNING_ON)

    fun sendTuredOnMessage() = sendMessage(MESSAGE_TURNED_ON)
    fun sendTurningOffMessage() = sendMessage(MESSAGE_TURNING_OFF)
    fun sendTuredOffMessage() = sendMessage(MESSAGE_TURNED_OFF)
    private fun sendDeviceConnectedMessage(device: BluetoothDevice, arg: Int = 0) = sendMessage(MESSAGE_CONNECTED_DEVICE, arg, 0, device)
    private fun sendDeviceDisconnectedMessage(device: BluetoothDevice) = sendMessage(MESSAGE_DISCONNECTED_DEVICE, device)
    fun sendDeviceConnectingMessage(device: BluetoothDevice) = sendMessage(MESSAGE_CONNECTING_DEVICE, device)
    fun sendDeviceDisconnectingMessage(device: BluetoothDevice) = sendMessage(MESSAGE_DISCONNECTING_DEVICE, device)
    fun sendReadyMessage() = sendMessage(MESSAGE_READY)
    fun sendDialingMessage(number: String) = sendMessage(MESSAGE_DIALING, number)
    fun sendInCallMessage() = sendMessage(MESSAGE_INCALL)
    fun sendInCommingCallMessage(number: String) = sendMessage(MESSAGE_INCOMMINGCALL, number)
    fun sendTerminatingCallMessage() = sendMessage(MESSAGE_TERMINATINGCALL)
    fun sendTerminatedCallMessage() = sendMessage(MESSAGE_TERMINATEDCALL)
    fun sendRemoteDisconnectedMessage(device: BluetoothDevice?) = sendMessage(MESSAGE_REMOTE_DISCONNECTED, device)
    fun sendConnectedFailMessage(device: BluetoothDevice) = sendMessage(MESSAGE_CONNECTED_FAIL, device)

    //--------------------------------------------------------------------------------------------------
    fun sendOuterDeviceConnectedMessage(device: BluetoothDevice, arg: Int = 0) = sendMessage(MESSAGE_OUTER_DEVICE_CONNECTED, arg, 0, device)

    fun sendOuterDeviceDisconnectedMessage(device: BluetoothDevice) = sendMessage(MESSAGE_OUTER_DEVICE_DISCONNECTED, device)
    //--------------------------------------------------------------------------------------------------
    private fun transitionTo(msg: Message?) {
        val state = transitionMap[msg!!.what]
        state?.message = msg
        transitionTo(state)
    }

    fun destroy() = quitNow()


    abstract inner class BaseState : State() {
        var message: Message? = null
        override fun enter() {
            super.enter()
            stateListener.handleEnter(this)
        }

        override fun exit() {
            super.exit()
            stateListener.handleExit(this)
        }
    }

    inner class Default : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_TURNING_ON, MESSAGE_TURNED_ON, MESSAGE_TURNED_OFF -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else                                                      -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }


    inner class TurningOn : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_TURNING_ON -> {
                    return IState.HANDLED
                }
                MESSAGE_TURNED_ON,
                MESSAGE_TURNED_OFF -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }

        }
    }

    inner class TurnedOn : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_TURNED_ON -> {
                    return IState.HANDLED
                }
                MESSAGE_TURNING_OFF,
                MESSAGE_CONNECTING_DEVICE,
                MESSAGE_CONNECTED_DEVICE,
                MESSAGE_REMOTE_DISCONNECTED,
                MESSAGE_DISCONNECTING_DEVICE,
                MESSAGE_CONNECTED_FAIL,
                MESSAGE_DISCONNECTED_DEVICE -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                MESSAGE_OUTER_DEVICE_CONNECTED -> {
                    sendDeviceConnectedMessage(msg.obj as BluetoothDevice, msg.arg1)
                    return IState.HANDLED
                }
                MESSAGE_OUTER_DEVICE_DISCONNECTED -> {
                    sendRemoteDisconnectedMessage(msg.obj as BluetoothDevice)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class TurningOff : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_TURNING_OFF,
                MESSAGE_CONNECTED_DEVICE,
                MESSAGE_DISCONNECTED_DEVICE,
                MESSAGE_OUTER_DEVICE_DISCONNECTED,
                MESSAGE_OUTER_DEVICE_CONNECTED -> {
                    return IState.HANDLED
                }
                MESSAGE_TURNED_OFF -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class TurnedOff : BaseState() {
        override fun processMessage(msg: Message?): Boolean {

            when (msg?.what) {
                MESSAGE_TURNED_OFF -> {
                    return IState.HANDLED
                }
                MESSAGE_TURNING_ON -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class ConnectingDevice : BaseState() {
        override fun processMessage(msg: Message?): Boolean {

            when (msg?.what) {
                MESSAGE_CONNECTING_DEVICE -> {
                    return IState.HANDLED
                }
                MESSAGE_OUTER_DEVICE_CONNECTED -> {
                    sendDeviceConnectedMessage(msg.obj as BluetoothDevice, msg.arg1)
                    return IState.HANDLED
                }
                MESSAGE_OUTER_DEVICE_DISCONNECTED -> {
                    sendConnectedFailMessage(msg.obj as BluetoothDevice)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class ConnectedDevice : BaseState() {
        override fun enter() {
            super.enter()
            transitionTo(ready)
        }

        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_CONNECTED_DEVICE                    -> {
                    return IState.HANDLED
                }
                MESSAGE_CONNECTING_DEVICE                   -> {
                    deferMessage(msg)
                    sendMessage(MESSAGE_DISCONNECTING_DEVICE)
                    return IState.HANDLED
                }
                MESSAGE_DISCONNECTING_DEVICE, MESSAGE_READY -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else                                        -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class ConnectedFail : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_CONNECTED_FAIL -> {
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class DisconnectingDevice : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_DISCONNECTING_DEVICE -> {
                    return IState.HANDLED
                }
                MESSAGE_CONNECTING_DEVICE -> {
                    deferMessage(msg)
                    return IState.HANDLED
                }
                MESSAGE_DISCONNECTED_DEVICE -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                MESSAGE_OUTER_DEVICE_DISCONNECTED -> {
                    sendDeviceDisconnectedMessage(msg.obj as BluetoothDevice)
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class DisconnectedDevice : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_DISCONNECTED_DEVICE -> {
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class RemoteDisconnected : BaseState() {
        override fun enter() {
            super.enter()
            transitionTo(tryAutoConnect)
        }

        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_REMOTE_DISCONNECTED -> {
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }

        }
    }

    inner class TryAutoConnect : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_OUTER_DEVICE_DISCONNECTED -> {
                    return IState.HANDLED
                }
                else -> {
                    stateListener.notHandleRequest(msg)
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class Ready : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_READY -> {
                    return IState.HANDLED
                }
                MESSAGE_DISCONNECTED_DEVICE -> {
                    transitionTo(msg)
                    return IState.HANDLED
                }
                MESSAGE_DIALING,
                MESSAGE_INCALL,
                MESSAGE_INCOMMINGCALL,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_TERMINATINGCALL -> {
                    deferMessage(msg)
                    transitionTo(call)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }

        }
    }

    inner class Call : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_DIALING,
                MESSAGE_INCALL,
                MESSAGE_INCOMMINGCALL,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_TERMINATINGCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }

        }
    }

    inner class Dialing : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_DIALING -> {
                    return IState.HANDLED
                }
                MESSAGE_INCALL,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_TERMINATINGCALL,
                MESSAGE_INCOMMINGCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class InCall : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_INCALL -> {
                    return IState.HANDLED
                }
                MESSAGE_DIALING,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_TERMINATINGCALL,
                MESSAGE_INCOMMINGCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }

        }
    }

    inner class InComingCall : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_INCOMMINGCALL -> {
                    return IState.HANDLED
                }
                MESSAGE_DIALING,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_TERMINATINGCALL,
                MESSAGE_INCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class TerminatingCall : BaseState() {
        override fun processMessage(msg: Message?): Boolean {
            when (msg?.what) {
                MESSAGE_TERMINATINGCALL -> {
                    return IState.HANDLED
                }
                MESSAGE_DIALING,
                MESSAGE_TERMINATEDCALL,
                MESSAGE_INCOMMINGCALL,
                MESSAGE_INCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }
        }
    }

    inner class TerminatedCall : BaseState() {
        override fun processMessage(msg: Message?): Boolean {

            when (msg?.what) {
                MESSAGE_TERMINATEDCALL -> {
                    transitionTo(ready)
                    return IState.HANDLED
                }
                MESSAGE_DIALING,
                MESSAGE_TERMINATINGCALL,
                MESSAGE_INCOMMINGCALL,
                MESSAGE_INCALL -> {
                    deferMessage(msg)
                    transitionTo(msg)
                    return IState.HANDLED
                }
                else -> {
                    return super.processMessage(msg)
                }
            }

        }
    }
}