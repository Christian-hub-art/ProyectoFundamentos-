# CU06 - Cancelar Reserva

**Actor:** Pasajero

**Objetivo:** Cancelar una reserva ya hecha.

## Guion

1. El pasajero accede a “Mis reservas”.
2. Selecciona una reserva.
3. El sistema pide confirmación.
4. El usuario confirma.
5. El sistema cambia el estado a CANCELADA.
6. Los asientos regresan al viaje.

## Excepciones

### **E1 – Reserva ya completada**
1. El sistema muestra: “No se puede cancelar una reserva completada”.

## Postcondiciones
- Reserva cancelada.
- Asientos restaurados.
