package verkehrschaos;

/**
* verkehrschaos/ETruckCompanyUnknownHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from verkehrschaos.idl
* Sonntag, 5. April 2015 16:48 Uhr MESZ
*/

public final class ETruckCompanyUnknownHolder implements org.omg.CORBA.portable.Streamable
{
  public verkehrschaos.ETruckCompanyUnknown value = null;

  public ETruckCompanyUnknownHolder ()
  {
  }

  public ETruckCompanyUnknownHolder (verkehrschaos.ETruckCompanyUnknown initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = verkehrschaos.ETruckCompanyUnknownHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    verkehrschaos.ETruckCompanyUnknownHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return verkehrschaos.ETruckCompanyUnknownHelper.type ();
  }

}
