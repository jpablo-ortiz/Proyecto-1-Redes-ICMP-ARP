package proyecto1redesfinalinterfaz;

import Entidades.Binario;
import Entidades.TarjetaRed;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jpcap.packet.EthernetPacket;

public class VentanaRespuestaARPController implements Initializable
{

    private EthernetPacket tramaEthernetII;
    private TarjetaRed tarjetaRed;
    private byte[] macDestino, ipDestino;

    @FXML
    private Text tRespuesta;
    @FXML
    private Button bSalir;
    @FXML
    private Button bEnviarMensaje;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    @FXML
    public void salir(ActionEvent event) throws Exception
    {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void realizarICMP(ActionEvent event) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ICMP.fxml"));
        fxmlLoader.load();
        ICMPController icmpController = fxmlLoader.getController();
        icmpController.enviarDatosNecesarios(tramaEthernetII, tarjetaRed, macDestino, ipDestino);
        Parent p = fxmlLoader.getRoot();
        Stage s = new Stage();
        s.setScene(new Scene(p));
        s.show();
    }

    void enviarDatosNecesarios(EthernetPacket tramaEthernetII, TarjetaRed tarjetaRed, byte[] macRespuesta, byte[] ipDestino)
    {
        this.tramaEthernetII = tramaEthernetII;
        this.tarjetaRed = tarjetaRed;
        this.ipDestino = ipDestino;
        this.macDestino = macRespuesta;
        tRespuesta.setText("La respuesta recibida fue: " + Binario.arregloBytesAHexa(macRespuesta, ":"));
    }

}
