package verkehrschaostruckcompany;

/**
 * Praktikum VSP (Prof. Heitmann), SS 15
 * Gruppe: Iacobi, Jannik      | Matrikelnr: 2144481 | jannik.iacobi@haw-hamburg.de 
 *         Kirstein, Katja     | Matrikelnr: 2125137 | katja.kirstein@haw-hamburg.de 
 * Aufgabe 1: Verkehrschaos
 */

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import verkehrschaos.ELocationInUse;
import verkehrschaos.ELocationNotFound;
import verkehrschaos.ELocationNotInUse;
import verkehrschaos.Streets;
import verkehrschaos.StreetsHelper;
import verkehrschaos.TTruckListHolder;
import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;
import verkehrschaos.TruckCompanyPOA;

public class TruckCompanyImpl extends TruckCompanyPOA {

	/**
	 * process variables
	 */
	private volatile boolean running = true;
	private Semaphore sema = new Semaphore(0);

	/**
	 * information variables
	 */
	private String name;
	private ArrayList<Truck> truckList = new ArrayList<Truck>();
	private ArrayList<Truck> truckIsOnTheWay = new ArrayList<Truck>();
	private TruckCompany _comp;

	/**
	 * start the truck company
	 * 
	 * @param orb
	 *            - the CORBA.ORB object
	 * @param compy
	 *            - the own company
	 * @param slot
	 *            - the slot that should be used
	 * @throws InvalidName 
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws InvalidInputException 
	 * @throws ELocationNotFound 
	 * @throws ELocationNotInUse 
	 * @throws ELocationInUse 
	 * @throws InterruptedException 
	 */
	public void run(ORB orb, TruckCompany compy, String slot, String streetsname) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, ELocationNotInUse, ELocationNotFound, InvalidInputException, ELocationInUse, InterruptedException {
		Streets streets = null;
		_comp = compy;
			// claim the user given slot
			NamingContextExt nc = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));
			org.omg.CORBA.Object obj = nc.resolve_str(streetsname); // TODO:
																	// parameterize
			streets = StreetsHelper.narrow(obj);
			System.out.println("slot: " + slot);
			try {
				streets.getTruckCompanyByLocation(slot);
			} catch(ELocationNotInUse e){
				
			} 
			streets.claim(compy, slot);
			while (running) {
				sema.acquire();
			}
			if (streets != null) {
				try {
					streets.free(slot);
				} catch (ELocationNotFound e) {
					e.printStackTrace();
				}
			}
	}

	public synchronized void set_name(String name) {
		this.name = name;
	}

	@Override
	public synchronized String getName() {
		return name;
	}

	@Override
	public synchronized void addTruck(Truck truck) {
		truckList.add(truck);
		System.out.println("addtruck");
	}

	@Override
	public synchronized void removeTruck(Truck truck) {
		if (truckList.contains(truck)) {
			truckList.remove(truck);
		}
		System.out.println("removeTruck");
	}

	@Override
	public synchronized int getTrucks(TTruckListHolder trucks) {
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
	public synchronized void leave(Truck truck) {
		truckList.remove(truck);
	}

	@Override
	public synchronized void advise(Truck truck) {
		truckIsOnTheWay.add(truck);
		truck.setCompany(_comp);
	}

	@Override
	public synchronized void arrive(Truck truck) {
		truckIsOnTheWay.remove(truck);
		truckList.add(truck);
	}

	@Override
	public void putOutOfService() {
		running = false;
		for (int i = truckIsOnTheWay.size() - 1; i >= 0; i--) {
			truckIsOnTheWay.get(i).putOutOfService();
		}
		for (int i = truckList.size() - 1; i >= 0; i--) {
			truckList.get(i).putOutOfService();
		}
		sema.release();
	}
}
