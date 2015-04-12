package verkehrschaostruckcompany;

import java.util.ArrayList;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import verkehrschaos.ELocationNotFound;
import verkehrschaos.Streets;
import verkehrschaos.StreetsHelper;
import verkehrschaos.TTruckListHolder;
import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;
import verkehrschaos.TruckCompanyHelper;
import verkehrschaos.TruckCompanyPOA;

public class TruckCompanyImpl extends TruckCompanyPOA {
	
	public void set_name(String name) {
		this.name = name;		
	}
	
	public void run(ORB orb, TruckCompany compy, String slot) {
		Streets streets = null;
		_comp = compy;
		try {
			NamingContextExt nc = NamingContextExtHelper.narrow(
			        orb.resolve_initial_references("NameService"));
			org.omg.CORBA.Object obj = nc.resolve_str("Streets");
			streets = StreetsHelper.narrow(obj);
			System.out.println("slot: " + slot);
			streets.claim(compy, slot);
			while(running) {
				// Todo: Zu condition variable oder semaphore machen.
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (streets != null) {
				try {
					streets.free(slot);
				} catch (ELocationNotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

    private String name;
    private ArrayList<Truck> truckList = new ArrayList<Truck>();
    private ArrayList<Truck> truckIsOnTheWay = new ArrayList<Truck>();
    private volatile boolean running = true;
    
    private TruckCompany _comp;
    
    int trucki = 0;
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addTruck(Truck truck) {
		truckList.add(truck);
		System.out.println("addtruck");
	}

	@Override
	public void removeTruck(Truck truck) {
        truckList.remove(truck);
        System.out.println("removeTruck");
	}

	@Override
	public int getTrucks(TTruckListHolder trucks) {
		for (Truck t : truckList) {
			System.out.println("Truck: " + t.getName());
		}
		System.out.println("Truck list is: " + truckList.size());
		trucks.value = new Truck[truckList.size()];
		if (truckList != null && !truckList.isEmpty()) {
			truckList.toArray(trucks.value);
		}
        return truckList != null ? truckList.size() : 0;
	}

	@Override
	public void leave(Truck truck) {
        truckList.remove(truck);
	}

	@Override
	public void advise(Truck truck) {
		truckIsOnTheWay.add(truck);
		truckList.add(truck);
		truck.setCompany(_comp);
	}

	@Override
	public void arrive(Truck truck) {
		truckIsOnTheWay.remove(truck);	
	}

	@Override
	public void putOutOfService() {
		running = false;
	}
}
