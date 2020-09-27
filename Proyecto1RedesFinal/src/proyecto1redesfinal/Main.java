package proyecto1redesfinal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class Main
{

    static NetworkInterface[] array;
    static Scanner entrada = new Scanner(System.in);

    static byte[] macFF = new byte[]
    {
        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff
    };
    static byte[] macVacia = new byte[]
    {
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };

    ArrayList<Dispositivo> dispositivos;

    public String[] cortarCadenaPorPuntos(String cadena)
    {
        return cadena.split("\\.");
    }

    public Main() throws Exception
    {
        mostrarTitulo();

        String[] scannerString;
        byte[] macOrigen, macDestino;
        byte[] ipOrigen, ipDestino;
        short tipoTrama;
        EthernetPacket tramaEthernetII = new EthernetPacket();

        array = JpcapCaptor.getDeviceList();
        for (int i = 0; i < array.length; i++)
        {
            System.out.println(
                    i + " -> " + array[i].description + " -> " + Binario.arregloBytesAHexa(array[i].mac_address, ":"));
        }
        System.out.println("Seleccione la tarjeta de red a usar: ");
        int res = entrada.nextInt();
        NetworkInterface tarjetaRed = array[res];

        System.out.println("Digite el tipo de trama ethernet (ARP -> 2054 o ICMP(ip) -> 2048): ");
        int tipo = entrada.nextShort();
        switch (tipo)
        {
            case 2054: //llamamos a ARP
                tipoTrama = 2054;

                System.out.println("Digite la dirección MAC del Host Origen para la Trama Ethernet II y ARP (FORMATO xx:xx:xx:xx:xx:xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split(":");
                macOrigen = Binario.macHexaStringAByte(scannerString);

                System.out.println("Digite la dirección IP del Host Origen para la Trama ARP (FORMATO xx.xx.xx.xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split("\\.");
                ipOrigen = Binario.ipDeciStringAByte(scannerString);

                System.out.println("Digite la direccion Mac del host Destino para la Trama Ethernet II (Presionar 'Enter' POR DEFECTO -> ff:ff:ff:ff:ff:ff)");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split(":");
                if (scannerString.length < 1 || scannerString == null)
                {
                    macDestino = macFF;
                }
                else
                {
                    macDestino = Binario.macHexaStringAByte(scannerString);
                }

                System.out.println("Digite la dirección IP del Host Destino para la Trama ARP (FORMATO xx.xx.xx.xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split("\\.");
                ipDestino = Binario.ipDeciStringAByte(scannerString);

                //Creación de la trama Ethernet II
                tramaEthernetII.frametype = tipoTrama;
                tramaEthernetII.src_mac = macOrigen;
                tramaEthernetII.dst_mac = macDestino; //esto tiene que ser FF

                byte[] macRespuesta = Arp.arpRequest2(macOrigen, ipOrigen, macDestino, ipDestino, (short) 1, tramaEthernetII, tarjetaRed);
                if (macRespuesta != null)
                {
                    System.out.println("La respuesta recibida fue: " + Binario.arregloBytesAHexa(macRespuesta, ":"));
                }
                else
                {
                    System.out.println(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ARP Request");
                }
                break;

            case 2048: //llamamos a ICMP
                int ttl,
                 tamMensaje,
                 protocol;
                mostrarTituloIcmp();
                tipoTrama = 2048;

                System.out.println("Digite la dirección MAC del Host Origen para la Trama Ethernet II (FORMATO xx:xx:xx:xx:xx:xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split(":");
                macOrigen = Binario.macHexaStringAByte(scannerString);

                System.out.println("Digite la dirección IP del Host Origen para la Trama IPV4 (FORMATO xx.xx.xx.xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split("\\.");
                ipOrigen = Binario.ipDeciStringAByte(scannerString);

                System.out.println("Digite la direccion Mac del host Destino para la Trama Ethernet II (FORMATO xx:xx:xx:xx:xx:xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split(":");
                macDestino = Binario.macHexaStringAByte(scannerString);

                System.out.println("Digite la dirección IP del Host Destino para la Trama IPV4 (FORMATO xx.xx.xx.xx): ");
                entrada = new Scanner(System.in);
                scannerString = entrada.nextLine().split("\\.");
                ipDestino = Binario.ipDeciStringAByte(scannerString);

                System.out.println("Digite TTL para la Trama ICMP: ");
                entrada = new Scanner(System.in);
                ttl = entrada.nextInt();

                System.out.println("Digite el tamaño del mensaje ICMP: ");
                entrada = new Scanner(System.in);
                tamMensaje = entrada.nextInt();

                protocol = 1; // 1 -> ICMP echo Request 

                //Creación de la trama Ethernet II
                tramaEthernetII.frametype = tipoTrama;
                tramaEthernetII.src_mac = macOrigen;
                tramaEthernetII.dst_mac = macDestino;

                byte[] ipRespuesta = ICMP.icmp2(ipOrigen, ipDestino, ttl, tamMensaje, protocol, tramaEthernetII);
                if (ipRespuesta != null)
                {
                    System.out.println("Se recibio el echo reply del host con IP: " + Binario.arregloBytesADeci(ipRespuesta, "."));
                }
                else
                {
                    System.out.println(Binario.arregloBytesADeci(ipDestino, ".") + " no respondió al ICMP echo Request");
                }
                break;

        }
    }

    public final void cargarDispositivos()
    {
        dispositivos = new ArrayList<>();
        dispositivos.add(new Dispositivo("CelularJuanpa", new byte[]
        {
            (byte) 192, (byte) 168, (byte) 0, (byte) 6
        },
                new byte[]
                {
                    (byte) 0x1c, (byte) 0xcc, (byte) 0xd6, (byte) 0xdb, (byte) 0xb4, (byte) 0x18
                }));
        dispositivos.add(new Dispositivo("TVJuanpa", new byte[]
        {
            (byte) 192, (byte) 168, (byte) 0, (byte) 8
        },
                new byte[]
                {
                    (byte) 0xcc, (byte) 0x6e, (byte) 0xa4, (byte) 0x5a, (byte) 0xee, (byte) 0x67
                }));
    }

    private void mostrarTitulo()
    {
        System.out.println("---------------------------------------------------------------");
        System.out.println("----------------------- Tarjetas de Red -----------------------");
        System.out.println("---------------------------------------------------------------");
    }

    private void mostrarTituloArp()
    {
        System.out.println("####################################");
        System.out.println("########### PROTOCOLO ARP ##########");
        System.out.println("####################################");
    }

    private void mostrarTituloIcmp()
    {
        System.out.println("#####################################");
        System.out.println("########### PROTOCOLO ICMP ##########");
        System.out.println("#####################################");
    }

    public final static void clearConsole()
    {
        
    }
}