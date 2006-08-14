Integration of policy engines in Globus Toolkit 4.0

Installation instructions:
1) run ant for compiling the project (service+client)
2) as user globus run ant deployservice for deploying the MathService into the Globus container
3) start the globus container
4) as your usual user run ant runClient

*5) to undeploy the service run ant undeployservice
 
The service/client has been tested with GT 4.0.0