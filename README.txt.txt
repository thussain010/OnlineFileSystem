Install Mongo Db: 
Step 1) Go to link(https://www.mongodb.com/download-center#community) and Download MongoDB Community Server. We will install the 64-bit version for Windows.
Step 2) Once download is complete open the msi file. Click Next in the start up screen
Step 3) Accept the End-User License Agreement Click Nest
Step 4) Click on the "complete" button to install all of the components. The custom option can be used to install selective components or if you want to change the location of the installation.
Step 5) Select “Run service as Network Service user”. make a note of the data directory, we’ll need this later.
Step 6) Click on the Install button to start the installation.
Step 7) Installation begins. Click Next once completed
Step 8) Click on the Finish button to complete the installation

Install Spring Tool Suite.
Step 1) Download Spring Tool Suite from https://spring.io/tools3/sts/all. Click on the platform which you are using. In this tutorial, we are using the Windows platform.
Step 2) Extract the zip file and install the STS.
sts-bundle -> sts-3.9.9.RELEASE -> Double-click on the STS.exe.
Step 3) Spring Tool Suite 3 Launcher dialog box appears on the screen. Click on the Launch button. You can change the Workspace if you want.
Step 4) It starts launching the STS.


Run the application:
Step 1) Run the Mongo server using command line.
Step 2) Select project folder using open project and go to that location and click on open.
Step 3) Clean the project and the pom.xml will download the required dependency using maven.
Step 4) Run the project by right clicking the pronject folder in the project explorer and run as Spring project.
Step 5) Go to the browser and input  http://localhost:8080/ or the port can be different.