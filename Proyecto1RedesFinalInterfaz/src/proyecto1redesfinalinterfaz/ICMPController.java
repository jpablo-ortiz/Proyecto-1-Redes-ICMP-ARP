package proyecto1redesfinalinterfaz;

import Entidades.Binario;
import Entidades.ICMP;
import Entidades.TarjetaRed;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;
import org.icmp4j.IcmpPingResponse;

public class ICMPController implements Initializable
{

    private EthernetPacket tramaEthernetII;
    private TarjetaRed tarjetaRed;

    @FXML
    private TextField tIpOrigen;
    @FXML
    private TextField tIpDestino;
    @FXML
    private Button button;
    @FXML
    private TextField tTTL;
    @FXML
    private TextField tProtocolo;
    @FXML
    private TextField tVersion;
    @FXML
    private TextField tCheckSum;
    @FXML
    private TextField tTamMensaje;
    @FXML
    private TextField tTipoServicio;

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
        byte[] ipOrigen, ipDestino;
        int ttl, tamMensaje, protocolo, checksum, typeOfService, version;

        //System.out.println("Digite la dirección IP del Host Origen para la Trama IPV4 (FORMATO xx.xx.xx.xx): ");
        //entrada = new Scanner(System.in);
        scannerString = tIpOrigen.getText().split("\\.");
        ipOrigen = Binario.ipDeciStringAByte(scannerString);

        //System.out.println("Digite la dirección IP del Host Destino para la Trama IPV4 (FORMATO xx.xx.xx.xx): ");
        //entrada = new Scanner(System.in);
        scannerString = tIpDestino.getText().split("\\.");
        ipDestino = Binario.ipDeciStringAByte(scannerString);

        //System.out.println("Digite TTL para la Trama ICMP: ");
        //entrada = new Scanner(System.in);
        ttl = Integer.valueOf(tTTL.getText());

        //System.out.println("Digite el tamaño del mensaje ICMP: ");
        //entrada = new Scanner(System.in);
        tamMensaje = Integer.valueOf(tTamMensaje.getText());

        protocolo = Integer.valueOf(tProtocolo.getText()); // 1 -> ICMP echo Request 

        checksum = Integer.valueOf(tCheckSum.getText());
        typeOfService = Integer.valueOf(tTipoServicio.getText());
        version = Integer.valueOf(tVersion.getText());

        IcmpPingResponse icmpRespuesta = ICMP.icmpRequest(ipOrigen, ipDestino, ttl, tamMensaje, protocolo, checksum, typeOfService, version, tramaEthernetII);
        if (icmpRespuesta != null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("ICMP Echo reply recibido");
            alert.setContentText("Se recibio el echo reply del host con IP: " + icmpRespuesta.getHost() + "\nLa duración del echo request/replay fue de: " + icmpRespuesta.getDuration() + " ms");
            alert.showAndWait();
            salir(event);
            System.out.println("Se recibio el echo reply del host con IP: " + icmpRespuesta.getHost() + "\nLa duración del echo request/replay fue de: " + icmpRespuesta.getDuration() + " ms");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Mensaje");
            alert.setContentText(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ICMP echo Request");
            alert.showAndWait();
            System.out.println(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ICMP echo Request");
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

    void enviarDatosNecesarios(EthernetPacket tramaEthernetII, TarjetaRed tarjetaRed, byte[] macDestino, byte[] ipDestino)
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
        tIpDestino.setText(Binario.arregloBytesADeci(ipDestino, "."));
        tramaEthernetII.dst_mac = macDestino;
    }
    
    @FXML
    public void salir(ActionEvent event) throws Exception
    {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
