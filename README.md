# API Gatling Demo (Java 21 + Maven)

Proyecto de ejemplo para pruebas de rendimiento con **Gatling** usando el **Java DSL** y **Maven**.  
Incluye un set de simulaciones basadas en una API demo con autenticación, CRUD de usuarios y validaciones de seguridad.

---

## 🚀 Requisitos

- Java 21
- Maven 3.9.x o superior
- Acceso a la API demo (o servicio equivalente con los mismos endpoints)

---

## ⚙️ Configuración inicial

1. Clona el repositorio:
   ```cmd
   git clone https://github.com/marcosmrls/api-gatling-demo.git
   cd api-gatling-demo

2. Copia el archivo de credenciales de admin:
   ```cmd
   copy src/test/resources/data/admin.csv.example src/test/resources/data/admin.csv

3. Edita src/test/resources/data/admin.csv con credenciales válidas en tu entorno:

---

## ▶️ Ejecutar pruebas
   ```cmd
   mvnw.cmd gatling:test

   ó Parametrizado

   mvnw.cmd gatling:test \
  -Dgatling.simulationClass=com.gatling.perf.simulations.PerformanceSimulation \
  -DbaseUrl=http://localhost:3000 \
  -DUSERS=10 -DRAMP_DURATION=1 -DCONCURRENT_DURATION=2 -DTEST_DURATION=5
  ```

---

## 📊 Resultados
   ```cmd
   target/gatling/<simulation-name>-<timestamp>/index.html
