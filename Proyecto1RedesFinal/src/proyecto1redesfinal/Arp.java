package proyecto1redesfinal;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class Arp
{
    static byte[] macFF = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

    static byte[] macVacia = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

    /**
     * 
     * @param mac        MAC en formato de byte[]
     * @param ip         Ip en formato de byte[]
     * @param tarjetaRed Interfaz de red de la tarjeta de red usada
     * @return Retorna la dirección MAC, en formato de byte[], recibida en el ARP
     *         Reply
     */
    public static byte[] arpRequest(byte[] ip, NetworkInterface tarjetaRed) throws IOException {
        JpcapCaptor capturador = JpcapCaptor.openDevice(tarjetaRed, 5000, true, 5000); // (tarjeta red, bytes máx, promiscuo activado,5 seg)
        // Se filtra los paquetes por protocolo ARP
        capturador.setFilter("arp", true);

        // Se obtiene la senderInstance (instancia de mensajero) del capturador
        JpcapSender mensajero = capturador.getJpcapSenderInstance();

        // Se busca la dirección IPV4 de la tarjeta gráfica para hacer el proceso
        InetAddress ipOrigen = null;
        for (int i = 0; i < tarjetaRed.addresses.length; i++) {
            if (tarjetaRed.addresses[i].address instanceof Inet4Address) {
                ipOrigen = tarjetaRed.addresses[i].address;
                break;
            }
        }

        // TODO
        // QUITAR
        System.out.println("Buenos dias estrellitas , la Tierra les dice hola !!!!!!!!!! :D : " + ipOrigen.getHostAddress());

        // Crear el paquete ARP
        ARPPacket tramaArp = new ARPPacket() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
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
        tramaArp.hardtype = ARPPacket.HARDTYPE_ETHER;
        tramaArp.prototype = ARPPacket.PROTOTYPE_IP;
        tramaArp.hlen = 6;
        tramaArp.plen = 4;
        tramaArp.operation = ARPPacket.ARP_REQUEST;
        tramaArp.sender_hardaddr = tarjetaRed.mac_address;
        tramaArp.sender_protoaddr = ipOrigen.getAddress();

        tramaArp.target_hardaddr = macVacia;
        //tramaArp.target_hardaddr  = mac;
        tramaArp.target_protoaddr = ip;
        
        // Se crea la trama EthernetPacket II
        EthernetPacket tramaEthernetII = new EthernetPacket();
        tramaEthernetII.frametype = EthernetPacket.ETHERTYPE_ARP;
        tramaEthernetII.src_mac   = tarjetaRed.mac_address; 
        tramaEthernetII.dst_mac   = macFF;
        
        // Hacer link de la trama ARP a la trama Ethernet II 
        tramaArp.datalink = tramaEthernetII;

        //System.out.println( tramaArp.toString() );
        //System.out.println( Binario.arregloBytesABin(tramaArp.header, " ") );
        
        // Enviar el paquete ARP
        mensajero.sendPacket(tramaArp);

        // Hacer hasta que no queden mas paquetes o se encuentre el paquete adecuado
        while (true)
        {
            // Capturar los paquetes 1 a 1 
            ARPPacket paquete = (ARPPacket) capturador.getPacket();

            // si se deja de recibir paquetes arrojar error
            if(paquete == null)
                throw new IllegalArgumentException(Binario.arregloBytesADeci(ip, ".") + " no respondió al ARP Replay");

            // si llega un paquete con el target_protoaddr (ip destino) igual a nuestro IPV4 retornar su MAC de respuesta
            if(Arrays.equals(paquete.target_protoaddr, ipOrigen.getAddress()) && Arrays.equals(ip, paquete.sender_protoaddr))
            {
                //TODO
                //QUITAR
                System.out.println("MAC recibida: " + Binario.arregloBytesAHexa(paquete.sender_hardaddr, "."));
                return paquete.sender_hardaddr;
            }
            //System.out.println("MAC recibida: " + Binario.arregloBytesAHexa(paquete.sender_hardaddr, "."));
        }
    }


    /**
     * 
     * @param macHostDestino MAC en formato de byte[] de host destino
     * @param ipHostDestino Ip en formato de byte[] de host destino
     * @param tarjetaRed Interfaz de red de la tarjeta de red usada
     * @return verifica que el ARP es correcto
     */
    public static boolean validarARP(byte[] macHostDestino, byte[] ipHostDestino, NetworkInterface tarjetaRed)
    {
        try
        {
            byte[] macRespuesta = arpRequest(ipHostDestino, tarjetaRed);
            if(Arrays.equals(macRespuesta, macHostDestino))
            {
                //TODO
                //QUITAR
                System.out.println("Se encontró IP: " + Binario.arregloBytesADeci(ipHostDestino, ".") + " MAC: " + Binario.arregloBytesAHexa(macHostDestino, ":"));
                return true;
            }
        }
        catch(Error | IOException e)
        {
            System.out.println("Error: " + e);
            return false;
        }
        return false;
    }

}
