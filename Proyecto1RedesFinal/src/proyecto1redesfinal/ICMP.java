package proyecto1redesfinal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

public class ICMP {

    public static void icmp(byte[] ipDestino, int sizeMessage)
    {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost(Binario.arregloBytesADeci(ipDestino, "."));
        request.setPacketSize(sizeMessage);
        //request.setHost("192.168.0.8");
       
        for (int count = 1; count <= 1; count++)
        {
            final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);

            final String formattedResponse = IcmpPingUtil.formatResponse(response);
            System.out.println(formattedResponse);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ICMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}