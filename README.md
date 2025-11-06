# Burraco Java (Spring Boot + Supabase)

Backend Java che replica la logica MGX per il torneo di Burraco.
Espone API REST e salva su Supabase (Postgres).

## Requisiti
- JDK 17+
- Maven 3.9+
- Un database Supabase (Postgres) con credenziali

## Configurazione ambiente
Imposta queste variabili d'ambiente (ad es. in un file `.env` locale o nelle impostazioni del servizio):
```
SUPABASE_DB_URL=jdbc:postgresql://db.<project>.supabase.co:5432/postgres?sslmode=require
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=<your-db-password>
```

> Nota: se usi la connessione "Connection String" di Supabase, scegli "JDBC" e incolla direttamente l'URL con sslmode=require.

## Avvio locale
```bash
mvn spring-boot:run
```

API principali (default http://localhost:8080):
- `GET /api/health`
- `GET /api/participants`
- `POST /api/participants` (JSON: {id?, name, phone?, isPresent?})
- `DELETE /api/participants/{id}`
- `GET /api/draws`
- `POST /api/draws/next?prizeType=1`
- `DELETE /api/draws`

## Deploy
Puoi creare un jar eseguibile:
```bash
mvn -DskipTests package
java -jar target/burraco-java-1.0.0.jar
```
Puoi pubblicarlo su qualsiasi hosting Java (Render, Railway, Fly.io, VPS, ecc.).
