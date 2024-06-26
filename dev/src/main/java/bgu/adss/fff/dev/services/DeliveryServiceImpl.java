package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DeliveryRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Delivery;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.domain.models.ShiftDayPart;
import bgu.adss.fff.dev.exceptions.DeliveryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static bgu.adss.fff.dev.domain.models.Constants.SHIFT_CHANGE_HOUR;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private static final String DRIVER_ROLE = "driver";
    private static final String LICENSE_FIELD = "license";
    private static final String STOREKEEPER_ROLE = "storekeeper";

    private final ShiftService shiftService;
    private final BranchService branchService;
    private final DeliveryRepository repository;
    private final EmployeeService employeeService;

    @Autowired
    public DeliveryServiceImpl(
            ShiftService shiftService,
            BranchService branchService,
            DeliveryRepository repository,
            EmployeeService employeeService) {
        this.repository = repository;
        this.shiftService = shiftService;
        this.branchService = branchService;
        this.employeeService = employeeService;
    }

    /**
     * @return All the deliveries in the system.
     */
    public List<Delivery> getDeliveries() {
        return repository.findAll();
    }

    /**
     * Creates a new delivery from the source branch to the destinations. Automatically assigns an available
     * driver with the given license type (if exists). Additionally, checks if all the destinations have a
     * storekeeper inorder to accept the delivery.
     *
     *
     * @param source The name of the source branch.
     * @param start The time the delivery is set to start.
     * @param truckNumber The number of the truck which will be used for this delivery.
     * @param license The license type required for the truck.
     * @param destinations The destinations this truck must go through.
     * @return a Delivery object.
     */
    @Override
    public Delivery registerDelivery(
            String source, LocalDateTime start, long truckNumber, String license, List<String> destinations) {

        // Retrieving a driver:
        Branch sourceBranch = branchService.getBranch(source);
        List<Employee> availableDrivers = getDrivers(sourceBranch, start);
        Employee goodDriver = findFittingDriver(availableDrivers, license);
        if(goodDriver == null) throw DeliveryException.noAvailableDriver();

        // Checking for storekeepers:
        List<Branch> destinationsConverted = destinations.stream().map(branchService::getBranch).toList();
        hasStorekeepersAvailable(start, destinationsConverted);

        Delivery delivery = new Delivery(sourceBranch, goodDriver, start, truckNumber, license, destinationsConverted);
        return repository.save(delivery);
    }

    /**
     * Throws an exception if there are no employees which can accept a delivery ===
     * There are no available storekeepers at the destinations with in the given time.
     *
     * @param time The time to infer shift.
     * @param branches The list of branched which expected to get the delivery.
     */
    private void hasStorekeepersAvailable(LocalDateTime time, List<Branch> branches) {
        ShiftDayPart part = time.getHour() < SHIFT_CHANGE_HOUR ? ShiftDayPart.MORNING : ShiftDayPart.EVENING;
        for(Branch b : branches) {
            List<Employee> emps = shiftService.getAvailableEmployees(time.toLocalDate(), part, b);
            boolean hasStoreKeeper = false;
            for(Employee emp : emps) {
                hasStoreKeeper |= isStorekeeper(emp);
            }
            if(!hasStoreKeeper) throw DeliveryException.noAvailableStorekeeper(b);
        }
    }

    /**
     * Checks whether a given employee is a storekeeper.
     *
     * @param emp The employee.
     * @return True iff the employee has the storekeeper role.
     */
    private boolean isStorekeeper(Employee emp) {
        for(Role r : emp.getRoles()) {
            if(r.getName().equals(STOREKEEPER_ROLE)) return true;
        }
        return false;
    }

    /**
     * Finds a driver with a fitting license type.
     *
     * @param drivers The list of available drivers.
     * @param license The license required.
     * @return A driver if exists or null.
     */
    private Employee findFittingDriver(List<Employee> drivers, String license) {
        Role driver = null;
        for(Employee emp : drivers) {
            // emp \in registerDelivery.availableDrivers => emp is driver => driver \in emp.getRoles()
            if(driver == null) driver = getDriverRole(emp);
            // if the license is fitting the requirement:
            if(employeeService.getCustomField(emp, driver, LICENSE_FIELD).getValue().equals(license))
                return emp;
        }
        return null;
    }

    /**
     * Allows to retrieve the role without having the role service here.
     * Just to not change the submission and annoy daniel. >.<
     *
     * @param emp The employee which is presumably a driver.
     * @return The role of a driver.
     */
    private Role getDriverRole(Employee emp) {
        for(Role r : emp.getRoles()) {
            if(r.getName().equals(DRIVER_ROLE)) return r;
        }
        return null;
    }

    /**
     * Returns all the drivers that available in the time of start at the source.
     * @post \forall e \in output -> e is driver.
     *
     * @param source The source branch.
     * @param time The time that the delivery is set to.
     * @return list of employees which are available at the time this delivery is set and are drivers.
     */
    private List<Employee> getDrivers(Branch source, LocalDateTime time) {
        ShiftDayPart part = time.getHour() < SHIFT_CHANGE_HOUR ? ShiftDayPart.MORNING : ShiftDayPart.EVENING;
        return shiftService.getAvailableEmployees(time.toLocalDate(), part, source)
                .stream().filter(this::containsDriverRole).toList();
    }

    /**
     * Checks if an employee contains the driver role.
     *
     * @param emp The employee.
     * @return True iff contains the driver role.
     */
    private boolean containsDriverRole(Employee emp) {
        for(Role r : emp.getRoles()) {
            if(r.getName().equals(DRIVER_ROLE)) return true;
        }
        return false;
    }
}
