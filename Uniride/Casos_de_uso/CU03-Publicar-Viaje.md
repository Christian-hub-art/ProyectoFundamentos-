# CU03 - Publicar Viaje

**Actor:** Conductor

**Objetivo:** Registrar un nuevo viaje disponible para pasajeros.

## Guion

1. El conductor accede a “Publicar viaje”.
2. El sistema solicita origen, destino, fecha, hora, precio y asientos.
3. El actor ingresa los datos.
4. El sistema valida que la fecha/hora sean futuras.
5. El conductor confirma la publicación.
6. El sistema registra el viaje con estado DISPONIBLE.

## Excepciones

### **E1 – Fecha/hora inválidas**
1. El sistema muestra: “La fecha u hora no son válidas”.
2. Permite corregir.

### **E2 – Asientos inválidos**
1. El sistema muestra: "Debe ingresar mínimo 1 asiento disponible".

## Postcondiciones
- Viaje creado y visible para todos los pasajeros.
