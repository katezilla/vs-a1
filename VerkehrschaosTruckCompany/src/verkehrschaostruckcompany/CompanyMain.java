package verkehrschaostruckcompany;

import java.util.*;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.CosNaming.*;

import verkehrschaos.*;

public class CompanyMain {

    /**
     * @param args
     * @throws InvalidName
     * @throws AdapterInactive
     */
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "20000");
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            ORB orb = ORB.init(args, props);

            String name = "";
            String slot = "";
            for (int i = 0; i < args.length; ++i) {
                if (args[i].contains("--name=")) {
                    name = args[i].split("=")[1];
                }
                if (args[i].contains("--slot=")) {
                    slot = args[i].split("=")[1];
                }
            }

            POA rootPoa = POAHelper.narrow(orb
                    .resolve_initial_references("RootPOA"));
            rootPoa.the_POAManager().activate();

            TruckCompanyImpl company = new TruckCompanyImpl();
            company.set_name(name);
            org.omg.CORBA.Object ref = rootPoa.servant_to_reference(company);

            TruckCompany href = TruckCompanyHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb
                    .resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("Company ready and waiting...");

            company.run(orb, href, slot);
            ncRef.unbind(path);

            orb.shutdown(true);
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        System.out.println("Company exiting");
    }
}
