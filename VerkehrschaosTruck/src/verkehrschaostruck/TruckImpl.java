package verkehrschaostruck;

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

import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;

public class TruckImpl extends verkehrschaos.TruckPOA {
	
	private String name;
	private TruckCompany company;
	private double x;
	private double y;
	private boolean running = true;
	
	public void truckRun(ORB orb, Truck truck) {
		company.addTruck(truck);
		while (running) {
			
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TruckCompany getCompany() {
		return company;
	}

	@Override
	public void setCompany(TruckCompany company) {
		this.company = company;
	}

	@Override
	public void setCoordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void putOutOfService() {
		running = false;
	}
}
