package com.example.projectgui;

import com.fazecast.jSerialComm.SerialPort;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ArduinoControlsTest {
    // tests that the port is actually open and that it is connected to the port with arduino
    // connect arduino with the bank code beforehand
    @BeforeEach
    public static void setUp() {
        // Most of the arduinoControl functions need a connection to the arduino so we make sure that a connection is established
        if (ArduinoControls.arduinoPort == null) {
            ArduinoControls.setupCommunication();
            return;
        }
        if (!ArduinoControls.arduinoPort.isOpen()) {
            ArduinoControls.setupCommunication();
        }
    }
    @AfterEach
    public static void cleanUp () {
        // to make sure none of the values of the previous tests get used in the next test we close the connection
        // every time the arduino makes a new connection it resets itself. (confirm this)
        if (ArduinoControls.arduinoPort.isOpen()) {
            ArduinoControls.arduinoPort.closePort();
        }

    }

    @Test
    void analyzeSetupSucces() throws Exception{
        Assertions.assertTrue(ArduinoControls.setupCommunication());
    }
    // tests that the port is not actually opened if the setup fails
    // disconnect arduino first
//    @Test
//    void analyzeSetupCommunicationFail() throws Exception{
//        Assertions.assertFalse(ArduinoControls.setupCommunication() &&
//                ArduinoControls.arduinoPort.isOpen());
//    }
    @Test
    void analyzeSetupPortStatus() throws Exception {
        Assertions.assertTrue(ArduinoControls.arduinoPort.isOpen());
    }
    @Test
    void analyzeSetupPortDescription() throws Exception {
        Assertions.assertTrue(ArduinoControls.arduinoPort.getPortDescription().contains("Arduino Mega 2560"));
    }
    // this test will test the data transfer between the pc and arduino
    // the arduino has been setup to respond to "ping" with "pong"
    @Test
    void analyzeSendData() throws Exception{
        ArduinoControls.sendData("ping\n");
        // to give the arduino a bit of time to respond we put the thread to sleep for a tiny bit
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {e.printStackTrace();}
        Assertions.assertEquals("pong", ArduinoControls.inputs.getRecData());
    }
    @Test
    void analyzeSendDataFailure() {
        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        ArduinoControls.sendData("ping\n");
        Assertions.assertEquals("no port to send data too", outputStreamCaptor.toString().trim());
        System.setOut(standardOut);
    }

    @Test
    void analyzeGetKeypadInputs() throws Exception {
        ArduinoControls.sendData("CgetKey\n");
        // you have to manually press the 1 button on the keypad
        Assertions.assertEquals('1', ArduinoControls.getKeypad());
    }
    @Test
    void getCardInfo() {
    }

    @Test
    void eatCard() {
    }

    @Test
    void getKeypad() {
    }

    @Test
    void reset() {
    }
}