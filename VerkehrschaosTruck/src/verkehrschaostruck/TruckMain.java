package verkehrschaostruck;

/**
 * Praktikum VSP (Prof. Heitmann), SS 15
 * Gruppe: Iacobi, Jannik      | Matrikelnr: 2144481 | jannik.iacobi@haw-hamburg.de 
 *         Kirstein, Katja     | Matrikelnr: 2125137 | katja.kirstein@haw-hamburg.de 
 * Aufgabe 1: Verkehrschaos
 */

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import verkehrschaos.TruckCompanyHelper;
import verkehrschaos.TruckHelper;

public class TruckMain {

    /**
     * Truck program entry
     * 
     * @param args
     *            - the --name=COMPANY_NAME and the --company=COMPANY_NAME as
     *            well as the --nameserverport=20000 and the
     *            --nameserverhost=lab24
     */
    public static void main(String[] args) {
        try {
            String name = "";
            String company = "";
            String nsPort = "20000";
            String nsHost = "localhost";
            for (int i = 0; i < args.length; ++i) {
                if (args[i].contains("--name=")) {
                    name = args[i].split("=")[1];
                } else if (args[i].contains("--company=")) {
                    company = args[i].split("=")[1];
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

            NamingContextExt nc = NamingContextExtHelper.narrow(orb
                    .resolve_initial_references("NameService"));

            TruckImpl truck = new TruckImpl();
            truck.setName(name);
            truck.setCompany(TruckCompanyHelper.narrow(nc.resolve_str(company)));
            org.omg.CORBA.Object ref = rootPoa.servant_to_reference(truck);

            NameComponent path[] = nc.to_name(name);
            nc.rebind(path, ref);

            System.out.println("naming");
            truck.truckRun(orb, TruckHelper.narrow(nc.resolve_str(name)));
            nc.unbind(path);
            orb.shutdown(true);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        System.out.println("truck destroring");
    }
}
