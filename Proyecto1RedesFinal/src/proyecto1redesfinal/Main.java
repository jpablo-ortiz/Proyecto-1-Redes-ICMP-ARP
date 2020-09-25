package proyecto1redesfinal;

import java.util.ArrayList;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class Main
{

    static NetworkInterface[] array;
    static Scanner entrada = new Scanner(System.in);

    ArrayList<Dispositivo> dispositivos;

    public Main() throws Exception
    {

        cargarDispositivos();

        array = JpcapCaptor.getDeviceList();
        for (int i = 0; i < array.length; i++)
        {
            System.out.println(
                    i + " -> " + array[i].description + " -> " + Binario.arregloBytesAHexa(array[i].mac_address, ":"));
        }
        System.out.println("Seleccione la tarjeta de red a usar: ");
        int res = entrada.nextInt();
        NetworkInterface tarjetaRed = array[res];


        Dispositivo actual = dispositivos.get(1);

        boolean validarARP = Arp.validarARP(actual.mac, actual.ip, tarjetaRed);
        if (validarARP)
        {
            System.out.println("Digite el tamaño del mensaje ICMP: ");
            res = entrada.nextInt();
            ICMP.icmp(actual.ip, res);
        }
        else
        {
            System.out.println("Fallo al realizar el proceso del mensaje ARP");
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

}
