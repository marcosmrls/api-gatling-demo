# API Gatling Demo (Java 21 + Maven)

Proyecto de ejemplo para pruebas de rendimiento con **Gatling** usando el **Java DSL** y **Maven**.  
Incluye un set de simulaciones basadas en una API demo con autenticación, CRUD de usuarios y validaciones de seguridad.

---

## 📂 Estructura del proyecto

src/test/java/com/gatling/perf
├── config/HttpProtocol.java # Configuración HTTP común
├── feeders/ # Feeders dinámicos y CSV
├── scenarios/CommonChains.java # Reutilización de cadenas (health, login, CRUD, etc.)
└── simulations/PerformanceSimulation.java # Ejemplo de simulación completa

src/test/resources
├── bodies/ # Templates JSON para requests
│ ├── login.json
│ └── user-create.json
└── data/
├── admin.csv.example # Plantilla de credenciales nivel admin
└── users.csv.example # Plantilla de credenciales nivel user

------------------------------------------------------------------------

## 🚀 Requisitos

- Java 21
- Maven 3.9.x o superior
- Acceso a la API demo (o servicio equivalente con los mismos endpoints)

------------------------------------------------------------------------

## ⚙️ Configuración inicial

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/api-gatling-demo.git
   cd api-gatling-demo

2. Copia el archivo de credenciales de admin:
   ```bash
   cp src/test/resources/data/admin.csv.example src/test/resources/data/admin.csv

3. Edita src/test/resources/data/admin.csv con credenciales válidas en tu entorno:

▶️ Ejecutar pruebas
   ```bash
   mvnw.cmd gatling:test

   mvnw.cmd gatling:test - \
  -Dgatling.simulationClass=com.gatling.perf.simulations.PerformanceSimulation \
  -DbaseUrl=http://localhost:3000 \
  -DUSERS=10 -DRAMP_DURATION=1 -DCONCURRENT_DURATION=2 -DTEST_DURATION=5

📊 Resultados
   ```bash
   target/gatling/<simulation-name>-<timestamp>/index.html
