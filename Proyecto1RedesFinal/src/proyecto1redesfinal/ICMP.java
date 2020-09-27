package proyecto1redesfinal;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpcap.packet.EthernetPacket;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

public class ICMP
{

    public static byte[] icmp2(byte[] ipOrigen, byte[] ipDestino, int ttl, int tamMensaje, int protocol, EthernetPacket tramaEthernetII)
    {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        
        request.setHost(Binario.arregloBytesADeci(ipDestino, "."));
        request.setPacketSize(tamMensaje);
        request.setTtl(ttl);

        final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);

        if (Arrays.equals(Binario.ipDeciStringAByte(response.getHost().split("\\.")), ipDestino))
        {
            System.out.println("La duración del echo request es de: " + response.getDuration() + " ms" );
            return Binario.ipDeciStringAByte(response.getHost().split("\\.")); // retornamos ip host origen del response
        }
        else
        {
            return null;
        }
    }
}
