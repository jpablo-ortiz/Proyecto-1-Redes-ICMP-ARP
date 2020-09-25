package proyecto1redesfinal;
public class Dispositivo
{
    String name;
    byte[] mac;
    byte[] ip;

    public Dispositivo(String name, byte[] ip, byte[] mac)
    {
        this.mac = mac;
        this.ip = ip;
    }

}
