package verkehrschaos;


/**
* verkehrschaos/ELocationNotFound.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from verkehrschaos.idl
* Sonntag, 5. April 2015 16:48 Uhr MESZ
*/

public final class ELocationNotFound extends org.omg.CORBA.UserException
{
  public String msg = null;

  public ELocationNotFound ()
  {
    super(ELocationNotFoundHelper.id());
  } // ctor

  public ELocationNotFound (String _msg)
  {
    super(ELocationNotFoundHelper.id());
    msg = _msg;
  } // ctor


  public ELocationNotFound (String $reason, String _msg)
  {
    super(ELocationNotFoundHelper.id() + "  " + $reason);
    msg = _msg;
  } // ctor

} // class ELocationNotFound
