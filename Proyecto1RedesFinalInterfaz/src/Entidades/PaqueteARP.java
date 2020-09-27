package Entidades;

import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class PaqueteARP
{

    static byte[] MAC_FF =
    {
        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff
    };
    static byte[] MAC_Vacia =
    {
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };

    static byte[] HARDTYPE_ETHERNET =
    {
        (byte) 0, (byte) 1
    }; // igual a short = 1
    static byte[] PROTOCOLTYPE_IP =
    {
        (byte) 8, (byte) 0
    }; // igual a short = 2048 
    static byte[] ARP_REQUEST =
    {
        (byte) 0, (byte) 1
    }; // igual a short = 1
    static byte[] ARP_REPLAY =
    {
        (byte) 0, (byte) 2
    }; // iguar a short = 2
    static byte[] HLEN =
    {
        (byte) 0, (byte) 6
    }; // iguar a short = 6
    static byte[] PLEN =
    {
        (byte) 0, (byte) 4
    }; // iguar a short = 4

    private byte[] hardwareType;
    private byte[] protocoltype;
    private byte[] hardwareLen;
    private byte[] protocolLen;
    private byte[] operation;
    private byte[] SenderHardwareAddress;
    private byte[] SenderProtocolAddress;
    private byte[] TargetHardwareAddress;
    private byte[] TargetProtocolAddress;

    private Packet p;

    PaqueteARP(Packet packet)
    {
        this.p = packet;
        this.hardwareType = new byte[2];
        this.protocoltype = new byte[2];
        this.hardwareLen = new byte[1];
        this.protocolLen = new byte[1];
        this.operation = new byte[2];
        this.SenderHardwareAddress = new byte[6];
        this.SenderProtocolAddress = new byte[4];
        this.TargetHardwareAddress = new byte[6];
        this.TargetProtocolAddress = new byte[4];

        if (this.p != null)
        {
            descomprimirData();
        }
    }

    public PaqueteARP(byte[] macHostOrigen, byte[] ipHostOrigen, byte[] macHostDestino, byte[] ipHostDestino, short hardType, short prototype, short hardwareLen, short prototypeLen, short operation, EthernetPacket tramaEthernetII)
    {
        this.hardwareType = Binario.shortAByte(hardType);
        this.protocoltype = Binario.shortAByte(prototype);
        this.hardwareLen = Binario.shortAByteTam1(hardwareLen);
        this.protocolLen = Binario.shortAByteTam1(prototypeLen);
        this.operation = Binario.shortAByte(operation);
        this.SenderHardwareAddress = macHostOrigen;
        this.SenderProtocolAddress = ipHostOrigen;
        this.TargetHardwareAddress = macHostDestino;
        this.TargetProtocolAddress = ipHostDestino;

        //Se crea un nuevo paquete
        p = new Packet();
        //Se ingresa el contenido de la trama ARP en el data
        p.data = convertirAData();
        // Hacer link de la trama ARP a la trama Ethernet II 
        p.datalink = tramaEthernetII;
    }

    public final byte[] convertirAData()
    {
        int tamTotal = hardwareType.length + protocoltype.length + hardwareLen.length + protocolLen.length + operation.length + SenderHardwareAddress.length + SenderProtocolAddress.length + TargetHardwareAddress.length + TargetProtocolAddress.length;
        byte[] data = new byte[tamTotal];

        int tam = 0;
        System.arraycopy(hardwareType, 0, data, tam, hardwareType.length);
        tam += hardwareType.length;
        System.arraycopy(protocoltype, 0, data, tam, protocoltype.length);
        tam += protocoltype.length;
        System.arraycopy(hardwareLen, 0, data, tam, hardwareLen.length);
        tam += hardwareLen.length;
        System.arraycopy(protocolLen, 0, data, tam, protocolLen.length);
        tam += protocolLen.length;
        System.arraycopy(operation, 0, data, tam, operation.length);
        tam += operation.length;
        System.arraycopy(SenderHardwareAddress, 0, data, tam, SenderHardwareAddress.length);
        tam += SenderHardwareAddress.length;
        System.arraycopy(SenderProtocolAddress, 0, data, tam, SenderProtocolAddress.length);
        tam += SenderProtocolAddress.length;
        System.arraycopy(TargetHardwareAddress, 0, data, tam, TargetHardwareAddress.length);
        tam += TargetHardwareAddress.length;
        System.arraycopy(TargetProtocolAddress, 0, data, tam, TargetProtocolAddress.length);
        return data;
    }

    public final void descomprimirData()
    {
        int tamTotal = p.header.length - 1;
        int j;

        j = 1;
        for (int i = tamTotal; i > tamTotal - TargetProtocolAddress.length; i--, j++)
        {
            TargetProtocolAddress[TargetProtocolAddress.length - j] = p.header[i];
        }
        tamTotal -= TargetProtocolAddress.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - TargetHardwareAddress.length; i--, j++)
        {
            TargetHardwareAddress[TargetHardwareAddress.length - j] = p.header[i];
        }
        tamTotal -= TargetHardwareAddress.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - SenderProtocolAddress.length; i--, j++)
        {
            SenderProtocolAddress[SenderProtocolAddress.length - j] = p.header[i];
        }
        tamTotal -= SenderProtocolAddress.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - SenderHardwareAddress.length; i--, j++)
        {
            SenderHardwareAddress[SenderHardwareAddress.length - j] = p.header[i];
        }
        tamTotal -= SenderHardwareAddress.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - operation.length; i--, j++)
        {
            operation[operation.length - j] = p.header[i];
        }
        tamTotal -= operation.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - protocolLen.length; i--, j++)
        {
            protocolLen[protocolLen.length - j] = p.header[i];
        }
        tamTotal -= protocolLen.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - hardwareLen.length; i--, j++)
        {
            hardwareLen[hardwareLen.length - j] = p.header[i];
        }
        tamTotal -= hardwareLen.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - protocoltype.length; i--, j++)
        {
            protocoltype[protocoltype.length - j] = p.header[i];
        }
        tamTotal -= protocoltype.length;

        j = 1;
        for (int i = tamTotal; i > tamTotal - hardwareType.length; i--, j++)
        {
            hardwareType[hardwareType.length - j] = p.header[i];
        }
    }

    @Override
    public String toString()
    {
        return " [ Hardware type: " + Binario.arregloBytesADeci(hardwareType, " ") + " | Protocol type: " + Binario.arregloBytesADeci(protocoltype, " ") + " ] "
                + "\n [ Hardware address length: " + Binario.arregloBytesADeci(hardwareLen, " ") + " | Protocol address length: " + Binario.arregloBytesADeci(protocolLen, " ") + " | Operation code: " + Binario.arregloBytesADeci(operation, " ") + " ] "
                + "\n [ Source hardware address: " + Binario.arregloBytesAHexa(SenderHardwareAddress, ":") + " ] "
                + "\n [ Source protocol address: " + Binario.arregloBytesADeci(SenderProtocolAddress, ".") + " ] "
                + "\n [ Target hardware address: " + Binario.arregloBytesAHexa(TargetHardwareAddress, ":") + " ] "
                + "\n [ Target protocol address: " + Binario.arregloBytesADeci(TargetProtocolAddress, ".") + " ] ";
    }

    public Packet getP()
    {
        return p;
    }

    public void setP(Packet p)
    {
        this.p = p;
    }

    public byte[] getHardwareType()
    {
        return hardwareType;
    }

    public void setHardwareType(byte[] hardwareType)
    {
        this.hardwareType = hardwareType;
    }

    public byte[] getProtocoltype()
    {
        return protocoltype;
    }

    public void setProtocoltype(byte[] protocoltype)
    {
        this.protocoltype = protocoltype;
    }

    public byte[] getHardwareLen()
    {
        return hardwareLen;
    }

    public void setHardwareLen(byte[] hardwareLen)
    {
        this.hardwareLen = hardwareLen;
    }

    public byte[] getProtocolLen()
    {
        return protocolLen;
    }

    public void setProtocolLen(byte[] protocolLen)
    {
        this.protocolLen = protocolLen;
    }

    public byte[] getOperation()
    {
        return operation;
    }

    public void setOperation(byte[] operation)
    {
        this.operation = operation;
    }

    public byte[] getSenderHardwareAddress()
    {
        return SenderHardwareAddress;
    }

    public void setSenderHardwareAddress(byte[] SenderHardwareAddress)
    {
        this.SenderHardwareAddress = SenderHardwareAddress;
    }

    public byte[] getSenderProtocolAddress()
    {
        return SenderProtocolAddress;
    }

    public void setSenderProtocolAddress(byte[] SenderProtocolAddress)
    {
        this.SenderProtocolAddress = SenderProtocolAddress;
    }

    public byte[] getTargetHardwareAddress()
    {
        return TargetHardwareAddress;
    }

    public void setTargetHardwareAddress(byte[] TargetHardwareAddress)
    {
        this.TargetHardwareAddress = TargetHardwareAddress;
    }

    public byte[] getTargetProtocolAddress()
    {
        return TargetProtocolAddress;
    }

    public void setTargetProtocolAddress(byte[] TargetProtocolAddress)
    {
        this.TargetProtocolAddress = TargetProtocolAddress;
    }

}
