package Entidades;

import java.util.ArrayList;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class TarjetasRed
{

    ArrayList<TarjetaRed> tarjetasRed;
    NetworkInterface[] array;

    public TarjetasRed()
    {
        tarjetasRed = new ArrayList<>();
        array = JpcapCaptor.getDeviceList();
        for (int i = 0; i < array.length; i++)
        {
            tarjetasRed.add(new TarjetaRed(array[i], i));
        }
    }

    public ArrayList<TarjetaRed> getTarjetasRed()
    {
        return tarjetasRed;
    }

    public void setTarjetasRed(ArrayList<TarjetaRed> tarjetasRed)
    {
        this.tarjetasRed = tarjetasRed;
    }

    public NetworkInterface[] getArray()
    {
        return array;
    }

    public void setArray(NetworkInterface[] array)
    {
        this.array = array;
    }

}
