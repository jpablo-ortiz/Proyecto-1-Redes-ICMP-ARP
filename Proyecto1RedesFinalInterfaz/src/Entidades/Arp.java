package Entidades;

import java.io.IOException;
import java.util.Arrays;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class Arp
{

    public static byte[] arpRequest(byte[] macHostOrigen, byte[] ipHostOrigen, byte[] macHostDestino, byte[] ipHostDestino, short hardType, short prototype, short hardwareLen, short prototypeLen, short operation, EthernetPacket tramaEthernetII, NetworkInterface tarjetaRed) throws IOException
    {

        JpcapCaptor capturador = JpcapCaptor.openDevice(tarjetaRed, 5000, true, 5000); // (tarjeta red, bytes máx,
        // promiscuo activado,5 seg)
        // Se filtra los paquetes por protocolo ARP
        capturador.setFilter("arp", true);

        // Se obtiene la senderInstance (instancia de mensajero) del capturador
        JpcapSender mensajero = capturador.getJpcapSenderInstance();

        // Crear el paquete ARP
        PaqueteARP p = new PaqueteARP(macHostOrigen, ipHostOrigen, macHostDestino, ipHostDestino, hardType, prototype, hardwareLen, prototypeLen, operation, tramaEthernetII);

        // Enviar el paquete ARP
        mensajero.sendPacket(p.getP());

        long inicio = System.currentTimeMillis();
        // Hacer hasta que no queden mas paquetes o se encuentre el paquete adecuado
        while (true)
        {
            // Capturar los paquetes 1 a 1
            PaqueteARP aux = new PaqueteARP((Packet) capturador.getPacket());

            long fin = System.currentTimeMillis();
            double segundosTranscurridos = (double) ((fin - inicio) / 1000);
            
            // si se deja de recibir paquetes arrojar error
            if (aux.getP() == null || segundosTranscurridos > 10)
            {
                return null;
            }

            // si llega un paquete con el target_protoaddr (ip destino) igual a nuestro IPV4
            if (Arrays.equals(aux.getTargetProtocolAddress(), ipHostOrigen) && Arrays.equals(aux.getSenderProtocolAddress(), ipHostDestino))
            {
                return aux.getSenderHardwareAddress(); // retornamos mac destino
            }
        }
    }
}
