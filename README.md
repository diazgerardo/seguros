#Simulación Oracle Forms
la aplicación de ejemplo ejecuta dos microservicios

1) policy-service
2) rating-service

#Funcionamiento:
mientras que el primero expone un api q consulta y emite pólizas, controlando idempotencia y concurrencia
el segundo es consumido por el primero y efectúa cálculos en forma sincrónica para calcular la prima
funcionando exactamente igual que Oracle Forms: "si no puedo calcular la prima, no emito" (abortando el commit y lanzando exception)

#Observaciones:
el proyecto buildea y ejecuta sin errores usando maven
el ide está configurado para delegar el build en maven
sin embargo, el IDE presenta errores aún no resueltos
(aunque se usa el jdk y mvn nativos de WSL, presenta problemas para
acceder al sistema de archivos incluso cuando se usa el path previsto
para eso \\wsl$\Ubuntu\home\gerardo\pocs\seguros)
quizás un tema de permisos o hay que "escapar" las contrabarras o algun 
otro motivo

#Ejecución:
mvn -pl policy-service spring-boot:run \
-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:6005"

mvn -pl rating-service spring-boot:run \
-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:6006"

#Demostración Consumo:
gerardo@gera-wsl:~/pocs/seguros$ curl -X POST   "http://localhost:8080/policies?policyNumber=POL-001"

#Demostración Consumo Concurrente:
gerardo@gera-wsl:~/pocs/seguros$ (   curl -X POST "http://localhost:8080/policies/1/issue?premium=1000&tariffVersion=A" &   curl -X POST "http://localhost:8080/policies/1/issue?premium=2000&tariffVersion=B" &   wait; )

#Debugging:
gerardo@gera-wsl:~$ gerardo@gera-wsl:~$ ip addr show eth0 | grep inet
inet 172.20.12.41/20 brd 172.20.15.255 scope global eth0
inet6 fe80::215:5dff:fe23:8f5e/64 scope link

=> 172.20.12.41 es el ip de la sesion wsl
en intelliJ necesitamos dos debuggers conectados a dos jvm remotas, usando ese ip y apuntados a los puertos
que donde escuchan los agentes jdwp (en nuestro caso, 6005 y 6006)


