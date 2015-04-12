package verkehrschaostruck;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;
import verkehrschaos.TruckCompanyHelper;
import verkehrschaos.TruckHelper;

public class TruckMain {



	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "20000");
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			ORB orb = ORB.init(args, props);
			String name = "";
			String company = "";
			for (int i = 0; i < args.length; ++i) {
				if (args[i].contains("--name=")) {
					name = args[i].split("=")[1];
				}
				if (args[i].contains("--company=")) {
					company = args[i].split("=")[1];
				}
			}
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

			System.out.println("nameing");
			truck.truckRun(orb, TruckHelper.narrow(nc.resolve_str(name)));
			nc.unbind(path);
			orb.shutdown(true);
			

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		System.out.println("truck destroring");
	}
}
