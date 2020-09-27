package Entidades;

import java.io.IOException;
import java.util.Arrays;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

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
        ARPPacket tramaArp = new ARPPacket()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString()
            {
                return " [ Hardware type: " + hardtype + " | Protocol type: " + prototype + " ] "
                        + " [ Hardware address length: " + hlen + " | Protocol address length: " + plen
                        + " | Operation code: " + operation + " ] " + " [ Source hardware address: "
                        + Binario.arregloBytesAHexa(sender_hardaddr, ":") + " ] " + " [ Source protocol address: "
                        + Binario.arregloBytesADeci(sender_protoaddr, ".") + " ] " + " [ Target hardware address: "
                        + Binario.arregloBytesAHexa(target_hardaddr, ":") + " ] " + " [ Target protocol address: "
                        + Binario.arregloBytesADeci(target_protoaddr, ".") + " ] ";
            }
        };
        // Asignar valores a los datos del ARP
        tramaArp.hardtype = hardType;
        tramaArp.prototype = prototype;
        tramaArp.hlen = hardwareLen;
        tramaArp.plen = prototypeLen;
        tramaArp.operation = operation; // Esperar respuesta profesor TODO
        tramaArp.sender_hardaddr = macHostOrigen;
        tramaArp.sender_protoaddr = ipHostOrigen;
        tramaArp.target_hardaddr = macHostDestino;
        tramaArp.target_protoaddr = ipHostDestino;

        // Hacer link de la trama ARP a la trama Ethernet II
        tramaArp.datalink = tramaEthernetII;

        // Enviar el paquete ARP
        mensajero.sendPacket(tramaArp);

        // Hacer hasta que no queden mas paquetes o se encuentre el paquete adecuado
        while (true)
        {
            // Capturar los paquetes 1 a 1
            ARPPacket paquete = (ARPPacket) capturador.getPacket();

            // si se deja de recibir paquetes arrojar error
            if (paquete == null)
            {
                return null;
            }

            // si llega un paquete con el target_protoaddr (ip destino) igual a nuestro IPV4
            // retornar su MAC de respuesta
            if (Arrays.equals(paquete.target_protoaddr, ipHostOrigen) && Arrays.equals(paquete.sender_protoaddr, ipHostDestino))
            {
                return paquete.sender_hardaddr; // retornamos mac destino
            }
        }
    }
}
