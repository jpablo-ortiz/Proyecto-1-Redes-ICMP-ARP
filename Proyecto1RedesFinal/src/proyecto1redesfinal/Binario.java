package proyecto1redesfinal;

import java.util.Arrays;

public class Binario {

    public static String arregloBytesAHexa(byte[] arr, String separador) {
        String cadena = "";
        for (int i = 0; i < arr.length; i++) {
            String conver = "" + Integer.toHexString((int) Byte.toUnsignedLong(arr[i]));
            if (conver.length() == 1)
                cadena += "0" + conver;
            else
                cadena += conver;

            if (i < arr.length - 1)
                cadena += separador;
        }
        return cadena;
    }

    public static String arregloBytesADeci(byte[] arr, String separador) {
        String cadena = "";

        for (int i = 0; i < arr.length; i++) {
            cadena += Integer.toString((int) Byte.toUnsignedLong(arr[i]));
            if (i < arr.length - 1)
                cadena += separador;
        }
        return cadena;
    }

    public static String arregloBytesABin(byte[] arr, String separador) {
        String cadena = "";

        for (int i = 0; i < arr.length; i++) {
            cadena += Integer.toBinaryString((int) Byte.toUnsignedLong(arr[i]));
            if (i < arr.length - 1)
                cadena += separador;
        }
        return cadena;
    }

    public static String decimalABinario(int numero) {
        return Integer.toBinaryString(numero);
    }

    public static byte[] macHexaStringAByte(String[] macString)
    {
        byte[] mac = new byte[6];
        for (int j = 0; j < macString.length; j++)
        {
            mac[j] = Integer.decode("0x"+macString[j]).byteValue();
        }
        return mac;
    }
    
    public static byte[] ipDeciStringAByte(String[] ipString)
    {
        byte[] ip = new byte[4];
        for (int j = 0; j < ipString.length; j++)
        {
            Integer aux = Integer.parseInt(ipString[j]);
            ip[j] = aux.byteValue();
        }
        return ip;                
    }
}
