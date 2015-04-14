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
	 * TruckCompany program entry, set up corba connections
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
			String streetsname = "";
			for (int i = 0; i < args.length; ++i) {
				if (args[i].contains("--name=")) {
					name = args[i].split("=")[1];
				} else if (args[i].contains("--slot=")) {
					slot = args[i].split("=")[1];
				} else if (args[i].contains("--streetsname=")) {
					streetsname = args[i].split("=")[1];
				} else if (args[i].contains("--nameserverport=")) {
					nsPort = args[i].split("=")[1];
				} else if (args[i].contains("--nameserverhost=")) {
					nsHost = args[i].split("=")[1];
				}
			}

			checkInputValues(name, slot, nsPort, nsHost);

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
			try {
				ncRef.resolve_str(name);
				// if no Exception is thrown, the name is already used
				throw new InvalidInputException("Name already used");
			} catch (org.omg.CosNaming.NamingContextPackage.NotFound e) {
				// no object found, so name is not in use
			}
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("Company running");
			company.run(orb, href, slot, streetsname);

			ncRef.unbind(path);
			orb.shutdown(true);
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Company destroyed");
	}

	private static void checkInputValues(String name, String slot,
			String nsPort, String nsHost) throws InvalidInputException {
		switch (slot) {
		case "nord":
			break;
		case "sued":
			break;
		case "ost":
			break;
		case "west":
			break;
		default:
			throw new InvalidInputException("Invalid slot");
		}
	}
}
