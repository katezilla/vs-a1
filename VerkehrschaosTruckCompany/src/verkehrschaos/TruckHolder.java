package verkehrschaos;

/**
* verkehrschaos/TruckHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from verkehrschaos.idl
* Sonntag, 5. April 2015 16:48 Uhr MESZ
*/

public final class TruckHolder implements org.omg.CORBA.portable.Streamable
{
  public verkehrschaos.Truck value = null;

  public TruckHolder ()
  {
  }

  public TruckHolder (verkehrschaos.Truck initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = verkehrschaos.TruckHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    verkehrschaos.TruckHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return verkehrschaos.TruckHelper.type ();
  }

}
