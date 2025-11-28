@startuml
left to right direction
skin rose

actor Estudiante
actor Conductor
actor Pasajero

rectangle "UniRide - Sistema de Transporte Compartido" {

    Estudiante --> (Registrar Usuario)
    Estudiante --> (Iniciar Sesión)

    Pasajero --> (Buscar Viaje)
    Pasajero --> (Reservar Asiento)
    Pasajero --> (Cancelar Reserva)

    Conductor --> (Publicar Viaje)
    Conductor --> (Cancelar Viaje)
    Conductor --> (Completar Viaje)

    Pasajero --> (Ver Mis Reservas)
    Conductor --> (Ver Mis Viajes)
}
@enduml
