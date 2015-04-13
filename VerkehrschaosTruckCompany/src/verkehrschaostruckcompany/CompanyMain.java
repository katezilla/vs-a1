package verkehrschaostruckcompany;

/**
 * Praktikum VSP (Prof. Heitmann), SS 15
 * Gruppe: Iacobi, Jannik      | Matrikelnr: 2144481 | jannik.iacobi@haw-hamburg.de 
 *         Kirstein, Katja     | Matrikelnr: 2125137 | katja.kirstein@haw-hamburg.de 
 * Aufgabe 1: Verkehrschaos
 */

import java.util.*;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.CosNaming.*;

import verkehrschaos.*;

public class CompanyMain {

    /**
     * TruckCompany program entry
     * 
     * @param args
     *            - the --name=COMPANY_NAME and the --slot=SLOT as well as the
     *            --nameserverport=20000 and the --nameserverhost=lab24
     */
    public static void main(String[] args) {
        try {

            String name = "";
            String slot = "";
            String nsPort = "20000";
            String nsHost = "localhost";
            for (int i = 0; i < args.length; ++i) {
                if (args[i].contains("--name=")) {
                    name = args[i].split("=")[1];
                } else if (args[i].contains("--slot=")) {
                    slot = args[i].split("=")[1];
                } else if (args[i].contains("--nameserverport=")) {
                    nsPort = args[i].split("=")[1];
                } else if (args[i].contains("--nameserverhost=")) {
                    nsHost = args[i].split("=")[1];
                }
            }
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", nsPort);
            props.put("org.omg.CORBA.ORBInitialHost", nsHost);
            ORB orb = ORB.init(args, props);

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
