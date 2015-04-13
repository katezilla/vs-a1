package verkehrschaostruck;

import java.util.concurrent.Semaphore;

import org.omg.CORBA.ORB;

import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;

public class TruckImpl extends verkehrschaos.TruckPOA {

    private volatile boolean running = true;
    private Semaphore sema = new Semaphore(0);

    private String name;
    private TruckCompany company;
    @SuppressWarnings("unused")
    private double x;
    @SuppressWarnings("unused")
    private double y;

    public void truckRun(ORB orb, Truck truck) {
        company.addTruck(truck);
        try {
            while (running) {
                sema.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        company.removeTruck(truck);
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
        sema.release();
    }
}
