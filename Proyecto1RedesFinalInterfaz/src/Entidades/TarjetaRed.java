package Entidades;

import jpcap.NetworkInterface;

public class TarjetaRed
{

    private NetworkInterface networkInterface;
    private int indice;

    public TarjetaRed(NetworkInterface networkInterface, int indice)
    {
        this.networkInterface = networkInterface;
        this.indice = indice;
    }

    @Override
    public String toString()
    {
        return indice + " -> " + networkInterface.description + " -> " + Binario.arregloBytesAHexa(networkInterface.mac_address, ":");
    }

    public NetworkInterface getNetworkInterface()
    {
        return networkInterface;
    }

    public void setNetworkInterface(NetworkInterface networkInterface)
    {
        this.networkInterface = networkInterface;
    }

    public int getIndice()
    {
        return indice;
    }

    public void setIndice(int indice)
    {
        this.indice = indice;
    }

}
