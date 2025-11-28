# CU08 - Completar Viaje

**Actor:** Conductor

**Objetivo:** Marcar que el viaje fue realizado con éxito.

## Guion

1. El conductor accede a “Mis viajes”.
2. Selecciona un viaje cuya fecha ya pasó.
3. Presiona “Finalizar viaje”.
4. El sistema cambia estado a FINALIZADO.
5. Las reservas pasan a COMPLETADA.

## Excepciones

### **E1 – Viaje no ha ocurrido aún**
1. El sistema muestra: “Este viaje aún no puede finalizarse”.

## Postcondiciones
- Historial actualizado.
