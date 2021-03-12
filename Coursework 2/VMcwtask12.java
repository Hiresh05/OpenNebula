/**
 * VM Example class to introduce OpenNebula Cloud API (OCA)
 *
 * Example 2
 * @author Karim Djemame
 * @version 1.0 [2017-02-16]
 *
 */


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;


public class VMcwtask12 {
    /**
     * Logs into the cloud requesting the user's name and password
     *
     * @param oneClient
     * @return
     */
    private OneResponse rc;
    private ArrayList<Integer> allHostID = new ArrayList<Integer>();
    private ArrayList<String> allHostNM = new ArrayList<>();
    private ArrayList <HOSTPERF> arrHost = new ArrayList<HOSTPERF>();
    private static DecimalFormat df2 = new DecimalFormat(".##");
    /**
     * Prints out all the host information available. Along with adding the HOST IDs to an array for future use
     * @param oneClient
     */
    public void retrieveInformation(Client oneClient)
    {

        try
        {
            HostPool pool = new HostPool( oneClient );
            pool.info();

            double cpuUsage, memUsage, diskUsage;
            for( Host host: pool)
            {
                rc = host.info();
                if (host.state() == 2){
                    //this.allHostID.add(Integer.parseInt(host.xpath("/HOST/ID")));
                    cpuUsage = (Double.parseDouble(host.xpath("/HOST/HOST_SHARE/CPU_USAGE"))/Double.parseDouble(host.xpath("/HOST/HOST_SHARE/MAX_CPU")))*100;
                    memUsage = (Double.parseDouble(host.xpath("/HOST/HOST_SHARE/MEM_USAGE"))/Double.parseDouble(host.xpath("/HOST/HOST_SHARE/MAX_MEM")))*100;
                    diskUsage = (Double.parseDouble(host.xpath("/HOST/HOST_SHARE/DISK_USAGE"))/Double.parseDouble(host.xpath("/HOST/HOST_SHARE/MAX_DISK")))*100;

                    int numVM = Integer.parseInt(host.xpath("/HOST/HOST_SHARE/RUNNING_VMS"));

                    arrHost.add(new HOSTPERF(Integer.parseInt(host.xpath("/HOST/ID")), (host.xpath("/HOST/NAME")).toString(), cpuUsage, memUsage, diskUsage, numVM));

                }

            }

            //vmAllocation(oneClient, arrHostHigh, arrHostMed, arrHostLow);
            arrHost.sort(Comparator.comparingDouble(HOSTPERF::getCpuUsage));
            //arrHost.sort(Comparator.comparingDouble(HOSTPERF::getCpuUsage));
            System.out.println("Physical Hosts with resource usage:");
            System.out.println("HOSTID\tHOST NAME\tCPU Usage\tMem Usage\tDisk Usage\tVMs");
            for(HOSTPERF h: arrHost)
            {
                System.out.println(h.HOSTID + "\t" +h.HOSTNAME + "\t" + df2.format(h.HostCpuUsage) +"\t\t" + df2.format(h.HostMemUsage) + "\t\t" + h.HostDiskUsage + "\t\t" + h.NumVM);
                this.allHostID.add(h.HOSTID);
                this.allHostNM.add(h.HOSTNAME);
            }
            System.out.println();


        }catch(Exception e){
            System.out.println("Error viewing all of the Host info");
            e.printStackTrace();
        }
    }
    
    /*class of HOST*/
    public class HOSTPERF
    {
        int HOSTID;
        String HOSTNAME;
        double HostCpuUsage;
        double HostMemUsage;
        double HostDiskUsage;
        int NumVM;

        public HOSTPERF(int _hostID, String _hostName, double _cpuUsage, double _memUsage, double _diskUsage, int _numVM)
        {
            HOSTID = _hostID;
            HOSTNAME = _hostName;
            HostCpuUsage = _cpuUsage;
            HostMemUsage = _memUsage;
            HostDiskUsage = _diskUsage;
            NumVM = _numVM;
        }

        public int getID(){
            return HOSTID;
        }
        public String getName(){
            return HOSTNAME;
        }
        public double getCpuUsage(){
            return HostCpuUsage;
        }
        public double getMemUsage(){
            return HostMemUsage;
        }
        public double getDiskUsage(){
            return HostDiskUsage;
        }
        public int getNumVM(){
            return NumVM;
        }
    }

    
    public Client logIntoCloud() {

        String passwd;
        Client oneClient = null;
        System.out.println("Enter your password: ");
        String username = System.getProperty("user.name");
        passwd = new String(System.console().readPassword("[%s]", "Password:"));
        try
        {
            oneClient = new Client(username + ":" + passwd, "https://csgate1.leeds.ac.uk:2633/RPC2");
            System.out.println("Authentication successful ...");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Incorrect Password. Program Closing.");
            System.exit(1);
        }
        return oneClient;
    }

