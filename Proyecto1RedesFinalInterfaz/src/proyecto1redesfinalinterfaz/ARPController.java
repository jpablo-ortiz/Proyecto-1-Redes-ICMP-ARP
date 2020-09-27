package proyecto1redesfinalinterfaz;

import Entidades.Arp;
import Entidades.Binario;
import Entidades.TarjetaRed;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;

public class ARPController implements Initializable
{

    private EthernetPacket tramaEthernetII;
    private TarjetaRed tarjetaRed;

    @FXML
    private TextField tIpOrigen;
    @FXML
    private TextField tIpDestino;
    @FXML
    private TextField tHardType;
    @FXML
    private TextField tPrototype;
    @FXML
    private TextField tHardwareLen;
    @FXML
    private TextField tPrototypeLen;
    @FXML
    private TextField tOperation;
    @FXML
    private Button button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    @FXML
    public void pressButton(ActionEvent event) throws Exception
    {
        String[] scannerString;
        byte[] macOrigen, macDestino;
        byte[] ipOrigen, ipDestino;
        short hardType, prototype, hardwareLen, prototypeLen, operation;

        scannerString = tIpOrigen.getText().split("\\.");
        ipOrigen = Binario.ipDeciStringAByte(scannerString);

        scannerString = tIpDestino.getText().split("\\.");
        ipDestino = Binario.ipDeciStringAByte(scannerString);

        hardType = Short.valueOf(tHardType.getText());
        prototype = Short.valueOf(tHardType.getText());
        hardwareLen = Short.valueOf(tHardType.getText());
        prototypeLen = Short.valueOf(tHardType.getText());
        operation = Short.valueOf(tHardType.getText());

        macDestino = Binario.macVacia;

        try
        {
            byte[] macRespuesta = Arp.arpRequest(tramaEthernetII.src_mac, ipOrigen, macDestino, ipDestino, hardType, prototype, hardwareLen, prototypeLen, operation, tramaEthernetII, tarjetaRed.getNetworkInterface());
            if (macRespuesta != null)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaRespuestaARP.fxml"));
                fxmlLoader.load();
                VentanaRespuestaARPController ventanaController = fxmlLoader.getController();
                ventanaController.enviarDatosNecesarios(tramaEthernetII, tarjetaRed, macRespuesta, ipDestino);
                Parent p = fxmlLoader.getRoot();
                Stage s = new Stage();
                s.setScene(new Scene(p));
                s.show();
                salir(event);
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error Mensaje");
                alert.setContentText(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ARP Request");
                alert.showAndWait();
                System.out.println(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ARP Request");
            }
        }
        catch (IOException ex)
        {
            System.out.println(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ARP Request");
        }
    }

    void enviarDatosNecesarios(EthernetPacket tramaEthernetII, TarjetaRed tarjetaRed)
    {
        this.tramaEthernetII = tramaEthernetII;
        this.tarjetaRed = tarjetaRed;
        for (NetworkInterfaceAddress addresse : tarjetaRed.getNetworkInterface().addresses)
        {
            if (addresse.address instanceof Inet4Address)
            {
                InetAddress ipOrigen = addresse.address;
                tIpOrigen.setText(Binario.arregloBytesADeci(ipOrigen.getAddress(), "."));
                break;
            }
        }
    }
    
    @FXML
    public void salir(ActionEvent event) throws Exception
    {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
