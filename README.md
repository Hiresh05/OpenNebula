# COMP5850M Coursework 2

Login to csgate1 server on Open Nebula and examine Dashboard:
1)	https://csgate1.leeds.ac.uk:8443/
2)	Go to Virtual Infrastructure section left hand side, check available Virtual Machines and Templates

Connect to the csgate1 server using remote connection:
1)	You can either connect to the remote server using https://feng-linux.leeds.ac.uk/
2)	Or using any ssh tool. For this task I am using XShell on Windows

In order to access the code file perform following steps:
1)	Log in to the csgate1 server using your username and password
2)	After logging in create a folder name /coursework or any specific name you want and copy the.java file in it
3)	Compile the code using :
javac VMcwtask12.java
java VMcwtask12

4)	Monitor the Virtual Machine on Open Nebula before and After instantiation of the VM
Monitoring VM using Zabbix:
  a)	Register your VM to Zabbix using VM Spot Metrics
  b)	Log in as guest on https://csgate1.leeds.ac.uk:8443/zabbix/zabbix.php?action=dashboard.view
  c)	Go to Monitoring -> Graph and select the host on which your VM is running and monitor the graphs