    public static void main(String[] args) {

        System.out.println("Task 2 start:");

        try {
            //create the VMSample object to complete the coursework
            VMcwtask12 VMSample = new VMcwtask12();

            //log into the cloud and return the client
            Client oneClient = VMSample.logIntoCloud();

            String vmTemplate =
                    "CPU=\"0.1\"\n"
                            + "VCPU=\"1\"\n"
                            + "SCHED_DS_REQUIREMENTS=\"NAME=system\"\n"
                            + "LOGO=\"images/logos/debian.png\"\n"
                            + "DESCRIPTION=\"Debian stretch x86_64 instance with Hadoop, VNC and DHCP. Available for testing purposes. In raw format.\"\n"
                            + "GRAPHICS=[\n"
                            + "\tLISTEN=\"0.0.0.0\",\n"
                            + "\tKEYMAP=\"en-gb\",\n"
                            + "\tTYPE=\"vnc\" ]\n"
                            + "MEMORY=\"512\"\n"
                            + "HYPERVISOR=\"kvm\"\n"
                            + "SUNSTONE_NETWORK_SELECT=\"YES\"\n"
                            + "SUNSTONE_CAPACITY_SELECT=\"YES\"\n"
                            + "DISK=[\n"
                            + "\tIMAGE_UNAME=\"scsrek\",\n"
                            + "\tIO=\"native\",\n"
                            + "\tDEV_PREFIX=\"vd\",\n"
                            + "\tCACHE=\"none\",\n"
                            + "\tTARGET=\"vda\",\n"
                            + "\tDRIVER=\"qcow2\",\n"
                            + "\tREADONLY=\"no\",\n"
                            + "\tTYPE=\"FILE\",\n"
                            + "\tDATASTORE=\"default\",\n"
                            + "\tIMAGE=\"Debian Stretch (Hadoop) x86_64 Base\" ]\n"
                            + "FEATURES=[\n"
                            + "\tAPIC=\"yes\",\n"
                            + "\tACPI=\"yes\" ]\n"
                            + "NIC=[\n"
                            + "\tNETWORK_UNAME=\"oneadmin\",\n"
                            + "\tMODEL=\"virtio\",\n"
                            + "\tVLAN=\"YES\",\n"
                            + "\tNETWORK=\"vnet1\" ]\n"
                    ;

            System.out.println("Virtual Machine Template:\n" + vmTemplate);
            System.out.println();
            System.out.print("Trying to allocate the virtual machine...\n");
            // records the time of allocating the vm.
            long startTime = System.currentTimeMillis();

            OneResponse rc = VirtualMachine.allocate(oneClient, vmTemplate);

            if (rc.isError()) {
                System.out.println("failed!");
                throw new Exception(rc.getErrorMessage());
            }

            // The response message is the new VM's ID
            int newVMID = Integer.parseInt(rc.getMessage());
            System.out.println("OK ... VM ID " + newVMID + ".");

            // We can create a representation for the new VM, using the returned
            // VM-ID
            VirtualMachine vm = new VirtualMachine(newVMID, oneClient);

            if (rc.isError()) {
                System.out.println("failed!");
                throw new Exception(rc.getErrorMessage());
            } else
                System.out.println("OK.");

            // Display VM's information.
            rc = vm.info();
            if (rc.isError())
                throw new Exception(rc.getErrorMessage());

            System.out.println();
            System.out.println(
                    "This is the information OpenNebula stores for the new VM:");
            System.out.println(rc.getMessage() + "\n");

            // This VirtualMachine object has some helpers, so we can access its
            // attributes easily (remember to load the data first using the info
            // method).
            System.out.println("The new VM " +
                    vm.getName() + " has status: " + vm.status());

            // And we can also use xpath expressions
            System.out.println("The path of the disk is: " + vm.xpath("TEMPLATE/DISK/SOURCE"));
            while (vm.status() != "runn") {
                vm.info();
                //System.out.println("has status: "+vm.status());
            }
            System.out.println("Successfully created VM!");
			System.out.println("Hostname of the VM created is(Before Migration):" + vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HOSTNAME"));
            System.out.println("Host ID associated with the VM is(Before Migration):" + vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HID"));
            long endTime = System.currentTimeMillis();
            long elapsed = endTime - startTime;
            System.out.println("Time Elapsed is: " + elapsed + "ms!");
            
            // migrate this VM into a host.
            VMSample.retrieveInformation(oneClient);
            System.out.println("Power-ON Host ID list on the server: "+VMSample.allHostID);
			System.out.println("Checking the host with least memory usage to Migrate ....");
            //System.out.println("Power-ON Host info array: \n"+ VMSample.arrHost);
            int hostId = VMSample.allHostID.get(0);
			System.out.println("Host " +VMSample.allHostNM.get(0) + "has less memory usage"); 
            System.out.println("The new VM id: "+vm.getId() + " will migrate from host: "+vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HOSTNAME")+" to host: "+ VMSample.allHostNM.get(0) + " start...");
            long startTime1 = System.currentTimeMillis();
            rc = vm.migrate(hostId,true);
            if (rc.isError())
                throw new Exception(rc.getErrorMessage());
            if (rc.isError() == false){
                long endTime1 = System.currentTimeMillis();
                long elapsed1 = endTime1 - startTime1;
                System.out.println("Migrate Completed!");
                System.out.println("Migration Time Elapsed is: "+elapsed1+"milliseconds!");
                           }
            System.out.println(rc.getMessage() + "\n");
            System.out.println("The VM id: "+vm.getId() + ", migrated from host: "+vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HOSTNAME")+" to host: "+ VMSample.allHostNM.get(0) + " end!");
			System.out.println("Hostname of the VM is(After Migration):" + vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HOSTNAME"));
            System.out.println("Host ID associated with the VM is(After Migration):" + vm.xpath("HISTORY_RECORDS/HISTORY[last()]/HID"));
            System.out.println("Task 2 Complete!");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void printMachinePool (VirtualMachinePool vmPool)
    {
        System.out.println("--------------------------------------------");
        System.out.println("Number of VMs: " + vmPool.getLength());
        System.out.println("User ID\t\tName\t\tEnabled");

        for( VirtualMachine vm : vmPool )
        {
            String id = vm.getId();
            String name = vm.getName();
            String enab = vm.xpath("enabled");

            System.out.println(id+"\t\t"+name+"\t\t"+enab);
        }

        System.out.println("--------------------------------------------");
    }

}

