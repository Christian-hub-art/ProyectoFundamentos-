# CU07 - Cancelar Viaje

**Actor:** Conductor

**Objetivo:** Cancelar un viaje pendiente antes de su realización.

## Guion

1. El conductor abre “Mis viajes”.
2. Selecciona un viaje.
3. Presiona “Cancelar”.
4. El sistema pide confirmación.
5. El conductor confirma.
6. El sistema cambia el estado a CANCELADO.
7. Todas las reservas se actualizan a CANCELADA.

## Excepciones

### **E1 – Viaje ya iniciado**
1. El sistema muestra: “No se puede cancelar un viaje iniciado”.

## Postcondiciones
- Viaje cancelado.
- Reservas anuladas.
