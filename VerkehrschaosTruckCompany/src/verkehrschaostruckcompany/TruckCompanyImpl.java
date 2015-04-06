package verkehrschaostruckcompany;

import java.util.ArrayList;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

import verkehrschaos.TTruckListHolder;
import verkehrschaos.Truck;
import verkehrschaos.TruckCompany;

public class TruckCompanyImpl implements TruckCompany {

    /**
     * generated serial ID
     */
    private static final long serialVersionUID = 7602071591051678920L;

    /**
     * the name of the truck company
     */
    private String name;

    private ArrayList<Truck> truckList;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addTruck(Truck truck) {
        if (truck != null) {
            truckList.add(truck);
        }
    }

    @Override
    public void removeTruck(Truck truck) {
        if (truck != null) {
            truckList.remove(truck);
        }
    }

    @Override
    public int getTrucks(TTruckListHolder trucks) {
        trucks.value = truckList.toArray(trucks.value); // TODO: correct usage
                                                        // of toArray?
        return truckList.size();
    }

    @Override
    public void leave(Truck truck) {
        this.removeTruck(truck); // TODO: check if actually same behaviour
    }

    @Override
    public void advise(Truck truck) {
        this.addTruck(truck);
        truck.setCompany(this);
    }

    @Override
    public void arrive(Truck truck) {
        // TODO: what behaviour needed?
    }

    @Override
    public void putOutOfService() {
        // TODO Auto-generated method stub
    }

    @Override
    public Request _create_request(Context arg0, String arg1, NVList arg2,
            NamedValue arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Request _create_request(Context arg0, String arg1, NVList arg2,
            NamedValue arg3, ExceptionList arg4, ContextList arg5) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object _duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DomainManager[] _get_domain_managers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object _get_interface_def() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Policy _get_policy(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int _hash(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean _is_a(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean _is_equivalent(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean _non_existent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void _release() {
        // TODO Auto-generated method stub

    }

    @Override
    public Request _request(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object _set_policy_override(Policy[] arg0, SetOverrideType arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
