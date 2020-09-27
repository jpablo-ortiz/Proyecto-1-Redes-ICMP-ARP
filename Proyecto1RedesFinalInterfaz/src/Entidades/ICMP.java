package Entidades;

import java.util.Arrays;
import jpcap.packet.EthernetPacket;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

public class ICMP
{

    public static IcmpPingResponse icmpRequest(byte[] ipOrigen, byte[] ipDestino, int ttl, int tamMensaje, int protocol, int checksum, int typeOfService, int version, EthernetPacket tramaEthernetII)
    {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();

        request.setHost(Binario.arregloBytesADeci(ipDestino, "."));
        request.setPacketSize(tamMensaje);
        request.setTtl(ttl);

        final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);

        if (Arrays.equals(Binario.ipDeciStringAByte(response.getHost().split("\\.")), ipDestino))
        {
            return response;
        }
        else
        {
            return null;
        }
    }
}
