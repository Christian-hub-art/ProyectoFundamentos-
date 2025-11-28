# CU05 - Reservar Asiento

**Actor:** Pasajero

**Objetivo:** Reservar uno o varios asientos en un viaje.

## Guion

1. El pasajero selecciona un viaje disponible.
2. Indica la cantidad de asientos.
3. El sistema verifica disponibilidad.
4. El sistema crea la reserva y descuenta los asientos.
5. Se marca la reserva como CONFIRMADA.

## Excepciones

### **E1 – Asientos insuficientes**
1. El sistema muestra: “No hay asientos disponibles suficientes”.

### **E2 – Viaje cancelado**
1. Se muestra: “Este viaje ya no está disponible”.

## Postcondiciones
- Reserva creada y asociada al pasajero.
