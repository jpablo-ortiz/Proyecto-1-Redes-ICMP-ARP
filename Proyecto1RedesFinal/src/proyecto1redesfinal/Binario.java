package proyecto1redesfinal;

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
}